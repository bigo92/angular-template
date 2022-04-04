package vnpt.net.syndata.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vnpt.net.syndata.component.HttpClientComponent;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApi {
    
    @Value("${api.system.base.url}")
    private String apiSystemBaseUrl;

    @Value("${api.dasboard.server-online}")
    private String serverOnlineUrl;

    @Value("${api.dasboard.total-task}")
    private String totalTaskUrl;

    @Value("${api.dasboard.group-schedule}")
    private String groupScheduleUrl;

    @Value("${api.dasboard.group-schedule-detail}")
    private String groupScheduleDetailUrl;

    @Value("${api.dasboard.schedule-detail}")
    private String scheduleDetailUrl;

    @Autowired
    private HttpClientComponent httpClient;

    @GetMapping(value = "server-online")
    public ResponseEntity<String> serverOnline()
    {
        ResponseEntity<String> response = httpClient.getDataByApi(apiSystemBaseUrl + serverOnlineUrl, null);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping(value = "total-task")
    public ResponseEntity<String> totalTask(String data)
    {
        ResponseEntity<String> response = httpClient.getDataByApi(apiSystemBaseUrl + totalTaskUrl, data);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping(value = "group-schedule")
    public ResponseEntity<String> groupSchedule(String data)
    {
        ResponseEntity<String> response = httpClient.getDataByApi(apiSystemBaseUrl + groupScheduleUrl, data);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping(value = "group-schedule-detail")
    public ResponseEntity<String> groupScheduleDetail(String data)
    {
        ResponseEntity<String> response = httpClient.getDataByApi(apiSystemBaseUrl + groupScheduleDetailUrl, data);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping(value = "schedule-detail")
    public ResponseEntity<String> scheduleDetail(String data)
    {
        ResponseEntity<String> response = httpClient.getDataByApi(apiSystemBaseUrl + scheduleDetailUrl, data);
        return ResponseEntity.ok(response.getBody());
    }
}