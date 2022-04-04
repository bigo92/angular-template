package vnpt.net.syndata.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EJson {
	private JsonElement json;
	private Dictionary<String, String> errors;
	private ResponseBodyJson responseBodyJson;

	public EJson() {
		super();
		this.json = new JsonObject();
		this.errors = new Hashtable<String, String>();
	}

	public EJson(String source) {
		super();
		JsonParser jsonParser = new JsonParser();
		if (source != null && !source.isEmpty()) {
			this.json = jsonParser.parse(source);
		}
		this.errors = new Hashtable<String, String>();
	}

	public EJson(JsonObject source) {
		super();
		this.json = source;
		this.errors = new Hashtable<String, String>();
	}

	public EJson(ResponseBodyJson responseBodyJson) {
		super();
		this.json = new JsonObject();
		this.errors = new Hashtable<String, String>();
		this.responseBodyJson = responseBodyJson;
	}

	public void put(String key, Object value) {
		this.setValue(key, value);
	}

	public String jsonString() {
		return json != null? setResponseBodyJson(json) : null;
	}

	public JsonObject jsonObject() {
		return json != null? json.getAsJsonObject() : null;
	}

	public JsonArray jsonArray() {
		return json != null? json.getAsJsonArray() : null;
	}

	public List<EJson> toArray() {
		List<EJson> result = new ArrayList<EJson>();
		for (int i = 0; i < json.getAsJsonArray().size(); i++) {
			result.add(new EJson(json.getAsJsonArray().get(i).getAsJsonObject()));
		}
		return result;
	}

	public String success() {
		setValue("result", "Success");
		setValue("msg", "OK");
		setValue("desc", "");
		return setResponseBodyJson(json);
	}

	public String success(String message) {
		setValue("result", "Success");
		setValue("msg", "OK");
		setValue("desc", message);
		return setResponseBodyJson(json);
	}

	public String error() {
		setValue("result", "Error");
		setValue("msg", "FAIL");
		setValue("desc", "Xuất hiện lỗi trong quá trình sử lý");
		if (errors.size() > 0)
			setValue("errors", errors);
		return setResponseBodyJson(json);
	}

	public String error(String message) {
		setValue("result", "Error");
		setValue("msg", "FAIL");
		setValue("desc", message);
		if (errors.size() > 0)
			setValue("errors", errors);
		return setResponseBodyJson(json);
	}

	public String error(String key, String message) {
		setValue(key, message);
		setValue("result", "Error");
		setValue("msg", "FAIL");
		setValue("desc", "");
		if (errors.size() > 0)
			setValue("errors", errors);
		return setResponseBodyJson(json);
	}

	public void addError(String key, String message) {
		errors.put(key, message);
	}

	private String setResponseBodyJson(JsonElement source) {
		String body = source.toString();
		if (responseBodyJson != null) {
			responseBodyJson.setBodyString(body);
		}
		return body;
	}

	public boolean hasValue(String key) {
		return json.getAsJsonObject().get(key).isJsonNull() ? false : json.getAsJsonObject().get(key).toString().equals("") ? false : true;
	}

	public boolean stringEmpty(String key) {
		return json.getAsJsonObject().get(key).isJsonNull() ? true : json.getAsJsonObject().get(key).toString().equals("") ? true : false;
	}

	public BigDecimal BigDecimal(String key) {
		return BigDecimal.valueOf(hasValue(key) ? json.getAsJsonObject().get(key).getAsBoolean() == true ? 1 : 0 : 0);
	}

	public String getString(String key) {
		return hasValue(key) ? json.getAsJsonObject().get(key).getAsString() : null;
	}

	public Long getLong(String key) {
		return hasValue(key) ? json.getAsJsonObject().get(key).getAsLong() : null;
	}

	public BigDecimal getBigDecimal(String key) {
		return hasValue(key) ? json.getAsJsonObject().get(key).getAsBigDecimal() : null;
	}

	public boolean getBoolean(String key) {
		return hasValue(key) ? json.getAsJsonObject().get(key).getAsBoolean() : false;
	}

	public BigDecimal convertBooleanToBigDecimal(String key) {
		return BigDecimal.valueOf(hasValue(key) ? (json.getAsJsonObject().get(key).getAsBoolean() ? 1 : 0) : 0);
	}

	public Date getDate(String key) throws ParseException {
		if (!hasValue(key)) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.parse(getString(key));
	}

	public EJson getJSONObject(String key) {
		return hasValue(key) ? new EJson(json.getAsJsonObject().get(key).getAsJsonObject()) : null;
	}

	public List<EJson> getJSONArray(String key) {
		List<EJson> data = new ArrayList<EJson>();
		if (hasValue(key)) {
			JsonParser jsonParser = new JsonParser();
			JsonArray items = new JsonArray();
			JsonElement value = json.getAsJsonObject().get(key);
			if (value.isJsonArray()) {
				items = value.getAsJsonArray();
			} else {
				items = jsonParser.parse(value.getAsString()).getAsJsonArray();
			}

			for (int i = 0; i < items.size(); i++) {
				JsonObject item = items.get(i).getAsJsonObject();
				data.add(new EJson(item));
			}
		}
		return data;
	}

	private void setValue(String key, Object value) {
		Gson gson = new GsonBuilder().create();
		JsonElement element = gson.toJsonTree(value);
		json.getAsJsonObject().add(key, element);
	}

}