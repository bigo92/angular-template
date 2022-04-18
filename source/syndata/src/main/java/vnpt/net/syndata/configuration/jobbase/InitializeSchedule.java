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
import vnpt.net.syndata.utils.ResponseBodyJson;
import vnpt.net.syndata.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                List<Map<String, Object>> rows = baseDao.getScheduleSetting();
                for (Map<String, Object> item : rows) {
                    String jobId = item.getOrDefault("SETTING_ID", "").toString();
                    String jobGroup = item.getOrDefault("JOB_GROUP", "").toString();
                    String jobDescription = item.getOrDefault("NAME_JOB", "").toString();
                    String cronSchedule = item.getOrDefault("TIME_CROSS", "").toString();
                    String paramJob = item.getOrDefault("JSON_PARAM", "").toString();
                    boolean isSingle = Utils.getTypeRun(Integer.parseInt(item.getOrDefault("TYPE_RUN", "0").toString()));
                    schedule.addSchedule(jobId, jobGroup, jobDescription, cronSchedule, isSingle);
                }
                isLoadDone = true;
                paramRequest.put("PAGE", paramRequest.getLong("PAGE") + 1);
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