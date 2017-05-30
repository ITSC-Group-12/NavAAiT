package com.team12.navaait.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sam on 5/28/2017.
 */

public class SharedPref {

    public static final String FILE = "NavAAiT";

    // KEYS
    public static final String USER_FIRST_NAME = "USER_FIRST_NAME";
    public static final String USER_LAST_NAME = "USER_LAST_NAME";
    public static final String USER_DEVICE_ID = "USER_DEVICE_ID";
    public static final String USER_VISIBILITY = "USER_VISIBILITY";
    public static final String MAP_VERSION = "MAP_VERSION";

    public static String getStringPref(@NonNull Context context, @NonNull String pref) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, MODE_PRIVATE);
        return sharedPreferences.getString(pref, "");
    }

    public static boolean getBooleanPref(@NonNull Context context, @NonNull String pref) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, MODE_PRIVATE);
        return sharedPreferences.getBoolean(pref, false);
    }

    public static void putStringPref(@NonNull Context context, @NonNull String pref, @NonNull String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, MODE_PRIVATE);
        sharedPreferences.edit().putString(pref, value).apply();
    }

    public static void putBooleanPref(@NonNull Context context, @NonNull String pref, @NonNull boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(pref, value).apply();
    }

    public static void clearPrefs(@NonNull Context context) {
        context.getSharedPreferences(FILE, MODE_PRIVATE).edit().clear().commit();
    }


}
