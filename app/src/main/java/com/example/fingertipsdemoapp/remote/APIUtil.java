package com.example.fingertipsdemoapp.remote;

import com.example.fingertipsdemoapp.ConfigURLs;
import com.example.fingertipsdemoapp.ConfigureURLS;
import com.example.fingertipsdemoapp.remote.AppConfig;

/**
 * Created by esec-sruthi on 20/1/18.
 */

public class APIUtil {

    public static final String BASE_URL = ConfigureURLS.BaseUrl;
    private static ConfigURLs configURLs;

    public APIUtil() {
    }

    public static ConfigURLs appConfig() {
        if (configURLs == null)
            configURLs = AppConfig.getClient(BASE_URL).create(ConfigURLs.class);
        return configURLs;
    }
}
