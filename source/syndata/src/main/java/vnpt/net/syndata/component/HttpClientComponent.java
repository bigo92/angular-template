package vnpt.net.syndata.component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.commons.text.StringEscapeUtils;

import vnpt.net.syndata.utils.EJson;

@Component
public class HttpClientComponent {

    @Value("${api.system.base.url}")
    private String apiSystemBaseUrl;
    @Value("${api.schedule-setting.select.url}")
    private String urlScheduleSettingGet;
    @Value("${api.schedule-setting.update.url}")
    private String urlScheduleSettingUpdate;
    @Value("${api.schedule-log.insert.url}")
    private String urlScheduleLogInsert;
    @Value("${api.sys-param.select.url}")
    private String urlSysPara;

    public ResponseEntity<String> getApi(String url, EJson param, EJson header) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        if(header != null){
            Iterator<String> keys = header.jsonObject().keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                headers.add(key, header.getString(key));
            }
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (param != null) {
            Iterator<String> keys = param.jsonObject().keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                builder.queryParam(key, param.getString(key));
            }
        }
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity,
                String.class);
        return result;
    }

    public ResponseEntity<String> getDataByApi(String url, String data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (data != null) {
            builder.queryParam("data", data);
        }
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity,
                String.class);
        return result;
    }

    public ResponseEntity<String> getDataByApiQueryParam(String url, Map<String, Object> data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet() ) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        return result;
    }

    public ResponseEntity<String> putDataByApi(String url, EJson headerParam, String data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (headerParam != null) {
            Iterator<String> keys = headerParam.jsonObject().keySet().iterator();
            // headers.add("session", UUID.randomUUID().toString());
            while (keys.hasNext()) {
                String key = keys.next();
                headers.add(key, headerParam.getString(key));
            }
        }

        EJson dataParam = new EJson();
        dataParam.put("data", data);
        HttpEntity<String> entity = new HttpEntity<String>(dataParam.jsonString(), headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        return result;
    }

    public ResponseEntity<String> postDataByApi(String url, EJson headerParam, String data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (headerParam != null) {
            Iterator<String> keys = headerParam.jsonObject().keySet().iterator();
            // headers.add("session", UUID.randomUUID().toString());
            while (keys.hasNext()) {
                String key = keys.next();
                headers.add(key, headerParam.getString(key));
            }
        }

        HttpEntity<String> entity = new HttpEntity<String>(data, headers);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return result;
    }

    public ResponseEntity<String> deleteDataByApi(String url, EJson headerParam, String param, String body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (headerParam != null) {
            Iterator<String> keys = headerParam.jsonObject().keySet().iterator();
            // headers.add("session", UUID.randomUUID().toString());
            while (keys.hasNext()) {
                String key = keys.next();
                headers.add(key, headerParam.getString(key));
            }
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (param!= null) {
            builder.queryParam("data", param);
        }

        EJson dataParam = new EJson();
        dataParam.put("data", body);
        HttpEntity<String> entity = new HttpEntity<String>(dataParam.jsonString(), headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.DELETE, entity, String.class);
        return result;
    }

    public ResponseEntity<String> getSetting(String data) {
        return getDataByApi(apiSystemBaseUrl + urlScheduleSettingGet, data);
    }

    public ResponseEntity<String> getSysPara() {
        return getDataByApi(apiSystemBaseUrl + urlSysPara, null);
    }

    /*
     * Hàm khóa task id hệ thống sử lý task này
     */
    public ResponseEntity<String> lockTask(String jobId) {
        // get api lock task
        String serverId = getIp();
        EJson dataJson = new EJson();
        dataJson.put("SCHEDULE_SETTING_ID", jobId);
        dataJson.put("IP_LOCK", serverId);
        EJson headerParam = new EJson();
        headerParam.put("servicename", "SYS_SCHEDULE_SETTING_LOCK_TASK");
        return putDataByApi(apiSystemBaseUrl + urlScheduleSettingUpdate, headerParam, dataJson.jsonString());
    }

    /*
     * Hàm mở khóa task id khi hệ thống sử lý xong task này
     */
    public boolean unLockTask(String jobId) {
        // get api lock task
        String serverId = getIp();

        EJson dataJson = new EJson();
        dataJson.put("SCHEDULE_SETTING_ID", jobId);
        dataJson.put("IP_LOCK", serverId);
        EJson headerParam = new EJson();
        headerParam.put("servicename", "SYS_SCHEDULE_SETTING_UNLOCK_TASK");
        ResponseEntity<String> result = putDataByApi(apiSystemBaseUrl + urlScheduleSettingUpdate, headerParam,
                dataJson.jsonString());
        if (result.getStatusCode() == HttpStatus.OK) {
            return true;
        }
        return false;
    }

    public ResponseEntity<String> online() {
        // get api connent online
        String serverId = getIp();
        EJson dataJson = new EJson();
        dataJson.put("IP_LOCK", serverId);
        EJson headerParam = new EJson();
        headerParam.put("servicename", "SYS_SCHEDULE_SETTING_ONLINE_TASK");
        return putDataByApi(apiSystemBaseUrl + urlScheduleSettingUpdate, headerParam, dataJson.jsonString());
    }

    public ResponseEntity<String> addLog(String jobId, EJson param, EJson result, String message, String trace,
            String file) {
        // get api connent online
        String serverId = getIp();
        EJson datJson = new EJson();
        datJson.put("SCHEDULE_SETTING_ID", jobId);
        datJson.put("SCHEDULE_LOG_PARAM", param.jsonObject());
        datJson.put("SCHEDULE_LOG_RETURN", result.jsonObject());
        datJson.put("SCHEDULE_LOG_MESSAGE", StringEscapeUtils.escapeHtml4(message));
        datJson.put("SCHEDULE_LOG_TRACE", StringEscapeUtils.escapeHtml4(trace));
        datJson.put("SCHEDULE_LOG_FILE", file);
        datJson.put("IP_SERVER", serverId);
        EJson headerParam = new EJson();
        headerParam.put("servicename", "SYS_SCHEDULE_LOG");
        return postDataByApi(apiSystemBaseUrl + urlScheduleLogInsert, headerParam, datJson.jsonString());
    }

    public ResponseEntity<String> addLogRetry(Long logId, String jobId, EJson param, EJson result, String message,
            String trace, String file) {
        // get api connent online
        String serverId = getIp();
        long epochTime = (new Date()).getTime() / 1000;
        EJson datJson = new EJson();
        datJson.put("SCHEDULE_LOG_ID", logId);
        datJson.put("SCHEDULE_SETTING_ID", jobId);
        datJson.put("SCHEDULE_LOG_RETURN", result.jsonObject());
        datJson.put("SCHEDULE_LOG_MESSAGE", StringEscapeUtils.escapeHtml4(message));
        datJson.put("SCHEDULE_LOG_TRACE", StringEscapeUtils.escapeHtml4(trace));
        datJson.put("SCHEDULE_LOG_FILE", file);
        datJson.put("SCHEDULE_LOG_PARAM", param != null ? param.jsonObject() : null);
        datJson.put("IP_SERVER", serverId);
        datJson.put("CREATE_TIME", epochTime);
        EJson headerParam = new EJson();
        headerParam.put("servicename", "SYS_SCHEDULE_LOG");
        return postDataByApi(apiSystemBaseUrl + urlScheduleLogInsert, headerParam, datJson.jsonString());
    }

    /*
     * Hàm khóa task retry khi hệ thống sử lý muốn thực hiện lại 1 task bị lỗi
     */
    public ResponseEntity<String> lockTaskRetry() {
        String serverId = getIp();
        EJson dataJson = new EJson();
        dataJson.put("IP_LOCK", serverId);
        EJson headerParam = new EJson();
        headerParam.put("servicename", "SYS_SCHEDULE_SETTING_LOCK_TASK_RETRY");
        return putDataByApi(apiSystemBaseUrl + urlScheduleSettingUpdate, headerParam, dataJson.jsonString());
    }

    /*
     * Hàm mở khóa task retry khi một task sử lý bị lỗi
     */
    public ResponseEntity<String> unLockTaskRetry(Long logId) {
        String serverId = getIp();
        EJson dataJson = new EJson();
        dataJson.put("SCHEDULE_LOG_ID", logId);
        dataJson.put("IP_LOCK", serverId);
        EJson headerParam = new EJson();
        headerParam.put("servicename", "SYS_SCHEDULE_SETTING_UNLOCK_TASK_RETRY");
        return putDataByApi(apiSystemBaseUrl + urlScheduleSettingUpdate, headerParam, dataJson.jsonString());
    }

    private String getIp() {
        String serverId;
        try {
            serverId = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            serverId = "127.0.0.1";
        }
        return serverId;
    }
}