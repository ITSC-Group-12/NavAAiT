package com.team12.navaait;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.team12.navaait.services.UserService;
import com.team12.navaait.util.DeviceInfo;
import com.team12.navaait.util.SharedPref;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        // for testing purposes only
//        SharedPref.putStringPref(getApplicationContext(), SharedPref.USER_FIRST_NAME, "Sam");
//        SharedPref.putStringPref(getApplicationContext(), SharedPref.USER_LAST_NAME, "Sam");
//        SharedPref.putStringPref(getApplicationContext(), SharedPref.USER_DEVICE_ID, "oneplus");
//        SharedPref.clearPrefs(getApplicationContext());
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
}

