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

public class Utils {


    public static long COUNTRY_DEFAULT = 1;
    public static long GAA_DC01__HH_RELATIONSHIP = 2;
    public static long GAA_DC01__MARRIAGE_CHUA_KET_HON = 1;
    public static long GAA_DC01__MARRIAGE_DA_KET_HON = 2;
    public static long GAA_DC01__MARRIAGE_DA_LY_HON = 3;


    /**
     * The constant FILE_TYPE.
     */
    public static String FILE_TYPE = "fileType";
    /**
     * The constant FILE_NAME.
     */
    public static String FILE_NAME = "fileName";
    /**
     * The constant STATUS.
     */
    public static String STATUS = "status";
    /**
     * The constant SEARCH_LIKE_OFF.
     */
    public static String SEARCH_LIKE_OFF = "%on%";

    /**
     * The constant TYPE
     * http://kho-dan-cu-qg.dcqg.vn:9000/ords/csdl_dancuqg/dancudongbo/kiem-tra-thong-tin/:check_type
     * . check_type: 1- thêm mới 2. update
     */
    public static long GAA_THEM_MOI = 1;
    public static long GAA_UPDATE = 1;

    /**
     * Gen md 5 hash string.
     *
     * @param source the source
     * @return the string
     */
    public static String genMD5Hash(String source) {
        String md5Hash = "";
        try {
            if (source == null || source.isEmpty()) {
                return "";
            }
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Add password bytes to digest
            md.update(source.getBytes());
            // Get the hash's bytes
            byte[] bytes = md.digest();
            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            // Get complete hashed password in hex format
            md5Hash = sb.toString();
            return md5Hash;
        } catch (Exception ex) {
            System.out.println("error : " + ex.toString());
            return null;
        }
    }

    // public static String toPrettyFormat(String jsonString) {
    // JsonParser parser = new JsonParser();
    // JsonObject json = parser.parse(jsonString).getAsJsonObject();
    //
    // Gson gson = new GsonBuilder().setPrettyPrinting().create();
    //
    // return gson.toJson(json);
    // }

    /***
     *
     * @param strDate:      yyyymmddhhmiss
     * @param outputFormat, 1:dd/mm/yyyy, 2: dd/mm/yyyy hh:mi:ss
     * @return
     */
    public String convertDateStringFormat(String strDate, int outputFormat) {
        if (!StringUtils.isEmpty(strDate) && outputFormat == 1) {
            return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/" + strDate.substring(0, 4);
        }

        if (!StringUtils.isEmpty(strDate) && outputFormat == 2) {
            return strDate.substring(6, 8) + "/" + strDate.substring(4, 6) + "/" + strDate.substring(0, 4) + " " + strDate.substring(8, 10) + ":" + strDate.substring(10, 12) + ":"
                    + strDate.substring(12, 14);
        }
        if (!StringUtils.isEmpty(strDate) && outputFormat == 3) {
            return strDate.substring(4, 6) + "/" + strDate.substring(0, 4);
        }
        return "";
    }

    /***
     *
     * @param date:           Input date
     * @param initDateFormat: input date format
     * @param endDateFormat,  result date formatted
     * @return String
     */
    public String dateConveterString(String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    public String getDateString(String strdate) {
        String result = "";
        if (!StringUtils.isEmpty(strdate) && !strdate.equals("")) {
            if (strdate.length() == 10) {
                String[] arr = strdate.split("/");
                result = arr[2] + "" + arr[1] + "" + arr[0];
            } else if (strdate.length() == 7) {
                String[] arr = strdate.split("/");
                result = arr[1] + "" + arr[0];
            } else {
                result = strdate;
            }

        }
        return result;
    }


    public static String trimDataString(String str) {
        if (!StringUtils.isEmpty(str)) {
            return str.trim();
        }

        return str;
    }

    public static String filterXSS(String str, boolean isEncode) {
//        if (!str.isEmpty()) {
//            Whitelist whitelist = Whitelist.relaxed().addAttributes("table",
//                    new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords", "dir",
//                            "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref", "rel", "rev",
//                            "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace", "width" })
//                    .addAttributes("tr",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("th",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("thead",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("tbody",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("td",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("p",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("h1",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("h2",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("h3",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("h4",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("h5",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("h6",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("li",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("ul",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("div",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" })
//                    .addAttributes("span",
//                            new String[] { "style", "align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class", "color", "cols", "colspan", "coords",
//                                    "dir", "face", "height", "hspace", "ismap", "lang", "marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref",
//                                    "rel", "rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap", "valign", "value", "vlink", "vspace",
//                                    "width" });
//            str = Jsoup.clean(str, whitelist);
//            if (isEncode) {
//                str = replaceSC(deReplaceSC(str));
//            } else {
//                str = str.replaceAll("'", "&apos;");
//            }
//        }
        return str;
    }

    public static String deReplaceSC(String str) {
//        String return_ = "";
//        if (!StringUtils.isEmpty(str)) {
//            return_ = str;
//            return_ = return_.replaceAll("&amp;", "&");
//            return_ = return_.replaceAll("&apos;", "'");
//            return_ = return_.replaceAll("&quot;", "\"");
//            return_ = return_.replaceAll("&lt;", "<");
//            return_ = return_.replaceAll("&gt;", ">");
//            return_ = return_.replaceAll("&cent;", "¢");
//            return_ = return_.replaceAll("&pound;", "£");
//            return_ = return_.replaceAll("&yen;", "¥");
//            return_ = return_.replaceAll("&euro;", "€");
//            return_ = return_.replaceAll("&copy;", "©");
//            return_ = return_.replaceAll("&reg;", "®");
//            return_ = return_.replaceAll("&equals;", "=");
//            return_ = return_.replaceAll("&comma;", ",");
//            return_ = return_.replaceAll("<br>", "\r\n");
//        }
//        return return_;
        return str;
    }

    public static String replaceSC(String str) {
//        String return_ = "";
//        if (!StringUtils.isEmpty(str)) {
//            return_ = str;
//            return_ = return_.replaceAll("&", "&amp;");
//            return_ = return_.replaceAll("'", "&apos;");
//            return_ = return_.replaceAll("\"", "&quot;");
//            return_ = return_.replaceAll("<", "&lt;");
//            return_ = return_.replaceAll(">", "&gt;");
//            return_ = return_.replaceAll("¢", "&cent;");
//            return_ = return_.replaceAll("£", "&pound;");
//            return_ = return_.replaceAll("¥", "&yen;");
//            return_ = return_.replaceAll("€", "&euro;");
//            return_ = return_.replaceAll("©", "&copy;");
//            return_ = return_.replaceAll("®", "&reg;");
//            return_ = return_.replaceAll("=", "&equals;");
//            return_ = return_.replaceAll(",", "&comma;");
//            return_ = return_.replaceAll("\n", "\r\n");
//            return_ = return_.replaceAll("\r\n", "<br>");
//
//        }
        return str;
    }

    public static String removeBr(String str) {
        String return_ = "";
        if (!StringUtils.isEmpty(str)) {
            return_ = str;
            return_ = return_.replaceAll("<br>", "");
        }
        return return_;
    }

    // ham convert dia chi chi tiet phuc vu du lieu golive
    public static String convertAddress(String regPlaceAddress) {
        Integer begin = null;
        String regPlaceAddressFinal = "";

        if (!StringUtils.isEmpty(regPlaceAddress)) {
            String regPlaceAddressTemp = regPlaceAddress;
            // index tinh
            Integer indexTinhFinal = -1;
            Integer indexHuyenFinal = -1;
            Integer indexPhuongFinal = -1;

            Integer indexTinh = regPlaceAddressTemp.toUpperCase().lastIndexOf("TỈNH");
            indexTinhFinal = indexTinh;

            indexTinh = regPlaceAddressTemp.toUpperCase().lastIndexOf("THÀNH PHỐ");
            if (indexTinh > indexTinhFinal) {
                indexTinhFinal = indexTinh;
            }

            indexTinh = regPlaceAddressTemp.toUpperCase().lastIndexOf("TP ");
            if (indexTinh > indexTinhFinal) {
                indexTinhFinal = indexTinh;
            }

            indexTinh = regPlaceAddressTemp.toUpperCase().lastIndexOf("TP.");
            if (indexTinh > indexTinhFinal) {
                indexTinhFinal = indexTinh;
            }

            if (indexTinhFinal != -1) {
                regPlaceAddressTemp = regPlaceAddressTemp.substring(0, indexTinhFinal);
            }


            // index huyen
            Integer indexHuyen = regPlaceAddressTemp.toUpperCase().lastIndexOf("HUYỆN");
            indexHuyenFinal = indexHuyen;

            indexHuyen = regPlaceAddressTemp.toUpperCase().lastIndexOf("THỊ XÃ");
            if (indexHuyen > indexHuyenFinal) {
                indexHuyenFinal = indexHuyen;
            }
            indexHuyen = regPlaceAddressTemp.toUpperCase().lastIndexOf("TP ");
            if (indexHuyen > indexHuyenFinal) {
                indexHuyenFinal = indexHuyen;
            }
            indexHuyen = regPlaceAddressTemp.toUpperCase().lastIndexOf("TP.");
            if (indexHuyen > indexHuyenFinal) {
                indexHuyenFinal = indexHuyen;
            }

            indexHuyen = regPlaceAddressTemp.toUpperCase().lastIndexOf("QUẬN");
            if (indexHuyen > indexHuyenFinal) {
                indexHuyenFinal = indexHuyen;
            }

            indexHuyen = regPlaceAddressTemp.toUpperCase().lastIndexOf("THÀNH PHỐ");
            if (indexHuyen > indexHuyenFinal) {
                indexHuyenFinal = indexHuyen;
            }

            if (indexHuyenFinal != -1) {
                regPlaceAddressTemp = regPlaceAddressTemp.substring(0, indexHuyenFinal);
            }

            // index phuong
            Integer indexPhuong = regPlaceAddressTemp.toUpperCase().lastIndexOf("PHƯỜNG");
            indexPhuongFinal = indexPhuong;

            indexPhuong = regPlaceAddressTemp.toUpperCase().lastIndexOf("XÃ");
            if (indexPhuong > indexPhuongFinal) {
                indexPhuongFinal = indexPhuong;
            }

            indexPhuong = regPlaceAddressTemp.toUpperCase().lastIndexOf("THỊ TRẤN");
            if (indexPhuong > indexPhuongFinal) {
                indexPhuongFinal = indexPhuong;
            }

            // cat
            if (indexHuyenFinal != -1 && indexTinhFinal != -1) {
                begin = indexHuyenFinal;
            }

            if (indexPhuongFinal != -1 && indexHuyenFinal != -1 && indexTinhFinal != -1) {
                begin = indexPhuongFinal;
            }

            if (begin != null && begin >= 0) {
                int sizeStr = regPlaceAddress.length();
                regPlaceAddress = regPlaceAddress.substring(0, begin).trim();

                String[] arrStr = regPlaceAddress.split(",");
                for (int i = 0; i <= arrStr.length - 1; i++) {
                    if (arrStr[i] != null && !arrStr[i].trim().equals("")) {

                        if (i == arrStr.length - 1) {
                            regPlaceAddressFinal += arrStr[i];
                        } else {
                            regPlaceAddressFinal += arrStr[i] + ",";
                        }
                    }

                }
            } else {
                regPlaceAddressFinal = regPlaceAddress;
            }
        }
        return regPlaceAddressFinal;
    }

    public String checkUnicodeToHop(EJson params) {
        List<String> codeTohop = new ArrayList<>();
        codeTohop.add("ẻ"); //ẻ \u0065\u0309
        codeTohop.add("é"); //é
        codeTohop.add("è"); //è
        codeTohop.add("ẹ"); //ẹ
        codeTohop.add("ẽ"); //ẽ
        codeTohop.add("ể"); //ể
        codeTohop.add("ế"); //ế
        codeTohop.add("ề"); //ề
        codeTohop.add("ệ"); //ệ
        codeTohop.add("ễ"); //ễ
        codeTohop.add("ỷ"); //ỷ
        codeTohop.add("ý"); //ý
        codeTohop.add("ỳ"); //ỳ
        codeTohop.add("ỵ"); //ỵ
        codeTohop.add("ỹ"); //ỹ
        codeTohop.add("ủ"); //ủ
        codeTohop.add("ú"); //ú
        codeTohop.add("ù"); //ù
        codeTohop.add("ụ"); //ụ
        codeTohop.add("ũ"); //ũ
        codeTohop.add("ử"); //ử
        codeTohop.add("ứ"); //ứ
        codeTohop.add("ừ"); //ừ
        codeTohop.add("ự"); //ự
        codeTohop.add("ữ"); //ữ
        codeTohop.add("ỉ"); //ỉ
        codeTohop.add("í"); //í
        codeTohop.add("ì"); //ì
        codeTohop.add("ị"); //ị
        codeTohop.add("ĩ"); //ĩ
        codeTohop.add("ỏ"); //ỏ
        codeTohop.add("ó"); //ó
        codeTohop.add("ò"); //ò
        codeTohop.add("ọ"); //ọ
        codeTohop.add("õ"); //õ
        codeTohop.add("ở"); //ở
        codeTohop.add("ớ"); //ớ
        codeTohop.add("ờ"); //ờ
        codeTohop.add("ợ"); //ợ
        codeTohop.add("ỡ"); //ỡ
        codeTohop.add("ổ"); //ổ
        codeTohop.add("ố"); //ố
        codeTohop.add("ồ"); //ồ
        codeTohop.add("ộ"); //ộ
        codeTohop.add("ỗ"); //ỗ
        codeTohop.add("ả"); //ả
        codeTohop.add("á"); //á
        codeTohop.add("à"); //à
        codeTohop.add("ạ"); //ạ
        codeTohop.add("ã"); //ã
        codeTohop.add("ẳ"); //ẳ
        codeTohop.add("ắ"); //ắ
        codeTohop.add("ằ"); //ằ
        codeTohop.add("ặ"); //ặ
        codeTohop.add("ẵ"); //ẵ
        codeTohop.add("ẩ"); //ẩ
        codeTohop.add("ấ"); //ấ
        codeTohop.add("ầ"); //ầ
        codeTohop.add("ậ"); //ậ
        codeTohop.add("ẫ"); //ẫ
        codeTohop.add("ð"); //ð CP 128
        codeTohop.add("Ẻ"); //Ẻ
        codeTohop.add("É"); //É
        codeTohop.add("È"); //È
        codeTohop.add("Ẹ"); //Ẹ
        codeTohop.add("Ẽ"); //Ẽ
        codeTohop.add("Ể"); //Ể
        codeTohop.add("Ế"); //Ế
        codeTohop.add("Ề"); //Ề
        codeTohop.add("Ệ"); //Ệ
        codeTohop.add("Ễ"); //Ễ
        codeTohop.add("Ỷ"); //Ỷ
        codeTohop.add("Ý"); //Ý
        codeTohop.add("Ỳ"); //Ỳ
        codeTohop.add("Ỵ"); //Ỵ
        codeTohop.add("Ỹ"); //Ỹ
        codeTohop.add("Ủ"); //Ủ
        codeTohop.add("Ú"); //Ú
        codeTohop.add("Ù"); //Ù
        codeTohop.add("Ụ"); //Ụ
        codeTohop.add("Ũ"); //Ũ
        codeTohop.add("Ử"); //Ử
        codeTohop.add("Ứ"); //Ứ
        codeTohop.add("Ừ"); //Ừ
        codeTohop.add("Ự"); //Ự
        codeTohop.add("Ữ"); //Ữ
        codeTohop.add("Ỉ"); //Ỉ
        codeTohop.add("Í"); //Í
        codeTohop.add("Ì"); //Ì
        codeTohop.add("Ị"); //Ị
        codeTohop.add("Ĩ"); //Ĩ
        codeTohop.add("Ỏ"); //Ỏ
        codeTohop.add("Ó"); //Ó
        codeTohop.add("Ò"); //Ò
        codeTohop.add("Ọ"); //Ọ
        codeTohop.add("Õ"); //Õ
        codeTohop.add("Ở"); //Ở
        codeTohop.add("Ớ"); //Ớ
        codeTohop.add("Ờ"); //Ờ
        codeTohop.add("Ợ"); //Ợ
        codeTohop.add("Ỡ"); //Ỡ
        codeTohop.add("Ổ"); //Ổ
        codeTohop.add("Ố"); //Ố
        codeTohop.add("Ồ"); //Ồ
        codeTohop.add("Ộ"); //Ộ
        codeTohop.add("Ỗ"); //Ỗ
        codeTohop.add("Ả"); //Ả
        codeTohop.add("Á"); //Á
        codeTohop.add("À"); //À
        codeTohop.add("Ạ"); //Ạ
        codeTohop.add("Ã"); //Ã
        codeTohop.add("Ẳ"); //Ẳ
        codeTohop.add("Ắ"); //Ắ
        codeTohop.add("Ằ"); //Ằ
        codeTohop.add("Ặ"); //Ặ
        codeTohop.add("Ẵ"); //Ẵ
        codeTohop.add("Ẩ"); //Ẩ
        codeTohop.add("Ấ"); //Ấ
        codeTohop.add("Ầ"); //Ầ
        codeTohop.add("Ậ"); //Ậ
        codeTohop.add("Ẫ"); //Ẫ
        codeTohop.add("Ð"); //Ð CP 128
        EJson errorJson = new EJson();
        Boolean hasError = false;

        if (params != null) {
            for (Map.Entry<String, JsonElement> entry : params.jsonObject().entrySet()) {
                JsonElement value = entry.getValue();

                if (value.isJsonArray()) {
                    continue;
                }
                if (value.isJsonObject()) {
                    continue;
                }
                if (value.isJsonNull() || StringUtils.isEmpty(value.getAsString())) {
                    continue;
                }

                Integer index = -1;
                for (int i = 0; i < codeTohop.size(); i++) {
                    if (value.getAsString().indexOf(codeTohop.get(i)) != -1) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    hasError = true;
                    errorJson.addError(entry.getKey(), "Dữ liệu không được dùng bảng mã unicode tổ hợp");
                }
            }
        }
        if (hasError) {
            return errorJson.error();
        }
        return "";
    }

    public EJson convertToUnicode(EJson paramJson) {
        EJson rsParam = new EJson();
        if (paramJson != null) {
            for (Map.Entry<String, JsonElement> entry : paramJson.jsonObject().entrySet()) {
                JsonElement value = entry.getValue();

                if (value.isJsonArray()) {
                    rsParam.put(entry.getKey(), value.getAsJsonArray());
                } else if (value.isJsonNull() || StringUtils.isEmpty(value.getAsString())) {
                    rsParam.put(entry.getKey(), value);
                } else if (value.getAsString().equals("true") || value.getAsString().equals("false")) {
                    rsParam.put(entry.getKey(), value);
                } else {
                    rsParam.put(entry.getKey(), converterUnicode(value.getAsString()));
                }
            }
        }

        return rsParam;
    }


    public static String converterUnicode(String str) {
        String rsStr = str;
        if (!StringUtils.isEmpty(str)) {
            rsStr = rsStr.replaceAll("é", "é");
            rsStr = rsStr.replaceAll("è", "è");
            rsStr = rsStr.replaceAll("ẹ", "ẹ");
            rsStr = rsStr.replaceAll("ẽ", "ẽ");
            rsStr = rsStr.replaceAll("ể", "ể");
            rsStr = rsStr.replaceAll("ế", "ế");
            rsStr = rsStr.replaceAll("ề", "ề");
            rsStr = rsStr.replaceAll("ệ", "ệ");
            rsStr = rsStr.replaceAll("ễ", "ễ");
            rsStr = rsStr.replaceAll("ỷ", "ỷ");
            rsStr = rsStr.replaceAll("ý", "ý");
            rsStr = rsStr.replaceAll("ỳ", "ỳ");
            rsStr = rsStr.replaceAll("ỵ", "ỵ");
            rsStr = rsStr.replaceAll("ỹ", "ỹ");
            rsStr = rsStr.replaceAll("ủ", "ủ");
            rsStr = rsStr.replaceAll("ú", "ú");
            rsStr = rsStr.replaceAll("ù", "ù");
            rsStr = rsStr.replaceAll("ụ", "ụ");
            rsStr = rsStr.replaceAll("ũ", "ũ");
            rsStr = rsStr.replaceAll("ử", "ử");
            rsStr = rsStr.replaceAll("ứ", "ứ");
            rsStr = rsStr.replaceAll("ừ", "ừ");
            rsStr = rsStr.replaceAll("ự", "ự");
            rsStr = rsStr.replaceAll("ữ", "ữ");
            rsStr = rsStr.replaceAll("ỉ", "ỉ");
            rsStr = rsStr.replaceAll("í", "í");
            rsStr = rsStr.replaceAll("ì", "ì");
            rsStr = rsStr.replaceAll("ị", "ị");
            rsStr = rsStr.replaceAll("ĩ", "ĩ");
            rsStr = rsStr.replaceAll("ỏ", "ỏ");
            rsStr = rsStr.replaceAll("ó", "ó");
            rsStr = rsStr.replaceAll("ò", "ò");
            rsStr = rsStr.replaceAll("ọ", "ọ");
            rsStr = rsStr.replaceAll("õ", "õ");
            rsStr = rsStr.replaceAll("ở", "ở");
            rsStr = rsStr.replaceAll("ớ", "ớ");
            rsStr = rsStr.replaceAll("ờ", "ờ");
            rsStr = rsStr.replaceAll("ợ", "ợ");
            rsStr = rsStr.replaceAll("ỡ", "ỡ");
            rsStr = rsStr.replaceAll("ổ", "ổ");
            rsStr = rsStr.replaceAll("ố", "ố");
            rsStr = rsStr.replaceAll("ồ", "ồ");
            rsStr = rsStr.replaceAll("ộ", "ộ");
            rsStr = rsStr.replaceAll("ỗ", "ỗ");
            rsStr = rsStr.replaceAll("ả", "ả");
            rsStr = rsStr.replaceAll("á", "á");
            rsStr = rsStr.replaceAll("à", "à");
            rsStr = rsStr.replaceAll("ạ", "ạ");
            rsStr = rsStr.replaceAll("ã", "ã");
            rsStr = rsStr.replaceAll("ẳ", "ẳ");
            rsStr = rsStr.replaceAll("ắ", "ắ");
            rsStr = rsStr.replaceAll("ằ", "ằ");
            rsStr = rsStr.replaceAll("ặ", "ặ");
            rsStr = rsStr.replaceAll("ẵ", "ẵ");
            rsStr = rsStr.replaceAll("ẩ", "ẩ");
            rsStr = rsStr.replaceAll("ấ", "ấ");
            rsStr = rsStr.replaceAll("ầ", "ầ");
            rsStr = rsStr.replaceAll("ậ", "ậ");
            rsStr = rsStr.replaceAll("ẫ", "ẫ");
            rsStr = rsStr.replaceAll("ð", "đ");
            rsStr = rsStr.replaceAll("Ẻ", "Ẻ");
            rsStr = rsStr.replaceAll("É", "É");
            rsStr = rsStr.replaceAll("È", "È");
            rsStr = rsStr.replaceAll("Ẹ", "Ẹ");
            rsStr = rsStr.replaceAll("Ẽ", "Ẽ");
            rsStr = rsStr.replaceAll("Ể", "Ể");
            rsStr = rsStr.replaceAll("Ế", "Ế");
            rsStr = rsStr.replaceAll("Ề", "Ề");
            rsStr = rsStr.replaceAll("Ệ", "Ệ");
            rsStr = rsStr.replaceAll("Ễ", "Ễ");
            rsStr = rsStr.replaceAll("Ỷ", "Ỷ");
            rsStr = rsStr.replaceAll("Ý", "Ý");
            rsStr = rsStr.replaceAll("Ỳ", "Ỳ");
            rsStr = rsStr.replaceAll("Ỵ", "Ỵ");
            rsStr = rsStr.replaceAll("Ỹ", "Ỹ");
            rsStr = rsStr.replaceAll("Ủ", "Ủ");
            rsStr = rsStr.replaceAll("Ú", "Ú");
            rsStr = rsStr.replaceAll("Ù", "Ù");
            rsStr = rsStr.replaceAll("Ụ", "Ụ");
            rsStr = rsStr.replaceAll("Ũ", "Ũ");
            rsStr = rsStr.replaceAll("Ử", "Ử");
            rsStr = rsStr.replaceAll("Ứ", "Ứ");
            rsStr = rsStr.replaceAll("Ừ", "Ừ");
            rsStr = rsStr.replaceAll("Ự", "Ự");
            rsStr = rsStr.replaceAll("Ữ", "Ữ");
            rsStr = rsStr.replaceAll("Ỉ", "Ỉ");
            rsStr = rsStr.replaceAll("Í", "Í");
            rsStr = rsStr.replaceAll("Ì", "Ì");
            rsStr = rsStr.replaceAll("Ị", "Ị");
            rsStr = rsStr.replaceAll("Ĩ", "Ĩ");
            rsStr = rsStr.replaceAll("Ỏ", "Ỏ");
            rsStr = rsStr.replaceAll("Ó", "Ó");
            rsStr = rsStr.replaceAll("Ò", "Ò");
            rsStr = rsStr.replaceAll("Ọ", "Ọ");
            rsStr = rsStr.replaceAll("Õ", "Õ");
            rsStr = rsStr.replaceAll("Ở", "Ở");
            rsStr = rsStr.replaceAll("Ớ", "Ớ");
            rsStr = rsStr.replaceAll("Ờ", "Ờ");
            rsStr = rsStr.replaceAll("Ợ", "Ợ");
            rsStr = rsStr.replaceAll("Ỡ", "Ỡ");
            rsStr = rsStr.replaceAll("Ổ", "Ổ");
            rsStr = rsStr.replaceAll("Ố", "Ố");
            rsStr = rsStr.replaceAll("Ồ", "Ồ");
            rsStr = rsStr.replaceAll("Ộ", "Ộ");
            rsStr = rsStr.replaceAll("Ỗ", "Ỗ");
            rsStr = rsStr.replaceAll("Ả", "Ả");
            rsStr = rsStr.replaceAll("Á", "Á");
            rsStr = rsStr.replaceAll("À", "À");
            rsStr = rsStr.replaceAll("Ạ", "Ạ");
            rsStr = rsStr.replaceAll("Ã", "Ã");
            rsStr = rsStr.replaceAll("Ẳ", "Ẳ");
            rsStr = rsStr.replaceAll("Ắ", "Ắ");
            rsStr = rsStr.replaceAll("Ằ", "Ằ");
            rsStr = rsStr.replaceAll("Ặ", "Ặ");
            rsStr = rsStr.replaceAll("Ẵ", "Ẵ");
            rsStr = rsStr.replaceAll("Ẩ", "ẩ");
            rsStr = rsStr.replaceAll("Ấ", "Ấ");
            rsStr = rsStr.replaceAll("Ầ", "Ầ");
            rsStr = rsStr.replaceAll("Ậ", "Ậ");
            rsStr = rsStr.replaceAll("Ẫ", "Ẫ");
            rsStr = rsStr.replaceAll("Ð", "Đ"); // CP 128
        }
        return rsStr;
    }

    public static <T> String arrayToString(T[] array, String delimiter) {
        StringBuilder arTostr = new StringBuilder();
        if (array.length > 0) {
            arTostr.append(array[0]);
            for (int i = 1; i < array.length; i++) {
                arTostr.append(delimiter);
                arTostr.append(array[i]);
            }
        }

        return arTostr.toString();
    }

    public static Long[] toLongArray(String[] strArray) {
        Long[] data = new Long[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            data[i] = Long.valueOf(strArray[i]);
        }

        return data;
    }

    public static String convertStringToTimestamp(String strDate) throws ParseException {
        String date = null;
        if (!StringUtils.isEmpty(strDate)) {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            Date tmp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(strDate);
            String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.setTimeZone(tz);
            date = simpleDateFormat.format(tmp);
        }
        return date;
    }

    public static String convertStringToTimestamp(Date strDate) throws ParseException {
        if (strDate != null) {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.setTimeZone(tz);
            return simpleDateFormat.format(strDate);
        }
        return null;
    }

    public static String toISO8601UTC(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //bỏ dấu tiếng việt
    public static String removeAccent(String value) {
        try {
            String str = value.replaceAll("đ", "d").replaceAll("Đ", "D");
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    //xóa khoảng trắng
    public static String removeSpace(String str) {
        StringBuilder sb = new StringBuilder(str);
        return sb.toString().trim().replaceAll(" ", "");
    }

    public static LocalDateTime convertToLocalDate(Date dateToConvert) {
        if (dateToConvert == null) return null;
        return dateToConvert.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();
    }
}