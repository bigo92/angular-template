package vnpt.net.syndata.job;

import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.utils.EJson;

public class ExecuteApiJob implements Tasklet, InitializingBean {

    @Autowired
    private HttpClientComponent httpClient;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final Map<String, Object> prameter = chunkContext.getStepContext().getJobParameters();
        try {
            // lấy tham số
            final EJson paramJson = new EJson(prameter.get("PARAM").toString());
            final String urlApi = paramJson.getString("API");
            final String method = paramJson.getString("METHOD");
            final EJson apiParam = new EJson(paramJson.getString("PARAMS"));
            final EJson headerParam = new EJson(paramJson.getString("HTTPHEADERS"));

            // excute api
            switch (method) {
                case "GET":
                    ResponseEntity<String> resultJsonIn = httpClient.getDataByApi(urlApi, apiParam.jsonString());
                    if (resultJsonIn.getStatusCode() == HttpStatus.OK) {
                        // return data
                        EJson resultLog = new EJson();
                        resultLog.put("STATUS", "SUCCESS");
                        contribution.getStepExecution().getExecutionContext().put("RESULT", resultLog.jsonString());
                        contribution.getStepExecution().getExecutionContext().put("CONTENT", resultJsonIn.getBody());
                    }
                    break;
                case "POST":
                    resultJsonIn = httpClient.postDataByApi(urlApi, headerParam, apiParam.jsonString());
                    if (resultJsonIn.getStatusCode() == HttpStatus.OK) {
                        // return data
                        EJson resultLog = new EJson();
                        resultLog.put("STATUS", "SUCCESS");
                        contribution.getStepExecution().getExecutionContext().put("RESULT", resultLog.jsonString());
                        contribution.getStepExecution().getExecutionContext().put("CONTENT", resultJsonIn.getBody());
                    }
                    break;
                case "PUT":
                    resultJsonIn = httpClient.putDataByApi(urlApi, headerParam, apiParam.jsonString());
                    if (resultJsonIn.getStatusCode() == HttpStatus.OK) {
                        // return data
                        EJson resultLog = new EJson();
                        resultLog.put("STATUS", "SUCCESS");
                        contribution.getStepExecution().getExecutionContext().put("RESULT", resultLog.jsonString());
                        contribution.getStepExecution().getExecutionContext().put("CONTENT", resultJsonIn.getBody());
                    }
                    break;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
        return RepeatStatus.FINISHED;
    }

}