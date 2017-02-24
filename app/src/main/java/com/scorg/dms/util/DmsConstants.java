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

    public static final String DEVICEID = "deviceId";
    public static final String OS = "os";
    public static final String OSVERSION = "osVersion";
    public static final String DEVICETYPE = "deviceType";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String LDPI = "/LDPI/";
    public static final String MDPI = "/MDPI/";
    public static final String HDPI = "/HDPI/";
    public static final String XHDPI = "/XHDPI/";
    public static final String XXHDPI = "/XXHDPI/";
    public static final String XXXHDPI = "/XXXHDPI/";
    public static final String TABLET = "Tablet";
    public static final String PHONE = "Phone";


    public static final String CONTENT_TYPE = "Content-Type";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String APPLICATION_URL_ENCODED = "application/x-www-form-urlencoded; charset=UTF-8";
    public static final String APPLICATION_JSON = "application/json";


    //--- Request Params
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String TRUE = "true";
    public static final String FALSE = "false";


    //Please do not change following strings anyhow beacuse patterns are used will be the same for timezone
    public static final String UTCTEXT = "2015-12-30T07:01:29.533000"; //need to replace this string with server date object
    public static final String UTCPATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
    public static final String ONLYDATEUTCPATTERN = "dd MMM yyyy";
    public static final String UTCNEWPATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FINALPATTERN = "dd MMM yyyy hh:mm a";
    public static final String TOTIMEZONE = "Asia/Kolkata";
    public static final String ONLYTIMEUTCPATTERN = "HH:mm a";
    // directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "DMS";

    public static String PROFILE_PIC_DATA = null;
    public static final String PROFILE_IMG_FOLDER = "DmsProfile";
    public static final String PROFILE_IMG_NAME = "profile.jpg";
    public static final String ONLYDAYUTCPATTERN = "dd";
    public static final String ONLYMONTHUTCPATTERN = "MMM";
    public static final String UTCPATTERN_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String UTCPATTERN1 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'";

    public static final String UTCPATTERN_TESTING = "yyyy-MM-dd HH:mm:ss'+'ssss";  //new datetime format from server
    public static final String UTCPATTERN_TESTING_CONVERT = "yyyy-MM-dd HH:mm:ss";
    public static final String UTCPATTERN_TESTING_OLD_REPORT = "yyyy-MM-dd HH:mm:ss.SSSSSS'+'ss:ss";  //old datetime format from server for report
    public static final String FINALPATTERN_OLD_REPORT = "MMM dd yyyy, hh:mm a";
    public static final int NO_OF_TIMES_HIT = 15; //if dashboad or report status not=generated

    // Connection codes
    public static final int REGISTRATION_CODE = 0;
    public static final int LOGIN_CODE = 1;


}
