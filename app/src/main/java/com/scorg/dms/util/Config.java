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

    public static final String URL_REGISTER_USER = BASE_URL + "users/";


}
