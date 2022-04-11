package vnpt.net.syndata.configuration.jobbase;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.component.ScheduleComponent;
import vnpt.net.syndata.dao.BaseDao;
import vnpt.net.syndata.utils.EJson;

import java.util.List;

public class InitializeSchedule implements Tasklet, InitializingBean {

    @Autowired
    private HttpClientComponent httpClient;

    @Autowired
    private ScheduleComponent schedule;

    @Autowired
    private BaseDao baseDao;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Value("${spring.batch.console-log}")
    private Boolean consoleLog;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        if (consoleLog) {
            System.out.println("start InitializeSchedule: LOADING");
        }

//        // Gọi lên server thông báo là hệ thống vẫn đang online
//        ResponseEntity<String> resultOnline = httpClient.online();
//        if (resultOnline.getStatusCode() == HttpStatus.OK) {
//            if (consoleLog) {
//                System.out.println("connect online server: SUCCESS");
//            }
//        }
//        resultOnline = null;

        // load congfig api
        EJson paramRequest = new EJson();
        paramRequest.put("PAGE", 1);
        paramRequest.put("SIZE", 10);

        boolean isLoadDone = true;
        do {
            try {
                ResponseEntity<String> result;
                /*DAO*/
//                List custome = baseDao.testfunction("");
//                ResponseEntity<String> result = httpClient.getSetting(paramRequest.jsonString());
//                if (result.getStatusCode() == HttpStatus.OK) {
//                    if (!result.getBody().endsWith("\"ITEMS\":[]}")) {
//                        EJson data = new EJson(result.getBody());
//                        List<EJson> listItem = data.getJSONArray("ITEMS");
//                        for (EJson item : listItem) {
//                            String jobId = item.getLong("SCHEDULE_SETTING_ID").toString();
//                            String jobGroup = item.getString("SCHEDULE_SETTING_GROUP");
//                            String jobDescription = item.getString("SCHEDULE_SETTING_NAME");
//                            String cronSchedule = item.getString("SCHEDULE_SETTING_CRON");
//                            Long loop = item.getLong("SCHEDULE_SETTING_LOOP");
//                            Long turn = item.getLong("SCHEDULE_SETTING_TURN");
//
//                            BigDecimal status = item.getBigDecimal("STATUS");
//                            // chi lay tak trang thai start và lặp vô tận, hoặc còn turn thực hiện
//                            if (status.longValue() == 1 && (loop.longValue() == 1 || (loop.longValue() == 0 && turn.longValue() > 0))) { // running
//                                schedule.addSchedule(jobId, jobGroup, jobDescription, cronSchedule);
//                            } else { // stop
//                                schedule.removeSchedule(jobId, jobGroup);
//                            }
//                        }
//                    } else {
//                        isLoadDone = true;
//                    }
//                }
                String jobId = "1";
                String jobGroup = "Job_Test";
                String jobDescription = "exampleApi";
                String cronSchedule = "0/3 * * * * ?";
                String param = "{\"API\":\"http://localhost:8080/public/example-api-one\",\"METHOD\":\"POST\",\"PARAMS\":{\"KEY_OBJ1\":1,\"KEY_OBJ2\":2},\"HTTPHEADERS\":{\"Content-Type\":\"application/json\",\"Cookie\":\"JSESSIONID=00D11F1F8CFC83B7A339B4498AF43DC4\"}}";
                // chi lay tak trang thai start và lặp vô tận, hoặc còn turn thực hiện
                schedule.addSchedule(jobId, jobGroup, jobDescription, cronSchedule, param);
                paramRequest.put("PAGE", paramRequest.getLong("PAGE") + 1);
                result = null;
            } catch (Exception e) {
                if (consoleLog) {
                    System.out.println("gọi api get setting bị lỗi");
                }
                isLoadDone = true;
            }
        } while (!isLoadDone);

        if (consoleLog) {
            System.out.println("start InitializeSchedule: DONE");
        }
        return RepeatStatus.FINISHED;
    }
}