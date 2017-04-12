package com.github.k24.arukas4j;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by k24 on 2017/04/12.
 */
public class ArukasApiLoggingAgent extends ArukasApiAgent {

    public ArukasApiLoggingAgent(Retrofit retrofit) {
        super(retrofit);
    }

    public static ArukasApiLoggingAgent create(Config config, String apiToken, String apiSecret, HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(level);
        return new ArukasApiLoggingAgent(retrofitBuilder(config)
                .client(okHttpClientBuilder(apiToken, apiSecret)
                        .addInterceptor(loggingInterceptor)
                        .build())
                .build());
    }
}
