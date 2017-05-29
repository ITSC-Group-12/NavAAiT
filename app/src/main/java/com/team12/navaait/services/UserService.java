package com.team12.navaait.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.team12.navaait.MainActivity;
import com.team12.navaait.domain.Location;
import com.team12.navaait.domain.User;
import com.team12.navaait.rest.NavRestClient;
import com.team12.navaait.rest.model.RestError;
import com.team12.navaait.rest.service.ApiService;
import com.team12.navaait.rest.util.RestCallback;
import com.team12.navaait.util.SharedPref;

import retrofit.Callback;
import retrofit.RetrofitError;
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

    public static boolean checkAuth(final Context context) {

        final boolean[] isAuth = new boolean[1];

        //  GET USER INFO
        String firstName = SharedPref.getStringPref(context, SharedPref.USER_FIRST_NAME);
        String lastName = SharedPref.getStringPref(context, SharedPref.USER_LAST_NAME);
        String deviceId = SharedPref.getStringPref(context, SharedPref.USER_DEVICE_ID);
        boolean visibility = SharedPref.getBooleanPref(context, SharedPref.USER_VISIBILITY);

        User user = new User(null, firstName, lastName, deviceId, visibility, null);

        apiService.auth(user, new RestCallback<User>() {
            @Override
            public void failure(RestError restError) {
                isAuth[0] = restError.getCode() != 404;
            }

            @Override
            public void success(User user, Response response) {
                Toast.makeText(context, "User is Registered", Toast.LENGTH_SHORT).show();
                isAuth[0] = true;
            }
        });

        return isAuth[0];
    }

    public static boolean checkOfflineAuth(final Context context) {

        boolean isAuth = false;

        //  GET USER INFO
        String firstName = SharedPref.getStringPref(context, SharedPref.USER_FIRST_NAME);
        String lastName = SharedPref.getStringPref(context, SharedPref.USER_LAST_NAME);
        String deviceId = SharedPref.getStringPref(context, SharedPref.USER_DEVICE_ID);

        if (!firstName.isEmpty() && !lastName.isEmpty() && !deviceId.isEmpty()) {
            isAuth = true;
        }

        return isAuth;
    }

    public static boolean createAccount(final Context context, final String firstName, final String lastName, final String deviceId) {

        final boolean[] isCreated = new boolean[1];

        User user = new User(null, firstName, lastName, deviceId, true, null);

        apiService.registerUser(user, new RestCallback<User>() {
            @Override
            public void failure(RestError restError) {
                // isCreated[0] = true;
                Toast.makeText(context, "Connection Error, Please try again ", Toast.LENGTH_LONG).show();
                isCreated[0] = false;
            }

            @Override
            public void success(User user, Response response) {
                Toast.makeText(context, "Welcome " + user.getFirstName(), Toast.LENGTH_SHORT).show();
                SharedPref.putStringPref(context, SharedPref.USER_FIRST_NAME, firstName);
                SharedPref.putStringPref(context, SharedPref.USER_LAST_NAME, lastName);
                SharedPref.putStringPref(context, SharedPref.USER_DEVICE_ID, deviceId);
                SharedPref.putBooleanPref(context, SharedPref.USER_VISIBILITY, user.isVisible());
                isCreated[0] = true;
            }

        });

        return isCreated[0];
    }

    public static boolean toggleVisibility(final Context context) {

        final boolean[] isVisibile = new boolean[1];

        //  GET USER INFO
        String firstName = SharedPref.getStringPref(context, SharedPref.USER_FIRST_NAME);
        String lastName = SharedPref.getStringPref(context, SharedPref.USER_LAST_NAME);
        String deviceId = SharedPref.getStringPref(context, SharedPref.USER_DEVICE_ID);
        final boolean visibility = SharedPref.getBooleanPref(context, SharedPref.USER_VISIBILITY);

        User user = new User(null, firstName, lastName, deviceId);

        apiService.toggleVisibility(user, new RestCallback<User>() {
            @Override
            public void failure(RestError restError) {
                Toast.makeText(context, "Error Connecting to Server", Toast.LENGTH_SHORT).show();
                isVisibile[0] = visibility;
            }

            @Override
            public void success(User user, Response response) {
                SharedPref.putBooleanPref(context, SharedPref.USER_VISIBILITY, user.isVisible());
                isVisibile[0] = user.isVisible();
            }
        });

        return isVisibile[0];
    }
}
