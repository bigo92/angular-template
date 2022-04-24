package vnpt.net.syndata.controller;

import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vnpt.net.syndata.dao.BaseDao;
import vnpt.net.syndata.utils.EJson;
import vnpt.net.syndata.utils.Utils;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/public")
public class ActionDoThingJob {
    @Autowired
    BaseDao baseDao;

    @PostMapping(value = "/dothing-job")
    public @ResponseBody
    ResponseEntity<?> actionDothingJob(@RequestBody String jsonBody){
        EJson jsonParam = new EJson(jsonBody);
        String action = jsonParam.getString("action");
        Map<String, Object> mapResult = new HashMap<>();
        int errorCode = -1;
        if (action.equals(Utils.ACTION_ADD)){
            long jobId = jsonParam.getLong("settingId");
            String serverIP = jsonParam.getString("serverIP");
            int isLast = jsonParam.getLong("isLast").intValue();
            boolean isSingle = jsonParam.getBoolean("typeJob");
            String transactionId = Utils.createUUID();

            if (isSingle){
                errorCode = baseDao.addProcessingSingle(jobId, transactionId, serverIP, isLast);
                mapResult.put("transactionId", transactionId);
            }else {
                errorCode = baseDao.addProcessingMulti(jobId, transactionId, serverIP, isLast);
                mapResult.put("transactionId", transactionId);
            }
        }

        if (action.equals(Utils.ACTION_DEL)){
            String transactionId = jsonParam.getString("transactionId");
            errorCode = baseDao.delScheduleProcessing(transactionId);
            mapResult.put("transactionId", transactionId);
        }

        mapResult.put("errorCode", errorCode);
        return ResponseEntity.ok(mapResult);
    }

    @GetMapping(value = "get-schedule-process")
    public @ResponseBody
    ResponseEntity<?> getScheduleProcessing(@RequestParam(value = "transactionId") String transactionId) throws Exception {
        Map<String, Object> mapProcess = baseDao.getScheduleProcessing(transactionId);
        if (mapProcess.isEmpty()){
            mapProcess.put("isEmpty", true);
        }else {
            mapProcess.put("isEmpty", false);
        }
//        mapResult.put("mapProcess", mapProcess.toString());
        return ResponseEntity.ok(mapProcess);
    }
}
