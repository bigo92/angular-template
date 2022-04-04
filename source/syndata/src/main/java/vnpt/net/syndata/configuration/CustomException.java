package vnpt.net.syndata.configuration;

import vnpt.net.syndata.utils.EJson;

public class CustomException extends Exception {
	private static final long serialVersionUID = 1L;
	private String errorJson;

	public CustomException(String key, String message) {
		super(message);
		EJson json = new EJson();
		json.addError(key, message);
		this.errorJson = json.error();
	}
	
	public CustomException(String message) {
		super(message);
	}

	public String getErrorJson() {
		return errorJson;
	}

	public void setErrorJson(String errorJson) {
		this.errorJson = errorJson;
	}
	
}