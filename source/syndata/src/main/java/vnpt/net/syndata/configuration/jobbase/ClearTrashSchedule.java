package vnpt.net.syndata.configuration.jobbase;

import java.io.File;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.utils.EJson;

public class ClearTrashSchedule implements Tasklet, InitializingBean {

    @Autowired
    private HttpClientComponent httpClient;

    @Value("${api.system.base.url}")
    private String apiSystemBaseUrl;

    @Value("${api.schedule-setting.update.url}")
    private String apiScheduleSettingUpdateUrl;

    @Value("${quartz.log.delete-after-day}")
    private Integer logDeleteAfterDay;

    @Value("${quartz.log.delete-skip-log-has-file}")
    private Boolean logDeleteSkipLogHasFile;

    @Value("${quartz.log.delete-skip-file}")
    private Boolean logDeleteSkipFile;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
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

            // xoa log ko co file
            EJson dataJson = new EJson();
            dataJson.put("day", logDeleteAfterDay);
            dataJson.put("type", "NO-FILE");
            EJson headerParam = new EJson();
            headerParam.put("servicename", "SYS_CLEAR_TRASH_LOG");
            ResponseEntity<String> response = httpClient.deleteDataByApi(apiSystemBaseUrl + apiScheduleSettingUpdateUrl,
                    headerParam, null, dataJson.jsonString());
            if (response.getStatusCode() == HttpStatus.OK) {
                if (!logDeleteSkipLogHasFile) {
                    // xoa log co file
                    dataJson = new EJson();
                    dataJson.put("day", logDeleteAfterDay);
                    dataJson.put("type", "HAS-FILE");
                    headerParam = new EJson();
                    headerParam.put("servicename", "SYS_CLEAR_TRASH_LOG");
                    response = httpClient.deleteDataByApi(apiSystemBaseUrl + apiScheduleSettingUpdateUrl, headerParam,
                            null, dataJson.jsonString());

                    if (response.getStatusCode() == HttpStatus.OK) {
                        if (!logDeleteSkipFile) {
                            // xoa file vat ly
                            EJson jsonData = new EJson(response.getBody());
                            List<EJson> listItem = jsonData.getJSONArray("ITEMS");

                            for (EJson itemFile : listItem) {
                                String status = itemFile.getString("STATUS");
                                if (status.equals("SUCCESS")) {
                                    List<EJson> listFile = itemFile.getJSONArray("FILES");
                                    for (EJson f : listFile) {
                                        String fileName = f.getString("FILENAME");
                                        // check file co trong thu muc pendung ko
                                        File tempFile = new File(pendingFilePhysic + fileName);
                                        boolean exists = tempFile.exists();
                                        if (exists) {
                                            tempFile.delete();
                                        } else {
                                            tempFile = new File(doneFilePhysic + fileName);
                                            exists = tempFile.exists();
                                            if (exists) {
                                                tempFile.delete();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

}