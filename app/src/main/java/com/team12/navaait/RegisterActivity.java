package com.team12.navaait;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team12.navaait.domain.User;
import com.team12.navaait.rest.NavRestClient;
import com.team12.navaait.rest.service.ApiService;
import com.team12.navaait.util.DeviceInfo;
import com.team12.navaait.util.SharedPref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.first_name)
    EditText firstname;
    @BindView(R.id.last_name)
    EditText lastname;
    @BindView(R.id.btn_register)
    Button mRegisterButton;

    String id;

    ApiService apiService = new NavRestClient().getApiService();

    @OnClick(R.id.btn_register)
    public void register() {
        if (firstname.getText().toString().isEmpty()) {
            firstname.setError("Please provide First Name");
        } else if (lastname.getText().toString().isEmpty()) {
            lastname.setError("Please provide Last Name");
        } else {
            createAccount();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        SharedPref.clearPrefs(getApplicationContext());
        String Fvalue = SharedPref.getStringPref(getApplicationContext(), SharedPref.USER_FIRST_NAME);
        String Lvalue = SharedPref.getStringPref(getApplicationContext(), SharedPref.USER_LAST_NAME);
        String Ivalue = SharedPref.getStringPref(getApplicationContext(), SharedPref.USER_DEVICE_ID);

        checkAuth(Fvalue, Lvalue, Ivalue);

        if (!Fvalue.isEmpty() && !Lvalue.isEmpty() && !Ivalue.isEmpty()) {
            startHomeActivity();
        }

        id = DeviceInfo.getDeviceID();


    }


    private void createAccount() {

        User user = new User(null, firstname.getText().toString(), lastname.getText().toString(), id, true, null);
        apiService.registerUser(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Toast.makeText(RegisterActivity.this, "Welcome " + user.getFirstName(), Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences(SharedPref.FILE, MODE_PRIVATE);
                SharedPreferences.Editor e = sharedPreferences.edit();
                e.putString(SharedPref.USER_FIRST_NAME, firstname.getText().toString());
                e.putString(SharedPref.USER_LAST_NAME, lastname.getText().toString());
                e.putString(SharedPref.USER_DEVICE_ID, id);
                e.apply();
                startHomeActivity();

            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    startHomeActivity();
//                    Log.e("TAG", error.getBody().toString());
                    Toast.makeText(RegisterActivity.this, "Connection Error, Please try again ", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }

        });

    }

    private void checkAuth(String firstname, String lastname, String deviceId) {
        User user = new User(null, firstname, lastname, deviceId, true, null);
        apiService.auth(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Toast.makeText(RegisterActivity.this, "User is Registered", Toast.LENGTH_SHORT).show();
                startHomeActivity();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
//                    Log.e("TAG", error.getBody().toString());
                    error.printStackTrace();
                }
            }

        });

    }


    private void startHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

