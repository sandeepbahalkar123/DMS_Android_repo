package com.scorg.dms.util;

/**
 * Created by Sandeep Bahalkar
 */
public class Config {

    public static final String TOKEN_TYPE = "Bearer";

    public static boolean DEV_BUILD = true;
    private static String BASE_URL;

    static {
        if (DEV_BUILD) {

            //  BASE_URL = "http://192.168.0.25:88/mobapi/api/";
            BASE_URL = "http://192.168.0.25:81/api/";
        } else {
            //TODO: production base url will be added once its ready
            BASE_URL = "";
        }

    }

    //Declared all URL used in app here
    public static final String URL_LOGIN = BASE_URL + "userLogin";
    public static final String URL_PATIENT_LIST = BASE_URL + "result/ShowSearchResults";
    public static final String URL_ANNOTATIONS_LIST = BASE_URL + "documenttype/getAnnotations";
    public static final String URL_GET_ARCHIVED_LIST = BASE_URL + "getArchived";

}
