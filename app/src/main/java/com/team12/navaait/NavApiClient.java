package com.team12.navaait;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Blen on 5/9/2017.
 */
public interface NavApiClient {
    @FormUrlEncoded
    @POST("/registerUser")
    void regUser(@Field("deviceId") String deviceId,
                 @Field("firstName") String firstName,
                 @Field("lastName") String lastName,

                 Callback<String> response);

    @FormUrlEncoded
    @POST("/checkPerson")
    void checkUser(@Field("firstName") String firstName,
                   @Field("lastName") String lastname,
                   Callback<String> response);
}
