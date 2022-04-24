package vnpt.net.syndata.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.dao.CSLTDao;
import vnpt.net.syndata.utils.EJson;
import vnpt.net.syndata.utils.Utils;

@Controller
@RequestMapping("/api/cslt")
public class CSLTController {

    @Autowired
    HttpClientComponent http;

    @Autowired
    CSLTDao csltDao;

    String tableName = "DATA_DIODE_SYNC_ACCOM_FACILITY";

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody String test() throws Exception {
        return "abc";
    }

    @RequestMapping(value = "/get-data", method = RequestMethod.GET)
    public @ResponseBody String getData() throws Exception {
        EJson header = new EJson();
        header.put("Authorization", "Basic ZGF0YWRpb2RlOmRhdGFkaW9kZTEyMw==");

        Number page = 0;
        LocalDateTime startDate = csltDao.getTime(tableName);
        if (startDate == null) {
            startDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        }

        EJson param = new EJson();
        param.put("syncType", "SYNC_ACCOM_FACILITY");
        param.put("date", "2022-04-05T11:11:11");
        param.put("page", page);
        param.put("size", "250");
        ResponseEntity<String> result = http.getApi("http://10.100.131.226:8009/api/sync", param, header);
        EJson resultJson = new EJson(result.getBody());
        if (resultJson.getBoolean("success")) {
            List<EJson> resultData = resultJson.getJSONArray("data");
            for (EJson item : resultData) {
                LocalDateTime updateDate = item.getDate("createDate") != null 
                        ? Utils.convertToLocalDate((Date)item.getDate("createDate"))
                        : Utils.convertToLocalDate((Date)item.getDate("updateDate"));
                if(updateDate.toEpochSecond(ZoneOffset.UTC) > startDate.toEpochSecond(ZoneOffset.UTC)){
                    startDate = updateDate;
                }
                csltDao.insertCslt(item);
            }
        }
        csltDao.insertTime(tableName, startDate);

        return "ok";
    }

    @RequestMapping(value = "/merge-data", method = RequestMethod.GET)
    public @ResponseBody String mergeData() throws Exception {
        EJson header = new EJson();
        header.put("Authorization", "Basic ZGF0YWRpb2RlOmRhdGFkaW9kZTEyMw==");

        EJson param = new EJson();
        param.put("syncType", "SYNC_ACCOM_FACILITY");
        param.put("date", "2022-04-05T11:11:11");
        param.put("page", "1");
        param.put("size", "250");
        ResponseEntity<String> result = http.getApi("http://10.100.131.226:8009/api/sync", param, header);
        EJson resultJson = new EJson(result.getBody());
        if (resultJson.getBoolean("success")) {
            List<EJson> resultData = resultJson.getJSONArray("data");
            for (EJson item : resultData) {
                csltDao.insertCslt(item);
            }
        }
        return "ok";
    }

    @RequestMapping(value = "/push-data", method = RequestMethod.GET)
    public @ResponseBody String pushData() throws Exception {
        EJson header = new EJson();
        header.put("Authorization", "Basic ZGF0YWRpb2RlOmRhdGFkaW9kZTEyMw==");

        EJson param = new EJson();
        param.put("syncType", "SYNC_ACCOM_FACILITY");
        param.put("date", "2022-04-05T11:11:11");
        param.put("page", "1");
        param.put("size", "250");
        ResponseEntity<String> result = http.getApi("http://10.100.131.226:8009/api/sync", param, header);
        EJson resultJson = new EJson(result.getBody());
        if (resultJson.getBoolean("success")) {
            List<EJson> resultData = resultJson.getJSONArray("data");
            for (EJson item : resultData) {
                csltDao.insertCslt(item);
            }
        }
        return "ok";
    }

    @RequestMapping(value = "/push-data-ref", method = RequestMethod.GET)
    public @ResponseBody String pushDataRef() throws Exception {
        EJson header = new EJson();
        header.put("Authorization", "Basic ZGF0YWRpb2RlOmRhdGFkaW9kZTEyMw==");

        EJson param = new EJson();
        param.put("syncType", "SYNC_ACCOM_FACILITY");
        param.put("date", "2022-04-05T11:11:11");
        param.put("page", "1");
        param.put("size", "250");
        ResponseEntity<String> result = http.getApi("http://10.100.131.226:8009/api/sync", param, header);
        EJson resultJson = new EJson(result.getBody());
        if (resultJson.getBoolean("success")) {
            List<EJson> resultData = resultJson.getJSONArray("data");
            for (EJson item : resultData) {
                csltDao.insertCslt(item);
            }
        }
        return "ok";
    }

}
