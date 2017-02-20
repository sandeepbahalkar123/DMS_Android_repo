package com.scorg.dms.util;

/**
 * @author Sandeep Bahalkar
 */
public class DmsConstants {
    public static final String DMS_LOG_FOLDER = "PAL_LOG";
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
    public static final int NO_OF_TIMES_HIT=15; //if dashboad or report status not=generated

    // Connection codes
    public static final int REGISTRATION_CODE = 0;
    public static final int NORMAL_LOGIN_CODE = 1;
    public static final int SOCIAL_LOGIN_CODE = 2;
    public static final int REFRESH_TOKEN_CODE = 3;

    public static final int LOGOUT_CODE       = 4;
    public static final int TAG_TEST_CODE = 5;
    public static final int GET_ATTEMPT_ID_CODE = 6;
    public static final int GET_ATTEMPT_DATA_CODE = 7;
    public static final int GET_TEST_DETAIL_CODE = 8;
    public static final int GET_DASHBOARD_USER_STATS_CODE = 9;
    public static final int GET_DASHBOARD_USER_TAG_STATS_CODE = 10;
    public static final int GET_DASHBOARD_GENERIC_NOTIFICATION_CODE = 11;
//    public static final int GET_DASHBOARD_PERFORMANCE_HISTORY_DATA_CODE = 12;
    public static final int GET_RECOMMENDATION_DATA_CODE = 13;
    public static final int RESET_PASSWORD = 14;
    public static final int RESET_PASSWORD_SUBMIT = 15;
    public static final int GET_TEST_DETAIL_CODE_SHORT = 16;

    public static final int GET_PREVIOUS_ATTEMPTS_CODE = 17;
    public static final int CONTENT_TAG_CODE_PHYSICS = 18;
    public static final int CONTENT_TAG_CODE_CHEMISTRY = 19;
    public static final int CONTENT_TAG_CODE_MATHS = 20;
    public static final int GET_REPORTDATA_CODE = 21;
    public static final int GET_RECOMMENDATION_IN_REPORT_CODE = 22;
    //3 series reserved for rss32
    public static final int GET_UPGRADE_CODE = 23;
    public static final int GET_PACKAGE_ASSIGN = 24;
    public static final int GET_MYACCOUNT_PROFILEDATA = 25;
    public static final int PATCH_MYACCOUNT_PROFILEDATA = 26;
    public static final int SET_CHANGEPASSWORD_DATA = 27;
    public static final int POST_UPLOADPROFILEIMAGE = 28;
    public static final int ALLINDIAMOCKTEST_LIVE_TESTCODE = 29;
    public static final int ALLINDIAMOCKTEST_UPCOMING_TESTCODE = 30;
    public static final int ALLINDIAMOCKTEST_MISSED_TESTCODE = 31;
    public static final int ALLINDIAMOCKTEST_PREVIOUSATTEMPTS_CODE = 32;
    public static final int LEADERBOARD_CODE = 33;
    public static final int ALLINDIAMOCKTEST_SETREMINDER_CODE = 34;
    public static final int ACTIVITIES_CODE = 35;

    public static final int TAG_CREATE_ASSESSMENT = 36;
    public static final int TAG_START_ASSESSMENT = 37;
    public static final int TAG_PAUSE_ASSESSMENT = 38;
    public static final int TAG_RESUME_ASSESSMENT = 39;
    public static final int TAG_SUBMIT_ASNWER_ASSESSMENT = 40;
//    public static final int PATCH_SENDREAD_NOTIFICATION = 41;
//    public static final int POST_SEND_ERROR = 42;
    public static final int VERSION_UPGRADE = 43;
    public static final int ACTIVATE_ACCOUNT = 44;
    public static final int REVIEW_QUESTION = 45;

    public static final int USER_ONBOARDING = 46;
//    public static final int INVITE_FRIENDS_CODE = 47;
    public static final int REFERRALS_CODE = 48;
    public static final int GET_OTP = 49;
    public static final int VERIFY_OTP = 50;
    public static final int GET_MODULE_DATA = 51;
    public static final int RESEND_INVITE_FRIENDS_CODE = 52;
    public static final int RESEND_ACTIVATION_MAIL = 53;
    public static final int CUSTOMTEST_PREVIOUSATTEMPTS_CODE = 54;
    public static final int PATCH_READ_NOTIFICATION_CODE = 55;
    public static final int DELETE_REVISION_QUESTION = 56;
    public static final int GET_DASHBOARD_USER_TAG_STATS_CODE_REVISIONCOUNT = 57;
    public static final int PATCH_EXAM_TYPE = 58;
    public static final int GET_DEFAULT_EXAM_TYPE_FOR_ACTIVTY = 59;
    public static final int GET_EXAM_DATA = 60;





}
