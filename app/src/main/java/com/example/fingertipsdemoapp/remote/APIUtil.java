package com.example.fingertipsdemoapp.remote;

import android.util.Log;

import com.example.fingertipsdemoapp.ConfigURLs;

/**
 * Created by esec-sruthi on 20/1/18.
 */

public class APIUtil {

    public static boolean isTesting = true;
    public static String BASE_URL = "http://test.login.fingertips.in/api/";
    private static ConfigURLs configURLs;

    public APIUtil() {

    }

    public static ConfigURLs appConfig() {
        if (configURLs == null)
            regenConfigURLs();
        return configURLs;
    }

    public static ConfigURLs regenConfigURLs() {
        BASE_URL = isTesting ? "http://test.login.fingertips.in/api/" : "http://login.fingertips.in/api/";
        Log.i("hjh", "regenConfigURLs: "+BASE_URL);
        AppConfig.clear();
        configURLs = AppConfig.getClient(BASE_URL).create(ConfigURLs.class);
        return configURLs;
    }

}
