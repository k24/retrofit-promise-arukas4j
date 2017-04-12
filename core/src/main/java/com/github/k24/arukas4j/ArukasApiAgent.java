package com.github.k24.arukas4j;

import com.github.k24.arukas4j.api.AppsApi;
import com.github.k24.arukas4j.api.ContainersApi;
import com.github.k24.deferred.Deferred;
import com.github.k24.retrofit2.adapter.promise.PromiseCallAdapterFactory;
import com.github.k24.retrofit2.converter.success.SuccessConverterFactory;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by k24 on 2017/04/10.
 */
@SuppressWarnings("WeakerAccess")
public class ArukasApiAgent {
    public static final String URL_API_KEYS = "https://app.arukas.io/settings/api-keys";
    public static final String URL_BASE = "https://app.arukas.io/api/";

    private final Retrofit retrofit;

    public ArukasApiAgent(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public static class Config {
        final Converter.Factory converterFactory;
        final Deferred.Factory deferredFactory;

        public Config(Converter.Factory converterFactory, Deferred.Factory deferredFactory) {
            this.converterFactory = converterFactory;
            this.deferredFactory = deferredFactory;
        }
    }

    @Nonnull
    public static ArukasApiAgent create(@Nonnull Config config, @Nonnull String apiToken, @Nonnull String apiSecret) {
        return new ArukasApiAgent(retrofitBuilder(config)
                .client(okHttpClientBuilder(apiToken, apiSecret).build())
                .build());
    }

    protected static Retrofit.Builder retrofitBuilder(Config config) {
        return new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addCallAdapterFactory(PromiseCallAdapterFactory.create(config.deferredFactory))
                .addConverterFactory(SuccessConverterFactory.create())
                .addConverterFactory(config.converterFactory);
    }

    protected static OkHttpClient.Builder okHttpClientBuilder(final String apiToken, final String apiSecret) {
        return new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .cipherSuites(CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384)
                        .supportsTlsExtensions(true)
                        .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_3)
                        .build()))
                .addInterceptor(new Interceptor() {
                    private final String credential = Credentials.basic(apiToken, apiSecret);

                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request request = new Request.Builder()
                                .url(originalRequest.url())
                                .addHeader("Authorization", credential)
                                .addHeader("Content-Type", "application/vnd.api+json")
                                .addHeader("Accept", "application/vnd.api+json")
                                .addHeader("User-Agent", "Arukas4j/" + BuildConfig.AGENT_VERSION)
                                .method(originalRequest.method(), originalRequest.body())
                                .build();
                        return chain.proceed(request);
                    }
                });
    }

    //region Apis
    private final Map<Class, Object> apis = new HashMap<>();

    @SuppressWarnings("unchecked")
    protected <T> T ensureApi(Class<T> apiClass) {
        synchronized (apis) {
            Object api = apis.get(apiClass);
            if (api == null) {
                api = retrofit.create(apiClass);
                apis.put(apiClass, api);
            }
            return (T) api;
        }
    }

    protected void clearApis() {
        synchronized (apis) {
            apis.clear();
        }
    }

    public AppsApi appsApi() {
        return retrofit.create(AppsApi.class);
    }

    public ContainersApi containersApi() {
        return retrofit.create(ContainersApi.class);
    }
    //endregion

    public static class BuildConfig {
        public static final String AGENT_VERSION;

        static {
            Properties props = new Properties();
            try (InputStream in = ArukasApiAgent.class.getResourceAsStream("/version.properties")){
                props.load(in);
            } catch (IOException e) {
                props.put("version", "error");
            }
            AGENT_VERSION = props.getProperty("version", "unknown");
        }
    }
}
