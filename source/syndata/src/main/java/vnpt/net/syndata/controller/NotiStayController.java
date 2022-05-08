package vnpt.net.syndata.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.dao.NotiStayDao;
import vnpt.net.syndata.utils.EJson;
import vnpt.net.syndata.utils.Utils;

@Controller
@RequestMapping("/api/noti-stay")
public class NotiStayController {

    private HttpClientComponent http;
    private NotiStayDao notiStayDao;

    private String syncType = "SYNC_NOTI_STAY";
    private String authorization = "Basic ZGF0YWRpb2RlOmRhdGFkaW9kZTEyMw==";

    @Value("${api.get.noti.stay}")
    private String urlApi;

    public NotiStayController(
            HttpClientComponent http,
            NotiStayDao notiStayDao) {
        this.http = http;
        this.notiStayDao = notiStayDao;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody String test() throws Exception {
        return "ok";
    }

    // ******************* pull payload ***********************//

    @RequestMapping(value = "/get-noti-stay", method = RequestMethod.GET)
    public @ResponseBody String getNotiStay() throws Exception {

        Number page = -1;
        Boolean nextPage = true;

        // Lấy thời gian dữ liệu đồng bộ gần nhất
        LocalDateTime startDate = notiStayDao.getLastTime();
        if (startDate == null) {
            // nếu chưa đồng bộ lần nào thì set lấy dữ liệu phát sinh sau năm 2020
            startDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        }

        do {
            page = page.longValue() + 1;

            // Set tham số Header
            EJson header = new EJson();
            header.put("Authorization", authorization);

            // Set tham số đầu vào cho api lấy dữ liệu
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            EJson param = new EJson();
            param.put("syncType", syncType);
            param.put("date", startDate.format(formatter));
            param.put("page", page);
            param.put("size", "250");

            ResponseEntity<String> result = http.getApi(urlApi, param, header);
            EJson resultJson = new EJson(result.getBody());
            if (resultJson.getBoolean("success")) {
                List<EJson> resultData = resultJson.getJSONArray("data");
                if (resultData.size() == 0) {
                    // không có dữ liệu thì ngừng lấy dữ liệu
                    return "success";
                }

                // Còn dữ liệu thì đẩy vào bảng tạm và tăng page
                for (EJson item : resultData) {
                    // thêm dữ liệu
                    notiStayDao.addPull(item);
                }
            } else {
                // Có lỗi không lấy được dữ liệu
                return "false";
            }
        } while (nextPage);

        return "success";
    }

    // ******************* marge payload ***********************//

    @RequestMapping(value = "/merge-noti-stay", method = RequestMethod.GET)
    public @ResponseBody String mergeNotiStay() throws Exception {

        // lock dữ liệu muốn xử lý
        String processId = Utils.createUUID();

        notiStayDao.lockDataMerge(processId,200,60);

        List<Map<String, Object>> data = notiStayDao.getLockDataMerge(processId);
        for (Map<String, Object> item : data) {
            Map<String,Object> mergeCurent = notiStayDao.getDataMergeCurent((Number)item.get("ID"));
            try {
                
                if(mergeCurent == null){
                    // insert all
                    notiStayDao.addMerge((String)item.get("PAYLOAD"), (String)item.get("PAYLOAD"), null, 0);
                    notiStayDao.unLockDataMerge((String)item.get("GUID"),3,"Thành Công");
                    continue;
                }
                
                // merge data
                EJson dataMerge = Utils.mergeData((String)mergeCurent.get("PAYLOAD_MERGE"), 
                                                  (String)item.get("PAYLOAD"), 
                                                  (String) mergeCurent.get("PAYLOAD"));
                notiStayDao.updateResult((Number)item.get("ID"),dataMerge.jsonString(),(String)item.get("PAYLOAD"));
                continue;
            } catch (Exception e) {
                // update status 2 and message
                continue;
            }
        }
        
        return "success";
    }

}
