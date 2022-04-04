package vnpt.net.syndata.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import vnpt.net.syndata.component.CreateFileComponent;
import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.utils.EJson;

@Controller
public class HomeController {

    @Autowired
    private HttpClientComponent httpClient;

    @Autowired
    private CreateFileComponent file;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "home";
    }

    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public String logDetail() {
        return "log";
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public String service() {
        return "schedule";
    }

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public String setting() {
        return "setting";
    }

    @RequestMapping(value = "/reset-syn", method = RequestMethod.GET)
    public String resetSyn() {
        return "reset-syn";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                // remove cookie
                cookie.setMaxAge(0);
                cookie.setValue(null);
                response.addCookie(cookie);
            }
        }

        return "login";
    }

    @RequestMapping(value = "/public/add-token", method = RequestMethod.GET)
    public RedirectView addToken(String token, @RequestParam(required = false) String urlback,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Cookie cookie = new Cookie("Authorization", URLDecoder.decode(token, "UTF-8"));
        String pathDomain = request.getContextPath();
        if (pathDomain.equals("")) {
            pathDomain = "/";
        }
        if (urlback.isEmpty()) {
            urlback = pathDomain;
        }

        // expires in 30 days
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath(pathDomain);
        // add cookie to response
        response.addCookie(cookie);

        return new RedirectView(urlback);
    }

    @RequestMapping(value = "/open-file", method = RequestMethod.GET)
    @ResponseBody
    public String openFile(String fileName) throws Exception {
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

            // check file co trong thu muc pendung ko
            File tempFile = new File(pendingFilePhysic + fileName);
            boolean exists = tempFile.exists();
            if (exists) {
                return file.OpenFile(pendingFilePhysic + fileName);
            } else {
                tempFile = new File(doneFilePhysic + fileName);
                exists = tempFile.exists();
                if (exists) {
                    return file.OpenFile(doneFilePhysic + fileName);
                } else {
                    return "Không tìm thấy file";
                }
            }
        }
        return "Hệ thống không đọc được cấu hình lưu file";
    }
}