package vnpt.net.syndata.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

import vnpt.net.syndata.configuration.CustomException;

import java.net.InetAddress;
import java.util.UUID;
import java.util.Map.Entry;

public class Utils {
    private static final int IS_SINGLE = 1;
    private static final int IS_MULTI = 2;

    public static final String ACTION_ADD = "add";
    public static final String ACTION_DEL = "del";
    public static final String ACTION_SEL = "sel";

    public String getIPAddress() throws CustomException {
        String ipAddress = "";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ipAddress = inetAddress.toString();
        } catch (Exception e) {
            ipAddress = "127.0.0.1";
        }
        return ipAddress;
    }

    public static String createUUID() {
        String uuId = UUID.randomUUID().toString().replaceAll("-", "");
        return uuId;
    }

    public static boolean getTypeRun(int typeRun) {
        if (typeRun == IS_SINGLE) {
            return true;
        }
        return false;
    }

    public static LocalDateTime convertToLocalDate(Date dateToConvert) {
        if (dateToConvert == null)
            return null;
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date convertToDate(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static EJson mergeData(String oldS, String newS, String endS){
        EJson oldData = new EJson(oldS);
        EJson newData = new EJson(newS);
        EJson endData = new EJson(endS);

        for (Entry<String, JsonElement> itemNew : newData.jsonObject().entrySet()) {
            for (Entry<String, JsonElement> itemOld : oldData.jsonObject().entrySet()) {
                if(!itemNew.getKey().equals(itemOld.getKey())){
                    continue;
                }

                if(itemNew.getValue() == null && itemOld.getValue() == null){
                    continue;
                }

                if(itemNew.getValue() != null && itemOld.getValue() != null && itemNew.getValue().toString().equals(itemOld.getValue().toString())){
                    continue;
                }

                // du lieu thay doi
                Gson gson = new GsonBuilder().create();
		        JsonElement element = gson.toJsonTree(itemNew.getValue());
                endData.jsonObject().add(itemNew.getKey(), element);
            }
        }

        return endData;
    }
}
