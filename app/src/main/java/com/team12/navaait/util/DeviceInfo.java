package com.team12.navaait.util;

/**
 * Created by Sam on 5/29/2017.
 */

public class DeviceInfo {

    public static String getDeviceID() {

        StringBuilder sb = new StringBuilder();

        sb.append(android.os.Build.MANUFACTURER);
        sb.append(" ");
        sb.append(android.os.Build.MODEL);
        sb.append(" ");
        sb.append(android.os.Build.SERIAL);

        String unique = sb.toString();

        return unique;
    }

}
