package com.team12.navaait.rest.service;

import com.team12.navaait.domain.Map;
import com.team12.navaait.domain.User;
import com.team12.navaait.rest.util.RestCallback;

import java.util.Set;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * Created by Blen on 5/9/2017.
 */
public interface ApiService {

    //  Map APIs
    @POST("/checkVersion")
    void checkVersion(RestCallback<Map> response);

    //  User APIs
    @POST("/api/user/register")
    void registerUser(@Body User user, RestCallback<User> response);

    @POST("/api/user/auth")
    void auth(@Body User user, RestCallback<User> response);

    @POST("/api/user/toggleVisiblity")
    void toggleVisibility(@Body User user, RestCallback<User> response);

    @PUT("/api/user/setLocation")
    void setLocation(@Body User user, RestCallback<User> response);

    @POST("/api/user/search")
    @FormUrlEncoded
    void search(@Field("searchKey") String searchKey, RestCallback<Set<User>> response);

    @POST("/api/user/updateName")
    void updateName(@Body User user, RestCallback<User> response);
}
