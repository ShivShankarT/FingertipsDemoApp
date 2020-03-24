package com.example.fingertipsdemoapp.remote;



import com.example.fingertipsdemoapp.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by esec-sruthi on 20/1/18.
 */

public class AppConfig {
    static OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS);
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url) {

        if (retrofit == null) {


            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            if (BuildConfig.DEBUG)
                okHttpClient.addInterceptor(interceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient.build())
                    .build();

        }
        return retrofit;
    }
}

