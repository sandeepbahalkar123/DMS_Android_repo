package com.scorg.dms.util;

/**
 * @author Sandeep Bahalkar
 */
public class DmsConstants {
    public static final String DMS_LOG_FOLDER = "DMS_LOG";
    public static final String DMS_LOG_FILE = "DMS_LOG_FILE.txt";

    //This is for bydefault textcolor,headercolor,buttoncolor etc.
    public static String HEADER_COLOR = "#E4422C";
    public static String HEADER_TEXT_COLOR = "#FFFFFF";
    public static String EVENT_CIRCLE_COLOR = "#E4422C";
    public static String BUTTON_COLOR = "#E4422C";
    public static String BUTTON_TEXT_COLOR = "#FFFFFF";
    public static String TEXT_COLOR = "#000000";
    public static final String ID = "_id";

    public static final String patientName = "patientName";
    public static final String fileType = "fileType";
    public static final String referenceId = "referenceId";
    public static final String dateType = "dateType";
    public static final String fromDate = "fromDate";
    public static final String toDate = "toDate";
    public static final String annotationText = "annotationText";
    public static final String DocTypeId = "DocTypeId";

    public static final String DEVICEID = "Device-Id";
    public static final String OS = "OS";
    public static final String OSVERSION = "OS-Version";
    public static final String DATEFORMAT = "dd/MM/yy";
    public static final String DEVICETYPE = "deviceType";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String TOKEN_TYPE = "token_type";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String LDPI = "/LDPI/";
    public static final String MDPI = "/MDPI/";
    public static final String HDPI = "/HDPI/";
    public static final String XHDPI = "/XHDPI/";
    public static final String XXHDPI = "/XXHDPI/";
    public static final String XXXHDPI = "/XXXHDPI/";
    public static final String TABLET = "Tablet";
    public static final String PHONE = "Phone";


    public static final String CONTENT_TYPE = "Content-Type";
    public static final String AUTHORIZATION = "Authorization";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String APPLICATION_URL_ENCODED = "application/x-www-form-urlencoded; charset=UTF-8";
    public static final String APPLICATION_JSON = "application/json; charset=utf-8";


    //--- Request Params
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_ID_VALUE = "334a059d82304f4e9892ee5932f81425";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    // Connection codes
    public static final int REGISTRATION_CODE = 0;
    public static final int TASK_LOGIN_CODE = 1;
    public static final int TASK_PATIENT_LIST = 2;


    public static class DATE_PATTERN {
        public static String YYYY_MM_DD = "yyyy-MM-dd";
        public static String YYYY_MM_DD_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
        public static String DD_MM_YYYY_hh_mm = "dd-MM-yyyy hh:mm aa";

        public static String HH_MM = "hh:mm";
        public static String EEE_MMM_DD_MMM = "EEEE dd MMM yyyy HH:mm";
        public final static String yyyy_MM_dd = "yyyy-MM-dd";
        public final static String DD_MM_YYYY = "dd/MM/yyyy";
        public final static String EEEE_dd_MMM_yyyy_hh_mm_a = "EEEE dd MMM yyyy | hh:mm a";
    }

    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String BLANK = "";

    public static class TIME_STAMPS {
        public static int ONE_SECONDS = 1000;
        public static int TWO_SECONDS = 2000;
        public static int THREE_SECONDS = 3000;
    }
}
