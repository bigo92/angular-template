package vnpt.net.syndata.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vnpt.net.syndata.configuration.jobbase.*;
import vnpt.net.syndata.job.*;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  // ----------------- list schedule task---------------------------------
  // ----------------- initializeScheduleJob---------------------Global
  @Bean
  public Step initializeScheduleStep() {
    return stepBuilderFactory.get("initializeScheduleStep").tasklet(initializeScheduleTasklet()).build();
  }

  @Bean
  public Job initializeScheduleJob() {
    return jobBuilderFactory.get("initializeScheduleJob").incrementer(new RunIdIncrementer())
        .flow(initializeScheduleStep()).end().build();
  }

  @Bean
  public Tasklet initializeScheduleTasklet() {
    InitializeSchedule taslet = new InitializeSchedule();
    return taslet;
  }

  // ----------------- clearTrashScheduleJob---------------------giu
  @Bean
  public Step clearTrashScheduleStep() {
    return stepBuilderFactory.get("clearTrashScheduleStep").tasklet(clearTrashScheduleTasklet()).build();
  }

  @Bean
  public Job clearTrashScheduleJob() {
    return jobBuilderFactory.get("clearTrashScheduleJob").incrementer(new RunIdIncrementer())
        .flow(clearTrashScheduleStep()).end().build();
  }

  @Bean
  public Tasklet clearTrashScheduleTasklet() {
    ClearTrashSchedule taslet = new ClearTrashSchedule();
    return taslet;
  }

  // -----------------------------------------------------------------
  // ----------------- Job Tạo file từ api ---------------------------delete
  @Bean
  public Step createFileStep() {
    return stepBuilderFactory.get("createFileStep").tasklet(createFileTasklet()).build();
  }

  @Bean
  public Job createFileJob() {
    return jobBuilderFactory.get("createFileJob").incrementer(new RunIdIncrementer()).flow(createFileStep())
        .end().build();
  }

  @Bean
  public Tasklet createFileTasklet() {
    CreateFileJob tasklet = new CreateFileJob();
    return tasklet;
  }
  // -----------------------------------------------------------------------------------------------
  // --- Job đọc file và gọi api (cấu hình outFile sẽ tạo file,không cấu hình thì ko tạo file) -------
  @Bean
  public Step receiveFileStep() {
    return stepBuilderFactory.get("receiveFileStep").tasklet(receiveFileTasklet()).build();
  }

  @Bean
  public Job receiveFileJob() {
    return jobBuilderFactory.get("receiveFileJob").incrementer(new RunIdIncrementer())
        .flow(receiveFileStep()).end().build();
  }

  @Bean
  public Tasklet receiveFileTasklet() {
    ReceiveFileJob tasklet = new ReceiveFileJob();
    return tasklet;
  }
  // -----------------------------------------------------------------
  // ---------------- Job đọc api đầu vào và api đầu ra --------------
  @Bean
  public Step synDataStep() {
    return stepBuilderFactory.get("synDataStep").tasklet(synDataTasklet()).build();
  }

  @Bean
  public Job synDataJob() {
    return jobBuilderFactory.get("synDataJob").incrementer(new RunIdIncrementer())
        .flow(synDataStep()).end().build();
  }

  @Bean
  public Tasklet synDataTasklet() {
    SynDataJob tasklet = new SynDataJob();
    return tasklet;
  }
  // -----------------------------------------------------------------
   // ---------------- Job gọi 1 api đặt lịch --------------
   @Bean
   public Step executeApiStep() {
     return stepBuilderFactory.get("executeApiStep").tasklet(executeApiTasklet()).build();
   }
 
   @Bean
   public Job executeApiJob() {
     return jobBuilderFactory.get("executeApiJob").incrementer(new RunIdIncrementer())
         .flow(executeApiStep()).end().build();
   }
 
   @Bean
   public Tasklet executeApiTasklet() {
    ExecuteApiJob tasklet = new ExecuteApiJob();
     return tasklet;
   }
   // -----------------------------------------------------------------
}