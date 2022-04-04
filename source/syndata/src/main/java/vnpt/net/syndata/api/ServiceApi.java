package vnpt.net.syndata.api;

import java.io.File;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vnpt.net.syndata.component.CreateFileComponent;
import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.utils.EJson;

@RestController
@RequestMapping("/api/service")
public class ServiceApi {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobLocator jobLocator;
    @Autowired
    private HttpClientComponent httpClient;
    @Autowired
	private CreateFileComponent file;

    @Value("${api.system.base.url}")
    private String apiSystemBaseUrl;

    @Value("${api.system.reset.url}")
    private String apisystemResetUrl;

    @Value("${spring.batch.console-log}")
    private Boolean consoleLog;

    @GetMapping(value = "test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Ok");
    }

    @GetMapping(value = "test-value-format")
    public ResponseEntity<String> testValueByFormat(String fileOrgin, String format){
        String sumData = file.getValueByFormat("${SUM}", fileOrgin, format);
        return ResponseEntity.ok(sumData);
    }

    @PostMapping(value = "test-sum", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Long> testSum(@RequestBody String data){
        byte[] fileByte = data.getBytes();
        Long checkSum = file.getCRC32Checksum(fileByte);
        return ResponseEntity.ok(checkSum);
    }

    @GetMapping(value = "test-sum-file", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> testSumFile(String fileName) throws Exception {
        // lay cau hinh thu muc luu file
        ResponseEntity<String> paraResult = httpClient.getSysPara();
        if (paraResult.getStatusCode() == HttpStatus.OK) {
            // doc config
            final EJson paraJson = new EJson(paraResult.getBody());
            String pendingFilePhysic = "";
            String doneFilePhysic = "";
            final List<EJson> paraItem = paraJson.getJSONArray("ITEMS");

            for (final EJson eJson : paraItem) {
                // if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.INTERNAL.EREA.FILE.SAVING.FOLDER")) {
                //     pendingFilePhysic = eJson.getString("PARA_VALUE");
                // }
                // if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.DONE.EREA.FILE.SAVING.FOLDER")) {
                //     doneFilePhysic = eJson.getString("PARA_VALUE");
                // }
            
                    //if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.INTERNAL.EREA.FILE.SAVING.FOLDER")) {
                    if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.READ.EREA.FILE.FOLDER")) {
                        pendingFilePhysic = eJson.getString("PARA_VALUE");
                    }
                    //if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.DONE.EREA.FILE.SAVING.FOLDER")) {
                    if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.DONE.EREA.FILE.FOLDER")) {
                        doneFilePhysic = eJson.getString("PARA_VALUE");
                    }
                
            }

            // check file co trong thu muc pendung ko
            File tempFile = new File(pendingFilePhysic + fileName);
            boolean exists = tempFile.exists();
            String jsonData = "";
            if (exists) {
                jsonData = file.OpenFile(pendingFilePhysic + fileName);
            } else {
                tempFile = new File(doneFilePhysic + fileName);
                exists = tempFile.exists();
                if (exists) {
                    jsonData = file.OpenFile(doneFilePhysic + fileName);
                } else {
                    return ResponseEntity.ok("Không tìm thấy file");
                }
            }
            Long checkSum = file.getCRC32Checksum(jsonData.getBytes());
            return ResponseEntity.ok(checkSum.toString());
        }
        return ResponseEntity.ok("Hệ thống không đọc được cấu hình lưu file");
    }

    @PutMapping(value = "reset")
    public ResponseEntity<String> reset(String SYSNAME) {
        ResponseEntity<String> response = httpClient
                .putDataByApi(apiSystemBaseUrl + apisystemResetUrl + '/' + SYSNAME, null, null);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping(value = "/run")
    public ResponseEntity<String> start(String jobId, String functionName, String jobDescription, String jobGroup,
            String jobParam) throws SchedulerException, ClassNotFoundException {

        try {
            // lock task
            JobParameters jobParameters = new JobParametersBuilder().addString("PARAM", jobParam)
                    .addString("JOBID", jobId).addString("JOBGROUP", jobGroup)
                    .addString("JOBDESCRIPTION", jobDescription).addString("JOBNAME", functionName)
                    .addLong("TIME", System.currentTimeMillis()).toJobParameters();

            Job job = jobLocator.getJob(functionName);
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
            List<Throwable> failureExceptions = stepExecution.getFailureExceptions();
            if (failureExceptions.isEmpty()) {
                // loc success
                final String resultJob = stepExecution.getExecutionContext().getString("RESULT");
                final Object resultBody = stepExecution.getExecutionContext().get("CONTENT");
                LogSuccess(jobId, new EJson(jobParam), new EJson(resultJob),
                        resultBody != null ? (String) resultBody : null);
                return ResponseEntity.ok(resultJob);
            } else {
                Throwable throwable = failureExceptions.get(0);
                return ResponseEntity.ok(throwable.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    private void LogSuccess(String jobId, EJson param, EJson result, String file) {
        httpClient.addLog(jobId, param, result, null, null, file);
    }

}