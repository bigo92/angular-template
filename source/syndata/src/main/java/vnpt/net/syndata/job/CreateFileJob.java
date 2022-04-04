package vnpt.net.syndata.job;

import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
* Job tạo file có phân trang từ internal được đẩy sang hệ thống external
*/
public class CreateFileJob implements Tasklet, InitializingBean {

	@Autowired
	private CreateFileComponent file;

	@Autowired
	private HttpClientComponent httpClient;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Map<String, Object> prameter = chunkContext.getStepContext().getJobParameters();
		try {
			// lấy tham số của job
			EJson paramJson = new EJson(prameter.get("PARAM").toString());
			String urlApi = paramJson.getString("API");
			Long size = paramJson.getLong("SIZE");
			Boolean hasPaging = paramJson.getBoolean("PAGING");

			// lay cau hinh du lieu tu api
			ResponseEntity<String> paraResult = httpClient.getSysPara();
			if (paraResult.getStatusCode() == HttpStatus.OK) {
				JsonArray listFileResult = new JsonArray();
				// doc config
				EJson paraJson = new EJson(paraResult.getBody());
				List<EJson> paraItem = paraJson.getJSONArray("ITEMS");
				String outFilePhysic = "";
				for (EJson eJson : paraItem) {
					//if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.INTERNAL.EREA.FILE.SAVING.FOLDER")) {
					if (eJson.getString("PARA_ID").equals("0.FILE.SYNC.PROJECT.SAVE.EREA.FILE.FOLDER")) {
						outFilePhysic = eJson.getString("PARA_VALUE");
					}
				}

				// lay du lieu dong bo tu api
				EJson paramRequest = new EJson();
				// Long pageDefault = paramJson.getLong("PAGE");
				// if (pageDefault == null) {
				// pageDefault = 1l;
				// }

				Long pageDefault = 1l;
				// String page = paramJson.getString("PAGE");
				// if (page != null && !page.isEmpty()) {
				// pageDefault = Long.valueOf(page);
				// }

				paramRequest.put("PAGE", pageDefault);
				paramRequest.put("SIZE", size);

				boolean isLoadDone = false;
				boolean hasData = false;
				do {
					try {
						String fileName = paramJson.getString("OUTFILE");
						ResponseEntity<String> result = httpClient.getDataByApi(urlApi, paramRequest.jsonString());
						if (result.getStatusCode() == HttpStatus.OK && paraResult.getStatusCode() == HttpStatus.OK) {
							if (result.getBody().indexOf("\"ITEMS\":[]}") == -1) {
								// tao file
								byte[] fileByte = result.getBody().getBytes();

								result = null; // clear data

								SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
								String currentDateTime = dateFormat.format(new Date());				

								long checkSum = file.getCRC32Checksum(fileByte);
								Long fileId = (Long) prameter.get("TIME");
								fileName = fileName.replaceAll("\\$\\{YYYYMMDD\\}", currentDateTime + "");
								fileName = fileName.replaceAll("\\$\\{SUM\\}", checkSum + "");
								fileName = fileName.replaceAll("\\$\\{ID\\}", fileId + "");
								fileName = fileName.replaceAll("\\$\\{PAGE\\}", paramRequest.getLong("PAGE") + "");

								if (outFilePhysic != null && !outFilePhysic.isEmpty()) {
									file.SaveFile(fileByte, fileName, outFilePhysic);
								}

								EJson resultFile = new EJson();
								resultFile.put("CHECKSUM", checkSum + "");
								resultFile.put("FILENAME", fileName);
								resultFile.put("FILEID", fileId);
								listFileResult.add(resultFile.jsonObject());

								hasData = true;
							} else {
								isLoadDone = true;
							}
						} else {
							throw new Exception("Hệ thống gọi api:"
									+ (result.getStatusCode() != HttpStatus.OK ? urlApi : "GETSYSPARA") + ". Gặp lỗi");
						}

						// nếu không có phân trang sẽ dừng ngay sau trang 1
						if (hasPaging != true) {
							isLoadDone = true;
						}
						paramRequest.put("PAGE", paramRequest.getLong("PAGE") + 1);
					} catch (Exception e) {
						paramJson.put("PAGE", paramRequest.getLong("PAGE"));
						contribution.getStepExecution().getExecutionContext().put("RESULT", paramJson.jsonString());
						throw new Exception(e.getMessage(), e.getCause());
					}
				} while (!isLoadDone);

				if (!hasData) {
					// khong co du lieu thay doi
					// return data
					EJson resultLog = new EJson();
					resultLog.put("STATUS", "SUCCESS");
					contribution.getStepExecution().getExecutionContext().put("RESULT", resultLog.jsonString());
					return RepeatStatus.FINISHED;
				}

				// return data
				EJson resultLog = new EJson();
				resultLog.put("STATUS", "SUCCESS");
				resultLog.put("FILES", listFileResult);
				contribution.getStepExecution().getExecutionContext().put("RESULT", resultLog.jsonString());
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e.getCause());
		}
		return RepeatStatus.FINISHED;
	}
}
