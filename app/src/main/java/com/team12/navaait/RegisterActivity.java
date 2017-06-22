package com.team12.navaait;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team12.navaait.services.UserService;
import com.team12.navaait.util.DeviceInfo;
import com.team12.navaait.util.SharedPref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.first_name)
    EditText firstName;
    @BindView(R.id.last_name)
    EditText lastName;
    @BindView(R.id.btn_register)
    Button registerButton;

    // Permission List
    String[] reqPermission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int requestCode1 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        // for testing purposes only
        SharedPref.clearPrefs(getApplicationContext());
        SharedPref.putStringPref(getApplicationContext(), SharedPref.USER_FIRST_NAME, "Sam");
        SharedPref.putStringPref(getApplicationContext(), SharedPref.USER_LAST_NAME, "Sam");
        SharedPref.putStringPref(getApplicationContext(), SharedPref.USER_DEVICE_ID, "oneplus");
//        SharedPref.clearPrefs(getApplicationContext());

        // For API level 23+ request permission at runtime
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, reqPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            if (SharedPref.getStringPref(getApplicationContext(), SharedPref.MAP_ASSET_LOADED) == "") {

                copyAssets();
                SharedPref.putStringPref(getApplicationContext(), SharedPref.MAP_ASSET_LOADED, "loaded");
            }
        } else {
            // request permission
            ActivityCompat.requestPermissions(RegisterActivity.this, reqPermission, requestCode1);
        }

        UserService.checkAuth(getApplicationContext(), this);
        UserService.checkOfflineAuth(getApplicationContext(), this);
    }

    @OnClick(R.id.btn_register)
    public void register() {
        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("Please provide First Name");
        } else if (lastName.getText().toString().isEmpty()) {
            lastName.setError("Please provide Last Name");
        } else {
            UserService.createAccount(getApplicationContext(), firstName.getText().toString(), lastName.getText().toString(), DeviceInfo.getDeviceID(), this);
        }
    }

    public void startHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) {

                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open("5kilo.mmpk");
                    File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "ArcGIS");
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    File outFile = new File(Environment.getExternalStorageDirectory() + File.separator + "ArcGIS", "5kilo.mmpk");
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                } catch (IOException e) {
                    Log.e("tag", "Failed to copy asset file: " + "5kilo.mmpk", e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }


        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * Handle the permissions request response
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case requestCode1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (SharedPref.getStringPref(getApplicationContext(), SharedPref.MAP_ASSET_LOADED) == "") {

                        copyAssets();
                        SharedPref.putStringPref(getApplicationContext(), SharedPref.MAP_ASSET_LOADED, "loaded");
                    }
                } else {
                    // report to user that permission was denied
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.location_permission_denied),
                            Toast.LENGTH_SHORT).show();
                }
            }


        }
    }
}

