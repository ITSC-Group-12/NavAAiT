package com.team12.navaait;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team12.navaait.services.UserService;
import com.team12.navaait.util.DeviceInfo;
import com.team12.navaait.util.SharedPref;

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
//        SharedPref.clearPrefs(getApplicationContext());
        UserService.checkAuth(getApplicationContext(), this);
        UserService.checkOfflineAuth(getApplicationContext(), this);

    }

    @OnClick(R.id.btn_register)
    public void register() {
        if (firstname.getText().toString().isEmpty()) {
            firstname.setError("Please provide First Name");
        } else if (lastname.getText().toString().isEmpty()) {
            lastname.setError("Please provide Last Name");
        } else {
            UserService.createAccount(getApplicationContext(), firstname.getText().toString(), firstname.getText().toString(), DeviceInfo.getDeviceID(), this);
        }
    }

    public void startHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

