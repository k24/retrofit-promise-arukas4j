package com.github.k24.arukas4j.api;

import com.github.k24.arukas4j.model.App;
import com.github.k24.deferred.Deferred;
import com.github.k24.retrofit2.converter.success.Success;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

/**
 * Created by k24 on 2017/04/10.
 */
public interface AppsApi {
    @GET("apps")
    Deferred.Promise<AppsListResponse> apps();

    @GET("apps/{apps_id}")
    Deferred.Promise<AppsSingleResponse> apps(@Path("apps_id") String appsId);

    @DELETE("apps/{apps_id}")
    Deferred.Promise<Success> deleteApps(@Path("apps_id") String appsId);

    class AppsListResponse {
        public List<App> data;
    }

    class AppsSingleResponse {
        public App data;
    }
}
