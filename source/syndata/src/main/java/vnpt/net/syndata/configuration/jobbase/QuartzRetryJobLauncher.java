package vnpt.net.syndata.configuration.jobbase;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.utils.EJson;

/*
* Lớp chịu trách nhiệm excute các job bị lỗi được khai bao trong schedule log, 
* cần locktask khi thực thiện task
*/
public class QuartzRetryJobLauncher extends QuartzJobBean {

  private JobLauncher jobLauncher;
  private JobLocator jobLocator;
  private HttpClientComponent httpClient;
  private Boolean consoleLog;

  public JobLauncher getJobLauncher() {
    return jobLauncher;
  }

  public void setJobLauncher(final JobLauncher jobLauncher) {
    this.jobLauncher = jobLauncher;
  }

  public JobLocator getJobLocator() {
    return jobLocator;
  }

  public void setJobLocator(final JobLocator jobLocator) {
    this.jobLocator = jobLocator;
  }

  public HttpClientComponent getHttpClient() {
    return httpClient;
  }

  public void setHttpClient(final HttpClientComponent httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
    final JobDetail jobDetail = context.getJobDetail();
    try {
      // lấy task bị lỗi và lock 1 tiến trình thực hiện lại task
      final ResponseEntity<String> result = httpClient.lockTaskRetry();
      if (result.getStatusCode() == HttpStatus.OK) {
        final EJson resultJson = new EJson(result.getBody());
        if (!resultJson.getString("MESSAGE").equals("SUCCESS")) {
          // không tim được task lỗi nào để retry
          return;
        }
        String jobParam = resultJson.getString("SCHEDULE_LOG_PARAM");
        final EJson logReturn = new EJson(resultJson.getString("SCHEDULE_LOG_RETURN"));
        final Long retryId = resultJson.getLong("SCHEDULE_LOG_ID");
        final String jobId = logReturn.getString("JOBID");
        final String jobName = logReturn.getString("JOBNAME");
        final String jobGroup = logReturn.getString("JOBGROUP");
        final String jobDescription = logReturn.getString("JOBDESCRIPTION");

        try {
          // excute job retry
          final JobParameters jobParameters = new JobParametersBuilder().addString("PARAM", jobParam)
              .addString("JOBID", jobId).addString("JOBNAME", jobName).addString("JOBGROUP", jobGroup)
              .addString("JOBDESCRIPTION", jobDescription).addLong("RETRYID", retryId)
              .addLong("TIME", System.currentTimeMillis()).toJobParameters();

          final Job job = jobLocator.getJob(jobName);
          final JobExecution jobExecution = jobLauncher.run(job, jobParameters);

          final StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
          final List<Throwable> failureExceptions = stepExecution.getFailureExceptions();
          if (failureExceptions.isEmpty()) {
            // log success
            final String resultJob = stepExecution.getExecutionContext().getString("RESULT");
            final Object resultBody = stepExecution.getExecutionContext().get("CONTENT");
            httpClient.addLogRetry(retryId, jobId, null, new EJson(resultJob), null, null, resultBody != null ? (String) resultBody : null);
          } else {
            final Throwable throwable = failureExceptions.get(0);
            // log error
            if (stepExecution.getExecutionContext().get("RESULT") != null) {
              final String resultJob = stepExecution.getExecutionContext().getString("RESULT");
              if (resultJob != null) {
                // update param nếu trường hợp lỗi có đánh dấu result trả về
                // Cụ thể các trường hợp sử lý file cần đánh giấu đang lỗi ở file nào.
                // hệ thống retry sẽ tiếp tục sử lý từ file lỗi đó
                jobParam = resultJob;
              }
            }
            addLogRetry(retryId, jobId, new EJson(jobParam), throwable.getMessage(), throwable.getLocalizedMessage());
          }
        } catch (final Exception e) {
          addLogRetry(retryId, jobId, null, e.getMessage(), e.getLocalizedMessage());
        } finally {
          // unlock task
          httpClient.unLockTaskRetry(retryId);
          if (consoleLog) {
            System.out.println("unlockTask:" + retryId);
          }
        }
      }
    } catch (final Exception e) {
      try {
        LogError(jobDetail, e.getMessage(), e.getLocalizedMessage());
      } catch (final Exception e2) {
        if (consoleLog) {
          System.out.println("executeInternal Retry Fail: " + e2.getMessage());
        }
      }
    }
  }

  private void addLogRetry(final Long logId, final String jobId, final EJson param, final String message,
      final String trace) {
    final EJson resultLog = new EJson();
    resultLog.put("STATUS", "FAIL");
    httpClient.addLogRetry(logId, jobId, param, resultLog, message, trace, null);
  }

  private void LogError(final JobDetail job, final String message, final String trace) {
    final EJson resultLog = new EJson();
    resultLog.put("STATUS", "FAIL");
    resultLog.put("JOBID", job.getKey().getName());
    resultLog.put("JOBNAME", "QUARTZRETRYJOBLAUNCHER");
    resultLog.put("JOBGROUP", job.getKey().getGroup());
    resultLog.put("JOBDESCRIPTION", job.getDescription());
    resultLog.put("RETRYMAX", 0);
    resultLog.put("RETRYTURN", 0);
    httpClient.addLog(job.getKey().getName(), null, resultLog, message, trace, null);
  }

  public Boolean getConsoleLog() {
    return consoleLog;
  }

  public void setConsoleLog(Boolean consoleLog) {
    this.consoleLog = consoleLog;
  }
}