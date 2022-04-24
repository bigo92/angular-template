package vnpt.net.syndata.configuration.jobbase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.quartz.JobDataMap;
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
import vnpt.net.syndata.utils.Utils;

/*
* Lớp chịu trách nhiệm excute các job được khai bao trong schedule setting, 
* cần locktask khi thực thiện task
*/
public class QuartzJobLauncher extends QuartzJobBean {

  private JobLauncher jobLauncher;
  private JobLocator jobLocator;
  private HttpClientComponent httpClient;
  private Boolean consoleLog;
  private String urlDothing;
  private String urlGetJob;

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    JobDetail jobDetail = context.getJobDetail();
    JobDataMap jobDataMap = jobDetail.getJobDataMap();
    try {
      String jobId = jobDetail.getKey().getName();
      String jobDescription = jobDetail.getDescription();
      String jobGroup = jobDetail.getKey().getGroup();
      String triggerCron = context.getTrigger().getDescription();
      boolean isJobSingle = jobDataMap.getBoolean("isSingle"); //là single hay multi

      // lock task insert vào bảng processing singal hay muilti
      //select  ra check time, ok => run job 0k => insert log xong => xóa trong process đi
//      String transactionId = Utils.createUUID();

      //check type job single hay multi

      EJson jsonParam = new EJson();
      jsonParam.put("typeJob", isJobSingle);
      jsonParam.put("settingId", jobId);
      jsonParam.put("serverIP", "");
      jsonParam.put("isLast", 1);

      /*Đăng ký job*/
      int errorCode = 0;
      int retry = 2;
      boolean success = false;
      String transactionId = "";
      do {
        try {
          jsonParam.put("action", Utils.ACTION_ADD);
          ResponseEntity<String> response = httpClient.postDataByApi(urlDothing, null, jsonParam.jsonString());
          EJson jsonResult = new EJson(response.getBody());
          transactionId = jsonResult.getString("transactionId");
          errorCode = jsonResult.getLong("errorCode").intValue();
          success = true;
        } catch (Exception e) {
          retry--;
          success = false;
          if (retry == 0){
            errorCode = -1;
            success = true;
          }
          System.out.println(e);
        }
      }while (!success);

      if (errorCode == 0) {
        int retryGetJob = 2;
        boolean successGet = false;
        String dataJob = "";
        do {
          try {
            Map<String, Object> mapParam =  new HashMap<>();
            mapParam.put("transactionId", transactionId);
            ResponseEntity<String> response = httpClient.getDataByApiQueryParam(urlGetJob, mapParam);
            dataJob = response.getBody();
            successGet = true;
          } catch (Exception e) {
            retryGetJob--;
            successGet = false;
            if (retryGetJob == 0){
              successGet = true;
            }
            System.out.println(e);
          }
        }while (!successGet);

        EJson jobInfo = new EJson(dataJob);
        if (jobInfo.getBoolean("isEmpty")){
          System.out.println("Job không đăng ký được lịch trình!");
          return;
        }

        String jobName = jobInfo.getString("NAME_JOB");//jsonParam.getString("SCHEDULE_SETTING_FUNCTION");
        Long jobRetry = Long.getLong("0");//sonParam.getLong("SCHEDULE_SETTING_RETRY");
        String jobCron = jobInfo.getString("TIME_CROSS"); //jsonParam.getString("SCHEDULE_SETTING_CRON");
        String jobParam = jobInfo.getString("JSON_PARAM");//jsonParam.getString("SCHEDULE_SETTING_PARAM");

          try {
            EJson paramJson = new EJson(jobParam);
            if (!triggerCron.equals(jobCron)) {
              // có thay đổi lịch chạy job trên database so với lịch job đăng ký hiện tại
              if (consoleLog) {
                System.out.println("Job fail: có thay đổi lịch chạy job trên database so với lịch job đăng ký hiện tại");
              }
              return;
            }

            JobParameters jobParameters = new JobParametersBuilder().addString("PARAM", jobParam)
                .addString("JOBID", jobId).addString("JOBGROUP", jobGroup).addString("JOBDESCRIPTION", jobDescription)
                .addString("JOBNAME", jobName).addLong("TIME", System.currentTimeMillis()).toJobParameters();

            Job job = jobLocator.getJob("executeApiJob");
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
            EJson jsonDel = new EJson();
            jsonDel.put("action", Utils.ACTION_DEL);
            jsonDel.put("transactionId", transactionId);
            ResponseEntity<String> response = httpClient.postDataByApi(urlDothing, null, jsonDel.jsonString());
            EJson resultDel = new EJson(response.getBody());
            if (resultDel.getLong("errorCode").intValue() > 0) {
              System.out.println("unlockTask:" + transactionId);
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

  public String getUrlDothing() {
    return urlDothing;
  }

  public void setUrlDothing(String urlDothing) {
    this.urlDothing = urlDothing;
  }

  public String getUrlGetJob() {
    return urlGetJob;
  }

  public void setUrlGetJob(String urlGetJob) {
    this.urlGetJob = urlGetJob;
  }
}