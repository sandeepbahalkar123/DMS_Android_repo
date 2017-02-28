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

            BASE_URL = "http://192.168.0.25:88/mobapi/api/";
        } else {
            //TODO: production base url will be added once its ready
            BASE_URL = "";
        }

    }

    //Declared all URL used in app here
    public static final String URL_LOGIN = BASE_URL + "userLogin";
    public static final String URL_SHOW_SEARCHRESULT = BASE_URL + "result/ShowSearchResults";


}
