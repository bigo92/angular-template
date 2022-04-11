package vnpt.net.syndata.component;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vnpt.net.syndata.utils.EJson;

import java.util.Map;

@Component
public class ScheduleCallApiComponent {
    @Autowired
    private HttpClientComponent httpClient;

    private String param;
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//        final Map<String, Object> prameter = chunkContext.getStepContext().getJobParameters();
//        String param = "{\"API\":\"http://localhost:8080/public/example-api-one\",\"METHOD\":\"POST\",\"PARAMS\":{\"KEY_OBJ1\":1,\"KEY_OBJ2\":2},\"HTTPHEADERS\":{\"Content-Type\":\"application/json\",\"Cookie\":\"JSESSIONID=00D11F1F8CFC83B7A339B4498AF43DC4\"}}";

        try {
            // lấy tham số
            final EJson paramJson = new EJson(param);
            final String urlApi = paramJson.getString("API");
            final String method = paramJson.getString("METHOD");
            final EJson apiParam = paramJson.getJSONObject("PARAMS");;
            final EJson headerParam = paramJson.getJSONObject("HTTPHEADERS");

            // excute api
            switch (method) {
                case "GET":
                    ResponseEntity<String> resultJsonIn = httpClient.getDataByApi(urlApi, apiParam.jsonString());
                    if (resultJsonIn.getStatusCode() == HttpStatus.OK) {
                        // return data
                        EJson resultLog = new EJson();
                        resultLog.put("STATUS", "SUCCESS");
                    }
                    break;
                case "POST":
                    resultJsonIn = httpClient.postDataByApi(urlApi, headerParam, apiParam.jsonString());
                    if (resultJsonIn.getStatusCode() == HttpStatus.OK) {
                        // return data
                        EJson resultLog = new EJson();
                        resultLog.put("STATUS", "SUCCESS");
                    }
                    break;
                case "PUT":
                    resultJsonIn = httpClient.putDataByApi(urlApi, headerParam, apiParam.jsonString());
                    if (resultJsonIn.getStatusCode() == HttpStatus.OK) {
                        // return data
                        EJson resultLog = new EJson();
                        resultLog.put("STATUS", "SUCCESS");
                    }
                    break;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
        return RepeatStatus.FINISHED;
    }
}
