package vnpt.net.syndata.job;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonArray;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import vnpt.net.syndata.component.CreateFileComponent;
import vnpt.net.syndata.component.HttpClientComponent;
import vnpt.net.syndata.utils.EJson;

/*
* Job đọc file và gọi api đồng bộ sau đó tạo file dựa vào kết quả trả về
*/
public class ReceiveFileJob implements Tasklet, InitializingBean {

    @Autowired
    private CreateFileComponent file;

    @Autowired
    private HttpClientComponent httpClient;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        final Map<String, Object> prameter = chunkContext.getStepContext().getJobParameters();
        try {
            // lấy tham số
            final EJson paramJson = new EJson(prameter.get("PARAM").toString());
            String inFile = paramJson.getString("INFILE");
            String outFile = paramJson.getString("OUTFILE");
            //String saveDoneFile = paramJson.getString("SAVEDONEFILE");

            final String urlApi = paramJson.getString("API");
            final EJson headerParam = paramJson.getJSONObject("HTTPHEADERS");
            // lay du lieu tu api
            final ResponseEntity<String> paraResult = httpClient.getSysPara();
            if (paraResult.getStatusCode() == HttpStatus.OK) {
                JsonArray listFileResult = new JsonArray();
                // doc config
                final EJson paraJson = new EJson(paraResult.getBody());
                String pendingFilePhysic = "";
                String doneFilePhysic = "";
                String outFilePhysic = "";

                final List<EJson> paraItem = paraJson.getJSONArray("ITEMS");

                for (final EJson eJson : paraItem) {
                    //if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.INTERNAL.EREA.FILE.SAVING.FOLDER")) {
                    if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.READ.EREA.FILE.FOLDER")) {
                        pendingFilePhysic = eJson.getString("PARA_VALUE");
                    }

                    //if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.INTERNAL.EREA.FILE.SAVING.FOLDER")) {
					if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.SAVE.EREA.FILE.FOLDER")) {
						outFilePhysic = eJson.getString("PARA_VALUE");
					}

                    //if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.DONE.EREA.FILE.SAVING.FOLDER")) {
                    if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.DONE.EREA.FILE.FOLDER")) {
                        doneFilePhysic = eJson.getString("PARA_VALUE");
                    }
                }

                //String fileFilter = file.keyMathFile(inFile);
                String extentionFilter = file.getExtention(inFile);
                // get list file in folder
                try (Stream<Path> walk = Files.walk(Paths.get(pendingFilePhysic))) {

                    // Sap xep file theo id va page
                    final List<Map<String, String>> result = walk.map(x -> {
                        Map<String, String> item = new HashMap<String, String>();
                        item.put("path", x.toString());
                        item.put("fileName", x.getFileName().toString());
                        return item;
                    }).filter(f -> matchNameFile(f.get("fileName"), inFile)
                            && f.get("fileName").endsWith(extentionFilter)).collect(Collectors.toList());
                    //   .sorted((f1, f2) -> {
                    //             String fileName1 = f1.get("fileName");
                    //             String fileName2 = f2.get("fileName");
                    //             Long fileId1 = Long.valueOf(file.getValueByFormat("${ID}", fileName1, inFile));
                    //             Long fileId2 = Long.valueOf(file.getValueByFormat("${ID}", fileName2, inFile));

                    //             //Long filePage1 = Long.valueOf(file.getValueByFormat("${PAGE}", fileName1, inFile));
                    //             //Long filePage2 = Long.valueOf(file.getValueByFormat("${PAGE}", fileName2, inFile));

                    //             Long filePage1 = 1l;
                    //             Long filePage2 = 1l;

                    //             String filePage1Temp = file.getValueByFormat("${PAGE}", fileName1, inFile);
                    //             String filePage2Temp = file.getValueByFormat("${PAGE}", fileName2, inFile);

                    //             if(filePage1Temp != null && !filePage1Temp.isEmpty())
                    //             {
                    //                 filePage1 = Long.valueOf(filePage1Temp);
                    //             }
                    //             if(filePage2Temp != null && !filePage2Temp.isEmpty())
                    //             {
                    //                 filePage2 = Long.valueOf(filePage2Temp);
                    //             }

                    //             return (int) ((fileId1 - fileId2) * 1000 + (filePage1 - filePage2));

                    //         }).collect(Collectors.toList());

                    for (Map<String, String> pathFileReader : result) {
                        try {
                            String dataJson = file.OpenFile(pathFileReader.get("path"));
                            // check sum file
                            String fileOrgin = pathFileReader.get("fileName");
                            String sumFileOrgine = file.getValueByFormat("${SUM}", fileOrgin, inFile);
                            
                            if(sumFileOrgine != null && !sumFileOrgine.isEmpty())
                            {
                                Long checkSumOrgine = file.getCRC32Checksum(dataJson.getBytes());
                                if (!sumFileOrgine.equals(checkSumOrgine.toString())) {
                                    throw new Exception(
                                            "Check sum <" + checkSumOrgine + "><" + sumFileOrgine + "> [" + fileOrgin + "]["
                                                    + inFile + "] không hợp lệ file: " + pathFileReader.get("path"));
                                }
                            }

                            ResponseEntity<String> resultJson = httpClient.putDataByApi(urlApi, headerParam, dataJson.replace(System.getProperty("line.separator").toString(), ""));
                            if (resultJson.getStatusCode() == HttpStatus.OK) {

                                // Nếu có cấu hình tạo file output thì tạo file
                                if (outFile != null) {
                                    // tao file
                                    byte[] fileByte = resultJson.getBody().getBytes();
                                    resultJson = null; // clear data
                                    String fileName = outFile;

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
								    String currentDateTime = dateFormat.format(new Date());	

                                    long checkSum = file.getCRC32Checksum(fileByte);
                                    String fileId = file.getValueByFormat("${ID}", fileOrgin, inFile);
                                    String filePage = file.getValueByFormat("${PAGE}", fileOrgin, inFile);
                                    fileName = fileName.replaceAll("\\$\\{YYYYMMDD\\}", currentDateTime + "");
                                    fileName = fileName.replaceAll("\\$\\{SUM\\}", checkSum + "");
                                    fileName = fileName.replaceAll("\\$\\{ID\\}", fileId);
                                    fileName = fileName.replaceAll("\\$\\{PAGE\\}", filePage);

                                    // if (pendingFilePhysic != null && !pendingFilePhysic.isEmpty()) {
                                    //     file.SaveFile(fileByte, fileName, pendingFilePhysic);
                                    // }
                                    if (outFilePhysic != null && !outFilePhysic.isEmpty()) {
                                        file.SaveFile(fileByte, fileName, outFilePhysic);
                                    }

                                    EJson resultFile = new EJson();
                                    resultFile.put("CHECKSUM", checkSum + "");
                                    resultFile.put("FILENAME", fileName);
                                    resultFile.put("FILEID", fileId);
                                    listFileResult.add(resultFile.jsonObject());
                                } else {
                                    /*
                                     * Dưu kết quả vào db. trong trường hợp ko tạo file Trường hợp dành cho đọc file
                                     * đối soát. sẽ ko tạo ra file Nên cần lưu response body kết quả đối soát cho dễ
                                     * quản lý
                                     */
                                    contribution.getStepExecution().getExecutionContext().put("CONTENT",
                                            resultJson.getBody());
                                }

                                // try {
                                //     // move file to folder done
                                //     if(saveDoneFile.toLowerCase().equals("true")){
                                //         file.SaveFile(dataJson.getBytes(), fileOrgin, doneFilePhysic);
                                //     }     
                                // }catch (Exception e) {

                                // }                        
                                
                                // delete file orgine
                                Files.delete(Paths.get(pathFileReader.get("path")));
                            }
                        } catch (Exception e) {
                            throw new Exception(e.getMessage(), e.getCause());
                        }
                    }

                    // return data
                    EJson resultLog = new EJson();
                    resultLog.put("STATUS", "SUCCESS");
                    resultLog.put("FILES", listFileResult);
                    contribution.getStepExecution().getExecutionContext().put("RESULT", resultLog.jsonString());
                } catch (final IOException e) {
                    throw new Exception(e.getMessage(), e.getCause());
                }
            } else {
                throw new Exception("Hệ thống gọi api: getSysPara Gặp lỗi");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e.getCause());
        }
        return RepeatStatus.FINISHED;
    }

    private boolean matchNameFile(String fileName, String format){
        boolean result = true;
        String[] listFormat = format.split("_");
        String[] listProFile = fileName.split("_");
        if (listFormat.length == listProFile.length) {
            for (int i = 0; i < listFormat.length; i++) {
                if (listFormat[i].startsWith("${") && listFormat[i].endsWith("}")) {
                    continue;
                }else if(!listFormat[i].equals(listProFile[i])){
                    return false;
                }
            }
        }else{
            return false;
        }
        return result;
    }
}