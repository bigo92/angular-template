package vnpt.net.syndata.configuration.jobbase;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.utils.EJson;

/*
* Lớp chịu trách nhiệm excute các job được khai bao trong schedule setting, 
* cần locktask khi thực thiện task
*/
public class QuartzJobLauncher extends QuartzJobBean {

  private JobLauncher jobLauncher;
  private JobLocator jobLocator;
  private HttpClientComponent httpClient;
  private Boolean consoleLog;

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    JobDetail jobDetail = context.getJobDetail();
    try {
      String jobId = jobDetail.getKey().getName();
      String jobDescription = jobDetail.getDescription();
      String jobGroup = jobDetail.getKey().getGroup();
      String triggerCron = context.getTrigger().getDescription();

      // lock task
      ResponseEntity<String> resultLock = null;
      try {
        resultLock = httpClient.lockTask(jobId);
      } catch (Exception e) {
        try {
          // log error
          LogError(jobDetail, null, null, "LockTask Error:"+ jobId, e.getLocalizedMessage(), null);
        } catch (Exception e2) {
          if (consoleLog) {
            System.out.println("executeInternal Job Fail: " + e2.getMessage());
          }
        }
      }
      if (resultLock != null && resultLock.getStatusCode() == HttpStatus.OK) {
        EJson jsonParam = new EJson(resultLock.getBody());
        if (jsonParam.getString("MESSAGE").equals("SUCCESS")) {

          String jobName = jsonParam.getString("SCHEDULE_SETTING_FUNCTION");
          Long jobRetry = jsonParam.getLong("SCHEDULE_SETTING_RETRY");
          String jobCron = jsonParam.getString("SCHEDULE_SETTING_CRON");
          String jobParam = jsonParam.getString("SCHEDULE_SETTING_PARAM");

          try {
            EJson paramJson = new EJson(jobParam);
            if (!triggerCron.equals(jobCron)) {
              // có thay đổi lịch chạy job trên database so với lịch job đăng ký hiện tại
              if (consoleLog) {
                System.out
                    .println("Job fail: có thay đổi lịch chạy job trên database so với lịch job đăng ký hiện tại");
              }
              return;
            }

            JobParameters jobParameters = new JobParametersBuilder().addString("PARAM", jobParam)
                .addString("JOBID", jobId).addString("JOBGROUP", jobGroup).addString("JOBDESCRIPTION", jobDescription)
                .addString("JOBNAME", jobName).addLong("TIME", System.currentTimeMillis()).toJobParameters();

            Job job = jobLocator.getJob(jobName);
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
            List<Throwable> failureExceptions = stepExecution.getFailureExceptions();
            if (failureExceptions.isEmpty()) {
              // loc success
              String resultJob = stepExecution.getExecutionContext().getString("RESULT");
              final Object resultBody = stepExecution.getExecutionContext().get("CONTENT");
              LogSuccess(jobId, paramJson, new EJson(resultJob), resultBody != null ? (String) resultBody : null);
            } else {
              Throwable throwable = failureExceptions.get(0);
              // log error
              if (stepExecution.getExecutionContext().get("RESULT") != null) {
                String resultJob = stepExecution.getExecutionContext().getString("RESULT");
                if (resultJob != null) {
                  // update param nếu trường hợp lỗi có đánh dấu result trả về
                  // Cụ thể các trường hợp sử lý file cần đánh giấu đang lỗi ở file nào.
                  // hệ thống retry sẽ tiếp tục sử lý từ file lỗi đó
                  jobParam = resultJob;
                }
              }
              LogError(jobDetail, jobName, jobParam, throwable.getMessage(), throwable.getLocalizedMessage(), jobRetry);
            }
            if (consoleLog) {
              System.out.println("########### Status: " + jobExecution.getStatus());
            }

          } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
              | JobParametersInvalidException | NoSuchJobException e) {
            try {
              // log error
              LogError(jobDetail, jobName, jobParam, e.getMessage(), e.getLocalizedMessage(), jobRetry);
            } catch (Exception e2) {
              if (consoleLog) {
                System.out.println("executeInternal Job Fail: " + e2.getMessage());
              }
            }
          } finally {
            // unlock task
            httpClient.unLockTask(jobId);
            if (consoleLog) {
              System.out.println("unlockTask:" + jobId);
            }
          }
        } else {
          // log thanh cong nhung ko co ket qua
          // EJson resultLog = new EJson();
          // resultLog.put("status", "success");
          // httpClient.addLog(jobId, null, resultLog,
          // "TaskId:" + jobId + "-" + jobDescription + ".Đã được hệ thống khác xử lý",
          // null, null);
          if (consoleLog) {
            System.out.println("lockTaskFail do task đã được hệ thống khác lock:" + jobId);
          }
        }
      }
    } catch (Exception e) {
      try {
        // log error
        LogError(jobDetail, null, null, e.getMessage(), e.getLocalizedMessage(), null);
      } catch (Exception e2) {
        if (consoleLog) {
          System.out.println("executeInternal Job Fail: " + e2.getMessage());
        }
      }
    }
  }

  private void LogError(JobDetail job, String jobName, String param, String message, String trace, Long retry) {
    if (retry == null) {
      retry = 0l;
    }
    EJson resultLog = new EJson();
    resultLog.put("STATUS", "FAIL");
    resultLog.put("JOBID", job.getKey().getName());
    resultLog.put("JOBNAME", jobName);
    resultLog.put("JOBGROUP", job.getKey().getGroup());
    resultLog.put("JOBDESCRIPTION", job.getDescription());
    resultLog.put("RETRYMAX", retry);
    resultLog.put("RETRYTURN", 0);
    httpClient.addLog(job.getKey().getName(), new EJson(param), resultLog, message, trace, null);
  }

  private void LogSuccess(String jobId, EJson param, EJson result, String file) {
    httpClient.addLog(jobId, param, result, null, null, file);
  }

  public JobLauncher getJobLauncher() {
    return jobLauncher;
  }

  public void setJobLauncher(JobLauncher jobLauncher) {
    this.jobLauncher = jobLauncher;
  }

  public JobLocator getJobLocator() {
    return jobLocator;
  }

  public void setJobLocator(JobLocator jobLocator) {
    this.jobLocator = jobLocator;
  }

  public HttpClientComponent getHttpClient() {
    return httpClient;
  }

  public void setHttpClient(HttpClientComponent httpClient) {
    this.httpClient = httpClient;
  }

  public Boolean getConsoleLog() {
    return consoleLog;
  }

  public void setConsoleLog(Boolean consoleLog) {
    this.consoleLog = consoleLog;
  }
}