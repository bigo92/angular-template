package vnpt.net.syndata.job;

import java.math.BigDecimal;
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

public class SynDataJob implements Tasklet, InitializingBean {

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

            final EJson getData = paramJson.getJSONObject("GETDATA");
            final String getData_Api = getData.getString("API");
            final BigDecimal getData_Size = getData.getBigDecimal("SIZE");

            final EJson synData = paramJson.getJSONObject("SYNDATA");
            final String synData_Api = synData.getString("API");
            final String synData_method = synData.getString("METHOD");
            final EJson synData_Header = synData.getJSONObject("HTTPHEADERS");

            final EJson verifyData = paramJson.getJSONObject("VERIFYDATA");


            // lay du lieu dong bo tu api
            EJson paramRequest = new EJson();
            paramRequest.put("PAGE", 1);
            paramRequest.put("SIZE", getData_Size);

            // excute api đầu vào
            ResponseEntity<String> resultGetData = httpClient.getDataByApi(getData_Api, paramRequest.jsonString());
            if (resultGetData.getStatusCode() == HttpStatus.OK) {

                // excute api đầu ra
                ResponseEntity<String> resultSynData = null;
                switch (synData_method) {
                    case "POST":
                        resultSynData = httpClient.postDataByApi(synData_Api, synData_Header, resultGetData.getBody());
                        break;
                    case "PUT":
                        resultSynData = httpClient.putDataByApi(synData_Api, synData_Header, resultGetData.getBody());
                        break;
                }
                if (resultSynData.getStatusCode() == HttpStatus.OK) {
                    // exute api veryfile
                    if(verifyData != null)
                    {
                        final String verifyData_Api = verifyData.getString("API");
                        final String verifyData_method = verifyData.getString("METHOD");
                        final EJson verifyData_Header = verifyData.getJSONObject("HTTPHEADERS");

                        ResponseEntity<String> resultVerifyData = null;
                        switch (verifyData_method) {
                            case "POST":
                                resultVerifyData = httpClient.postDataByApi(verifyData_Api, verifyData_Header,
                                        resultSynData.getBody());
                                break;
                            case "PUT":
                                resultVerifyData = httpClient.putDataByApi(verifyData_Api, verifyData_Header,
                                        resultSynData.getBody());
                                break;
                        }
                        if (resultVerifyData.getStatusCode() == HttpStatus.OK) {
                            EJson resultLog = new EJson();
                            resultLog.put("STATUS", "SUCCESS");
                            contribution.getStepExecution().getExecutionContext().put("RESULT", resultLog.jsonString());
                            contribution.getStepExecution().getExecutionContext().put("CONTENT",
                                    resultVerifyData.getBody());
                        }
                    }else{
                        EJson resultLog = new EJson();
                            resultLog.put("STATUS", "SUCCESS");
                            contribution.getStepExecution().getExecutionContext().put("RESULT", resultLog.jsonString());
                            contribution.getStepExecution().getExecutionContext().put("CONTENT",null);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
        return RepeatStatus.FINISHED;
    }

}