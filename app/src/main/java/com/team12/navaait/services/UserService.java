package com.team12.navaait.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.team12.navaait.domain.Location;
import com.team12.navaait.domain.User;
import com.team12.navaait.rest.NavRestClient;
import com.team12.navaait.rest.model.RestError;
import com.team12.navaait.rest.service.ApiService;
import com.team12.navaait.rest.util.RestCallback;
import com.team12.navaait.util.SharedPref;

import retrofit.client.Response;

/**
 * Created by Sam on 5/29/2017.
 */

public class UserService {

    private static final String USER_TAG = "UserService";

    public static ApiService apiService = new NavRestClient().getApiService();


    public static void setLocation(final Context context, Location location) {

        //  GET USER INFO
        String firstName = SharedPref.getStringPref(context, SharedPref.USER_FIRST_NAME);
        String lastName = SharedPref.getStringPref(context, SharedPref.USER_LAST_NAME);
        String deviceId = SharedPref.getStringPref(context, SharedPref.USER_DEVICE_ID);
        boolean visibility = SharedPref.getBooleanPref(context, SharedPref.USER_VISIBILITY);

        User user = new User("", firstName, lastName, deviceId, visibility, location);

        apiService.setLocation(user, new RestCallback<User>() {
            @Override
            public void failure(RestError restError) {

                Toast.makeText(context, restError.getStrMessage() + " " + restError.getCode(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(User user, Response response) {
                Log.i(USER_TAG, "YAY! I KNOW WHERE I AM.");
                Toast.makeText(context, "YAY! I KNOW WHERE I AM.", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
