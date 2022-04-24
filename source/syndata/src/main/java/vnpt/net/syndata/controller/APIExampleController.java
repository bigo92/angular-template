package vnpt.net.syndata.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vnpt.net.syndata.dao.BaseDao;
import vnpt.net.syndata.utils.EJson;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/public")
public class APIExampleController {
    @Autowired
    BaseDao baseDao;

    @RequestMapping(value = "/example-api-one", method = RequestMethod.POST)
    public @ResponseBody
    String printCurrentTime(@RequestBody String jsonParam,
                            HttpSession session) throws Exception {
        JsonParser jsonParser = new JsonParser();
        JsonObject paramObj = new JsonObject();
        paramObj = jsonParser.parse(jsonParam).getAsJsonObject();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return dtf.format(now);
    }
}
