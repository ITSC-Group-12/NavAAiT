package com.team12.navaait;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.team12.navaait.services.UserService;
import com.team12.navaait.util.DeviceInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.first_name)
    EditText firstname;
    @BindView(R.id.last_name)
    EditText lastname;
    @BindView(R.id.btn_register)
    Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        if (UserService.checkAuth(getApplicationContext()) && UserService.checkOfflineAuth(getApplicationContext())) {
            startHomeActivity();
        }
    }

    @OnClick(R.id.btn_register)
    public void register() {
        if (firstname.getText().toString().isEmpty()) {
            firstname.setError("Please provide First Name");
        } else if (lastname.getText().toString().isEmpty()) {
            lastname.setError("Please provide Last Name");
        } else {
            if (UserService.createAccount(getApplicationContext(), firstname.getText().toString(), firstname.getText().toString(), DeviceInfo.getDeviceID())) {
                startHomeActivity();
            }
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

