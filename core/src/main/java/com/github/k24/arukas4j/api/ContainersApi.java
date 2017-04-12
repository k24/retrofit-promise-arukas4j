package com.github.k24.arukas4j.api;

import com.github.k24.arukas4j.model.Container;
import com.github.k24.deferred.Deferred;
import com.github.k24.retrofit2.converter.success.Success;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

/**
 * Created by k24 on 2017/04/10.
 */
public interface ContainersApi {
    @GET("containers")
    Deferred.Promise<List<Container>> containers();

    @POST("containers/{containers_id}/power")
    Deferred.Promise<Success> startContainer(@Path("containers_id") String containersId);

    @DELETE("containers/{containers_id}/power")
    Deferred.Promise<Success> stopContainer(@Path("containers_id") String containersId);
}
