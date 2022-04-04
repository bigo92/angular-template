package vnpt.net.syndata.api;

import java.io.File;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.utils.EJson;

@RestController
@RequestMapping("/api/schedule-log")
public class ScheduleLogApi {

    @Value("${api.system.base.url}")
    private String apiSystemBaseUrl;

    @Value("${api.schedule-log.find.url}")
    private String apiScheduleLogFindOneUrl;

    @Value("${api.schedule-log.select.url}")
    private String apiScheduleLogSelectUrl;

    @Value("${api.schedule-setting.update.url}")
    private String apiScheduleSettingUpdateUrl;

    @Autowired
    private HttpClientComponent httpClient;

    @GetMapping(value = "find-one")
    public ResponseEntity<String> serverOnline(String data) {
        ResponseEntity<String> response = httpClient.getDataByApi(apiSystemBaseUrl + apiScheduleLogFindOneUrl, data);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping(value = "select")
    public ResponseEntity<String> select(String data) {
        ResponseEntity<String> response = httpClient.getDataByApi(apiSystemBaseUrl + apiScheduleLogSelectUrl, data);
        return ResponseEntity.ok(response.getBody());
    }

    @DeleteMapping(value = "delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> delete(@RequestBody String data) {

        // lay cau hinh thu muc luu file
        ResponseEntity<String> paraResult = httpClient.getSysPara();
        if (paraResult.getStatusCode() == HttpStatus.OK) {
            // doc config
            final EJson paraJson = new EJson(paraResult.getBody());
            String pendingFilePhysic = "";
            String doneFilePhysic = "";
            final List<EJson> paraItem = paraJson.getJSONArray("ITEMS");

            for (final EJson eJson : paraItem) {

                //if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.INTERNAL.EREA.FILE.SAVING.FOLDER")) {
                if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.READ.EREA.FILE.FOLDER")) {
                    pendingFilePhysic = eJson.getString("PARA_VALUE");
                }

                //if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.DONE.EREA.FILE.SAVING.FOLDER")) {
                if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.DONE.EREA.FILE.FOLDER")) {
                    doneFilePhysic = eJson.getString("PARA_VALUE");
                }
                
            }

            // xoa data file
            EJson param = new EJson(data);
            for (int i = 0; i < param.jsonArray().size(); i++) {
                JsonObject item = param.jsonArray().get(i).getAsJsonObject();
                if (item.has("SCHEDULE_LOG_FILE") && !item.get("SCHEDULE_LOG_FILE").isJsonNull()) {
                    JsonArray fileData = item.getAsJsonArray("SCHEDULE_LOG_FILE");
                    for (int j = 0; j < fileData.size(); j++) {
                        JsonObject fileJson = fileData.get(j).getAsJsonObject();
                        String fileName = fileJson.get("FILENAME").getAsString();

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

        //xoa data qua api
        EJson headerParam = new EJson();
        headerParam.put("servicename", "SYS_SCHEDULE_SETTING_LOG");
        ResponseEntity<String> response = httpClient.deleteDataByApi(apiSystemBaseUrl
        + apiScheduleSettingUpdateUrl,
        headerParam, null, data);
        return ResponseEntity.ok(response.getBody());
    }
}