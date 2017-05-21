package com.team12.navaait;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText firstname;
    private EditText lastname;
    String id;

    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://192.168.43.35:8080").build();
    NavApiClient navApiClient =
            restAdapter.create(NavApiClient.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // TODO check sharedPref for User

        firstname = (EditText) findViewById(R.id.first_name);
        lastname = (EditText) findViewById(R.id.last_name);


        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Button mRegisterButton = (Button) findViewById(R.id.btn_register);
        mRegisterButton.setOnClickListener(this);

    }

    private void createAccount(String deviceId, final String firstName, String lastName) {

        try {
            Callback<String> callback = new Callback<String>() {

                @Override
                public void success(String s, retrofit.client.Response response) {
                    Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error != null) {
//                        Log.e("TAG", error.getBody().toString());
                        error.printStackTrace();
                    }
                }
            };

            navApiClient.regUser(deviceId, firstName, lastName, callback);
        } catch (Exception ex) {
            Log.d("TAG", "Not successful");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                if (firstname.getText().toString().isEmpty()) {
                    firstname.setError("Please provide First Name");
                } else if (lastname.getText().toString().isEmpty()) {
                    lastname.setError("Please provide Last Name");
                } else {
                    createAccount(id, firstname.getText().toString(), lastname.getText().toString());
                    SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.loginTAG, MODE_PRIVATE);
                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putBoolean(MainActivity.loginTAG, true);
                    e.apply();

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
