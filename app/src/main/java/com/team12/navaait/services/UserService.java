package com.team12.navaait.services;

import android.content.Context;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.team12.navaait.RegisterActivity;
import com.team12.navaait.domain.Location;
import com.team12.navaait.domain.User;
import com.team12.navaait.rest.NavRestClient;
import com.team12.navaait.rest.model.RestError;
import com.team12.navaait.rest.service.ApiService;
import com.team12.navaait.rest.util.RestCallback;
import com.team12.navaait.util.DataHelper;
import com.team12.navaait.util.SharedPref;

import java.util.HashSet;
import java.util.Set;

import retrofit.client.Response;

/**
 * Created by Sam on 5/29/2017.
 */

public class UserService {

    private static final String USER_TAG = "UserService";

    private static ApiService apiService = new NavRestClient().getApiService();

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

    public static void search(final Context context, final String searchKey, final DataHelper.OnFindSuggestionsListener listener) {

        apiService.search(searchKey, new RestCallback<Set<User>>() {
            @Override
            public void failure(RestError restError) {
                Set<User> users = new HashSet<User>();
                DataHelper.findSuggestions(context, searchKey, 5, users, listener);
            }

            @Override
            public void success(Set<User> users, Response response) {

                DataHelper.findSuggestions(context, searchKey, 5, users, listener);
            }
        });

    }

    public static void checkAuth(final Context context, final RegisterActivity registerActivity) {

        //  GET USER INFO
        String firstName = SharedPref.getStringPref(context, SharedPref.USER_FIRST_NAME);
        String lastName = SharedPref.getStringPref(context, SharedPref.USER_LAST_NAME);
        String deviceId = SharedPref.getStringPref(context, SharedPref.USER_DEVICE_ID);
        boolean visibility = SharedPref.getBooleanPref(context, SharedPref.USER_VISIBILITY);

        User user = new User(null, firstName, lastName, deviceId, visibility, null);

        apiService.auth(user, new RestCallback<User>() {
            @Override
            public void failure(RestError restError) {

            }

            @Override
            public void success(User user, Response response) {
                Toast.makeText(context, "User is Registered", Toast.LENGTH_SHORT).show();
                registerActivity.startHomeActivity();
            }
        });
    }

    public static void checkOfflineAuth(final Context context, RegisterActivity registerActivity) {

        //  GET USER INFO
        String firstName = SharedPref.getStringPref(context, SharedPref.USER_FIRST_NAME);
        String lastName = SharedPref.getStringPref(context, SharedPref.USER_LAST_NAME);
        String deviceId = SharedPref.getStringPref(context, SharedPref.USER_DEVICE_ID);

        if (!firstName.isEmpty() && !lastName.isEmpty() && !deviceId.isEmpty()) {
            registerActivity.startHomeActivity();
        }
    }

    public static void createAccount(final Context context, final String firstName, final String lastName, final String deviceId, final RegisterActivity registerActivity) {

        User user = new User(null, firstName, lastName, deviceId, true, null);

        apiService.registerUser(user, new RestCallback<User>() {
            @Override
            public void failure(RestError restError) {
                Toast.makeText(context, "Connection Error, Please try again ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void success(User user, Response response) {
                Toast.makeText(context, "Welcome " + user.getFirstName(), Toast.LENGTH_SHORT).show();
                SharedPref.putStringPref(context, SharedPref.USER_FIRST_NAME, firstName);
                SharedPref.putStringPref(context, SharedPref.USER_LAST_NAME, lastName);
                SharedPref.putStringPref(context, SharedPref.USER_DEVICE_ID, deviceId);
                SharedPref.putBooleanPref(context, SharedPref.USER_VISIBILITY, user.isVisible());
                registerActivity.startHomeActivity();
            }

        });
    }

    public static void updateName(final Context context ,final String firstName, final String lastName, final String deviceId){

        User user = new User(null, firstName, lastName, deviceId, true, null);

        apiService.updateName(user, new RestCallback<User>() {
            @Override
            public void failure(RestError restError) {
                Toast.makeText(context, "Connection Error, Please try again ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void success(User user, Response response) {
                Toast.makeText(context, "You have successfully updated your name " + user.getFirstName(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public static void toggleVisibility(final Context context, final Switch aSwitch) {

        //  GET USER INFO
        String firstName = SharedPref.getStringPref(context, SharedPref.USER_FIRST_NAME);
        String lastName = SharedPref.getStringPref(context, SharedPref.USER_LAST_NAME);
        String deviceId = SharedPref.getStringPref(context, SharedPref.USER_DEVICE_ID);

        User user = new User(null, firstName, lastName, deviceId);

        apiService.toggleVisibility(user, new RestCallback<User>() {
            @Override
            public void failure(RestError restError) {
                Toast.makeText(context, "Error Connecting to Server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(User user, Response response) {
                SharedPref.putBooleanPref(context, SharedPref.USER_VISIBILITY, user.isVisible());
                aSwitch.setChecked(user.isVisible());
            }
        });
    }
}
