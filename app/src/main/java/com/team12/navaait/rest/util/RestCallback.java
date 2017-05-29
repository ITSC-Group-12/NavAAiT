package com.team12.navaait.rest.util;

import com.team12.navaait.rest.model.RestError;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by Sam on 5/29/2017.
 */

public abstract class RestCallback<T> implements Callback<T> {

    public abstract void failure(RestError restError);

    @Override
    public void failure(RetrofitError error) {

        RestError restError = (RestError) error.getBodyAs(RestError.class);

        if (restError != null) {
            failure(restError);
        } else {
            failure(new RestError(error.getMessage()));
        }

    }
}
