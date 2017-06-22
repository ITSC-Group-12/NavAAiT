package com.team12.navaait.services;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.team12.navaait.domain.Map;
import com.team12.navaait.rest.NavRestClient;
import com.team12.navaait.rest.model.RestError;
import com.team12.navaait.rest.service.ApiService;
import com.team12.navaait.rest.util.RestCallback;
import com.team12.navaait.util.SharedPref;

import java.io.File;

import retrofit.client.Response;

/**
 * Created by Sam on 5/29/2017.
 */

public class MapService {

    private static final String MAP_TAG = "MapService";
    private static final String WRITE_LOCATION = "/sdcard/ArcGIS/5kilo.mmpk";

    private static ApiService apiService = new NavRestClient().getApiService();

    public static void checkVersion(final Context context, final ProgressBar progressBar) {

        apiService.checkVersion(new RestCallback<Map>() {
            @Override
            public void failure(RestError restError) {
                Toast.makeText(context, "Error with Connection!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(Map map, Response response) {
                Toast.makeText(context, map.getVersion(), Toast.LENGTH_SHORT).show();
                String version = map.getVersion();
                if (version.equals(SharedPref.getStringPref(context, SharedPref.MAP_VERSION))) {
                    Toast.makeText(context, "YOUR MAP IS ALREADY UPDATED!", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPref.putStringPref(context, SharedPref.MAP_VERSION, version);
                    downloadMMPK(context, map.getFileName(), progressBar);

                }
            }
        });

    }

    private static void downloadMMPK(final Context context, String downloadUrl, final ProgressBar progressBar) {
        Ion.with(context)
                .load(downloadUrl)
                // have a ProgressBar get updated automatically with the percent
                .progressBar(progressBar)
                // can also use a custom callback
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        progressBar.setVisibility(View.VISIBLE);
                        System.out.println("" + downloaded + " / " + total);
                    }
                })
                .write(new File(WRITE_LOCATION))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        if (e != null) {

//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("DOWNLOAD", e.getMessage());
                        }
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }
}
