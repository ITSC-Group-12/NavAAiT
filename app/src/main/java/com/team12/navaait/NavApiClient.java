package com.team12.navaait;

import com.team12.navaait.domain.Map;
import com.team12.navaait.domain.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Blen on 5/9/2017.
 */
public interface NavApiClient {


    @POST("/checkVersion")
    void checkVersion(Callback<Map> response);

    @POST("/api/user/register")
    void RegisterUser(@Body User user, Callback<User> response);

    @POST("/api/user/auth")
    void checkUser(@Body User user, Callback<User> response);

    @POST("/api/user/toggleVisiblity")
    void checkVisibility(@Body User user, Callback<User> response);
}
