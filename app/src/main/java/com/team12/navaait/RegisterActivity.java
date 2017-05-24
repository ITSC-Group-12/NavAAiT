package com.team12.navaait;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.team12.navaait.domain.User;

import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
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

    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://192.168.137.194:8086").build();
    NavApiClient navApiClient = restAdapter.create(NavApiClient.class);

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

        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        String Fvalue = sharedPreferences.getString("first name","");
        String Lvalue = sharedPreferences.getString("last name","");
        String Ivalue = sharedPreferences.getString("Device id","");

        checkAuth(Fvalue,Lvalue,Ivalue);

        if(!Fvalue.isEmpty() && !Lvalue.isEmpty() && !Ivalue.isEmpty()){
            startHomeActivity();
        }

        id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


    }


    private void createAccount() {

        User user = new User(null, firstname.getText().toString(), lastname.getText().toString(), id, true, null);
        navApiClient.RegisterUser(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Toast.makeText(RegisterActivity.this, "Welcome " + user.getFirstName(), Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
                SharedPreferences.Editor e = sharedPreferences.edit();
                e.putString("first name", firstname.getText().toString());
                e.putString("last name",lastname.getText().toString());
                e.putString("Device id", id);
                e.apply();
                startHomeActivity();

            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Log.e("TAG", error.getBody().toString());
                    Toast.makeText(RegisterActivity.this, "Connection Error, Please try again " , Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }

        });

    }
    private void checkAuth(String firstname, String lastname, String deviceId){
        User user = new User(null, firstname , lastname, deviceId, true, null);
        navApiClient.checkUser(user, new Callback<User>() {
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

