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
import org.springframework.scheduling.quartz.QuartzJobBean;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.utils.EJson;

/*
* Lớp chịu trách nhiệm excute các job trực tiếp không cần locktask
*/
public class QuartzExcuteJobLaucher extends QuartzJobBean {

    private String jobName;
    private JobLauncher jobLauncher;
    private JobLocator jobLocator;
    private HttpClientComponent httpClient;
    private Boolean consoleLog;
    private Boolean systemLog;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        try {
            JobParameters jobParameters = new JobParametersBuilder().addLong("TIME", System.currentTimeMillis()).toJobParameters();

            Job job = jobLocator.getJob(jobName);
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
            List<Throwable> failureExceptions = stepExecution.getFailureExceptions();
            if (!failureExceptions.isEmpty()) {
                Throwable throwable = failureExceptions.get(0);
                // log error
                if (systemLog) {
                    LogError(jobDetail, jobName, throwable.getMessage(), throwable.getLocalizedMessage());
                }
            }
            if (consoleLog) {
                System.out.println("########### Status: " + jobExecution.getStatus());
            }
        } catch (Exception e) {
            try {
                if (systemLog) {
                    LogError(jobDetail, "QuartzInitJobLaucher", e.getMessage(), e.getLocalizedMessage());
                }
            } catch (Exception e2) {
                if (consoleLog) {
                    System.out.println("executeInternal Init Fail: " + e2.getMessage());
                }
            }
        }
    }

    private void LogError(JobDetail job, String jobName, String message, String trace) {
        EJson resultLog = new EJson();
        resultLog.put("STATUS", "FAIL");
        resultLog.put("JOBID", job.getKey().getName());
        resultLog.put("JOBNAME", jobName);
        resultLog.put("JOBGROUP", job.getKey().getGroup());
        resultLog.put("JOBDESCRIPTION", job.getDescription());
        resultLog.put("RETRYMAX", 0);
        resultLog.put("RETRYTURN", 0);
        httpClient.addLog(job.getKey().getName(), null, resultLog, message, trace, null);
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public Boolean getSystemLog() {
        return systemLog;
    }

    public void setSystemLog(Boolean systemLog) {
        this.systemLog = systemLog;
    }

}