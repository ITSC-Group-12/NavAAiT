package com.team12.navaait.rest;

import com.team12.navaait.rest.service.ApiService;

import retrofit.RestAdapter;

/**
 * Created by Sam on 5/29/2017.
 */

public class NavRestClient {

    private static final String BASE_URL = "http://192.168.137.1:8086";
    private ApiService apiService;

    public NavRestClient() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .build();

        apiService = restAdapter.create(ApiService.class);

    }

    public ApiService getApiService() {
        return apiService;
    }
}
