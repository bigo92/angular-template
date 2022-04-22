package vnpt.net.syndata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vnpt.net.syndata.dao.BaseDao;
import vnpt.net.syndata.utils.EJson;
import vnpt.net.syndata.utils.Utils;

import javax.servlet.http.HttpSession;
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
        int errorCode = 0;
        if (action.equals(Utils.ACTION_ADD)){
            long jobId = jsonParam.getLong("settingId");
            String transactionId = jsonParam.getString("transactionId");
            String serverIP = jsonParam.getString("serverIP");
            int isLast = jsonParam.getLong("isLast").intValue();
            boolean isSingle = jsonParam.getBoolean("typeJob");

            if (isSingle){
                errorCode = baseDao.addProcessingSingle(jobId, transactionId, serverIP, isLast);
            }else {
                errorCode = baseDao.addProcessingMulti(jobId, transactionId, serverIP, isLast);
            }
        }

        if (action.equals(Utils.ACTION_DEL)){
            String transactionId = jsonParam.getString("transactionId");
            errorCode = baseDao.delScheduleProcessing(transactionId);
        }

        mapResult.put("errorCode", errorCode);
        return ResponseEntity.ok(mapResult);
    }
}
