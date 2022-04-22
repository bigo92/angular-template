package vnpt.net.syndata.utils;

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

public class Utils {
    private static final int IS_SINGLE = 1;
    private static final int IS_MULTI = 2;

    public static final String ACTION_ADD = "add";
    public static final String ACTION_DEL = "del";
    public static final String ACTION_SEL= "sel";

    public String getIPAddress() throws CustomException {
        String ipAddress = "";
        try{
            InetAddress inetAddress = InetAddress.getLocalHost();
            ipAddress = inetAddress.toString();
        }catch (Exception e){
            ipAddress = "127.0.0.1";
        }
        return ipAddress;
    }

    public static String createUUID(){
        String uuId = UUID.randomUUID().toString().replaceAll("-", "");
        return uuId;
    }

    public static boolean getTypeRun(int typeRun){
        if (typeRun == IS_SINGLE){
            return true;
        }
        return false;
    }

    public static LocalDateTime convertToLocalDate(Date dateToConvert) {
        if (dateToConvert == null) return null;
        return dateToConvert.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();
    }
}
