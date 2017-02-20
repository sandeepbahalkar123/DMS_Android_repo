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

            BASE_URL = "https://qa-api.plancess.com/";
        } else {
            //TODO: production base url will be added once its ready
            BASE_URL = "https://api.plancess.com/";
        }

    }

    public static final String URL_REGISTER_USER = BASE_URL + "users/";


}
