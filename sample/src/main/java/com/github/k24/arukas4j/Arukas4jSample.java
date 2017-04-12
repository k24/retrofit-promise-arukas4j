package com.github.k24.arukas4j;

import com.github.k24.arukas4j.api.AppsApi;
import com.github.k24.deferred.Deferred;
import com.github.k24.deferred.RxJava2DeferredFactory;
import com.github.k24.retrofit2.converter.jsonic.JsonicConverterFactory;
import com.github.k24.retrofit2.converter.success.Success;
import io.reactivex.schedulers.Schedulers;
import net.arnx.jsonic.JSON;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.annotation.Nonnull;
import javax.net.ssl.SSLSocketFactory;
import java.util.Arrays;

/**
 * Created by k24 on 2017/04/11.
 */
public class Arukas4jSample {
    public static void main(String[] args) {
        String apiToken = System.getenv("ARUKAS_JSON_API_TOKEN");
        String apiSecret = System.getenv("ARUKAS_JSON_API_SECRET");
        if (apiToken == null || apiSecret == null) {
            if (args.length < 3) {
                System.out.println("Set ApiToken,ApiSecret with args or ARUKAS_JSON_API_TOKEN/SECRET");
                return;
            }

            apiToken = args[0];
            apiSecret = args[1];
        }

        if (apiToken == null || apiToken.isEmpty()) {
            System.out.println("Set ApiToken with args[0] or ARUKAS_JSON_API_TOKEN");
            return;
        }
        if (apiSecret == null || apiSecret.isEmpty()) {
            System.out.println("Set ApiSecret with args[1] or ARUKAS_JSON_API_SECRET");
            return;
        }

        ArukasApiLoggingAgent apiAgent = ArukasApiLoggingAgent.create(new ArukasApiAgent.Config(JsonicConverterFactory.create(), new RxJava2DeferredFactory(Schedulers.io())),
                apiToken, apiSecret, HttpLoggingInterceptor.Level.HEADERS);

        try {
            apiAgent.appsApi().apps()
                    .then(new Deferred.OnResolved<AppsApi.AppsListResponse, Success>() {
                        @Override
                        public Success onResolved(AppsApi.AppsListResponse appsListResponse) throws Exception {
                            System.out.println(JSON.encode(appsListResponse, true));
                            return Success.SUCCESS;
                        }
                    }, new Deferred.OnRejected<Success>() {
                        @Override
                        public Success onRejected(@Nonnull Throwable throwable) throws Exception {
                            throwable.printStackTrace(System.out);
                            return Success.SUCCESS;
                        }
                    }).waitForCompletion(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
