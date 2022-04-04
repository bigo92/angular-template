package vnpt.net.syndata.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.utils.EJson;

@RestController
@RequestMapping("/api/schedule-setting")
public class ScheduleSetting {

    @Value("${api.system.base.url}")
    private String apiSystemBaseUrl;

    @Value("${api.schedule-setting.update.url}")
    private String apiScheduleSettingUpdateUrl;

    @Value("${api.schedule-setting.select.url}")
    private String apiScheduleSettingSelectUrl;

    @Autowired
    private HttpClientComponent httpClient;

    @GetMapping(value = "select")
    public ResponseEntity<String> select(String data) {
        ResponseEntity<String> response = httpClient.getDataByApi(apiSystemBaseUrl + apiScheduleSettingSelectUrl, data);
        return ResponseEntity.ok(response.getBody());
    }

    @PutMapping(value = "update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> update(@RequestBody String data) {
        EJson headerParam = new EJson();
        headerParam.put("servicename", "SYS_SCHEDULE_SETTING");
        ResponseEntity<String> response = httpClient.putDataByApi(apiSystemBaseUrl + apiScheduleSettingUpdateUrl,
                headerParam, data);
        return ResponseEntity.ok(response.getBody());
    }

    @DeleteMapping(value = "delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> delete(@RequestBody String data) {
        EJson headerParam = new EJson();
        headerParam.put("servicename", "SYS_SCHEDULE_SETTING");
        ResponseEntity<String> response = httpClient.deleteDataByApi(apiSystemBaseUrl + apiScheduleSettingUpdateUrl,
                headerParam, null, data);
        return ResponseEntity.ok(response.getBody());
    }

}