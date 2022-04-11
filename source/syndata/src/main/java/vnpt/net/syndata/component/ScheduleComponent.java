package vnpt.net.syndata.component;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import vnpt.net.syndata.configuration.jobbase.QuartzJobLauncher;

@Component
public class ScheduleComponent {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private HttpClientComponent httpClient;

    @Autowired
    private ScheduleCallApiComponent scheduleCallApiComponent;

    @Value("${spring.batch.console-log}")
    private Boolean consoleLog;

    public void addSchedule(String jobId, String jobGroup, String jobDescription, String cronSchedule, String param) throws ClassNotFoundException, SchedulerException {
        if (!scheduler.checkExists(new JobKey(jobId, jobGroup))) {
            // chưa có job được đăng ký => đăng ký job mới
            JobDetail jobDetail = buildJobDetail(jobId, jobGroup, jobDescription, param);
            Trigger trigger = buildJobTrigger(jobDetail, cronSchedule);
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            // job đã dược đăng ký
            TriggerKey triggerKey = new TriggerKey(jobId, jobGroup);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            // kiểm tra xem trigger có thay đổi lịch hay không?
            if (!trigger.getDescription().equals(cronSchedule)) {
                JobDetail jobDetail = buildJobDetail(jobId, jobGroup, jobDescription, param);
                // update job với lịch mới
                scheduler.rescheduleJob(triggerKey, buildJobTrigger(jobDetail, cronSchedule));
            }
        }
    }

    public void removeSchedule(String jobId, String jobGroup) throws SchedulerException {
        if (scheduler.checkExists(new JobKey(jobId, jobGroup))) {
            scheduler.deleteJob(new JobKey(jobId, jobGroup));
        }
    }


    /*Build job với data*/
    private JobDetail buildJobDetail(String jobId, String jobGroup, String jobDescription, String param)
            throws ClassNotFoundException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);
//        jobDataMap.put("httpClient", httpClient);
        jobDataMap.put("settingParam", param);
        jobDataMap.put("consoleLog", consoleLog);
        return JobBuilder.newJob(QuartzJobLauncher.class).withIdentity(jobId, jobGroup).withDescription(jobDescription)
                .usingJobData(jobDataMap).storeDurably().build();
    }

    /*build job với jobdetail + cron time*/
    private Trigger buildJobTrigger(JobDetail jobDetail, String cronSchedule) {
        return TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), jobDetail.getKey().getGroup()).withDescription(cronSchedule)
                .startNow().withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule)).build();
    }
}