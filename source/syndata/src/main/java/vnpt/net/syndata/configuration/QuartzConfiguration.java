package vnpt.net.syndata.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.quartz.SimpleTrigger;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.configuration.jobbase.*;

@Configuration
public class QuartzConfiguration {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private HttpClientComponent httpClient;

    @Value("${spring.batch.console-log}")
    private Boolean consoleLog;

    @Value("${quartz.threadPool.threadCount}")
    private String threadCount;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    // -------------------------- job base --------------------------
    @Bean
    public JobDetailFactoryBean jobDetailInitializeSchedule() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(QuartzExcuteJobLaucher.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jobName", "initializeScheduleJob");
        map.put("jobLauncher", jobLauncher);
        map.put("jobLocator", jobLocator);
        map.put("httpClient", httpClient);
        map.put("consoleLog", consoleLog);
        // khởi tạo schedule sẽ không log lên database do job initializeSchedule là job
        // base không khai báo trong schedule-setting
        map.put("systemLog", false);
        jobDetailFactoryBean.setJobDataAsMap(map);

        return jobDetailFactoryBean;
    }

    @Bean
    public JobDetailFactoryBean jobDetailClearTrashSchedule() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(QuartzExcuteJobLaucher.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jobName", "clearTrashScheduleJob");
        map.put("jobLauncher", jobLauncher);
        map.put("jobLocator", jobLocator);
        map.put("httpClient", httpClient);
        map.put("consoleLog", consoleLog);
        // Dọn rác schedule sẽ không log lên database do job clearTrashScheduleJob là
        // job base không khai báo trong schedule-setting
        map.put("systemLog", false);
        jobDetailFactoryBean.setJobDataAsMap(map);

        return jobDetailFactoryBean;
    }

    @Bean
    public JobDetailFactoryBean jobDetailRetrySchedule() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(QuartzRetryJobLauncher.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jobLauncher", jobLauncher);
        map.put("jobLocator", jobLocator);
        map.put("httpClient", httpClient);
        map.put("consoleLog", consoleLog);
        jobDetailFactoryBean.setJobDataAsMap(map);

        return jobDetailFactoryBean;
    }

    // -------------------------- trigger --------------------------
    @Bean
    public SimpleTriggerFactoryBean triggerInitializeSchedule() {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(jobDetailInitializeSchedule().getObject());
        trigger.setRepeatInterval(120000);// 2 phut se verify hệ thống 1 lan
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return trigger;
    }

    @Bean
    public CronTriggerFactoryBean cronTriggerClearTrashSchedule() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetailClearTrashSchedule().getObject());
        cronTriggerFactoryBean.setCronExpression("0 0 0 ? * * *");// 12h đêm hàng ngày sẽ check
        return cronTriggerFactoryBean;
    }

    @Bean
    public CronTriggerFactoryBean cronTriggerRetrySchedule() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetailRetrySchedule().getObject());
        cronTriggerFactoryBean.setCronExpression("0 */3 * ? * *");// cứ 5 phút sẽ kiểm tra các task bị lỗi
        return cronTriggerFactoryBean;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadCount", threadCount);
        schedulerFactoryBean.setQuartzProperties(properties);

        //schedulerFactoryBean.setTriggers(cronTriggerClearTrashSchedule().getObject());
        // schedulerFactoryBean.setTriggers(triggerInitializeSchedule().getObject());
        // schedulerFactoryBean.setTriggers(cronTriggerRetrySchedule().getObject());
        schedulerFactoryBean.setTriggers(triggerInitializeSchedule().getObject(), cronTriggerClearTrashSchedule().getObject());
        return schedulerFactoryBean;
    }
}