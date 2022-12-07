package com.example.uipractice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends FragmentActivity {
    Button send;
    EditText username, password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        //getDirectories();

        send = (Button) findViewById(R.id.go);
        username = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, DirectoriesActivity.class);
//                LoginActivity.this.startActivity(intent);
                if(TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Username / Password is empty", Toast.LENGTH_LONG).show();
                } else {
                    login();
                    //Log.d("BASEURL", Constants.BASE_URL);
                   // getDirectories();
                }
            }

        });


    }

    public void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(password.getText().toString());
        loginRequest.setUsername(username.getText().toString());
        password.setText("");
        username.setText("");
        Call<LoginResponse> loginResponseCall = UserService.service.loginUser(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("LOGINRESPONSE", response.body().getToken().toString());
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    getIP(response.body().getToken().toString());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            getDirectories();
                            startActivity(new Intent(LoginActivity.this, DirectoriesActivity.class));
                        }
                    }, 700);
                } else {
//                    Log.d("FAILURE 1", t.toString());
                    Toast.makeText(LoginActivity.this, "Login Failed 1", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            startActivity(new Intent(getIntent()));
                        }
                    }, 700);

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("FAILURE 1", t.toString());
                Toast.makeText(LoginActivity.this, "Login Failed 2", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        startActivity(new Intent(getIntent()));
                    }
                }, 700);
            }
        });
    }

    public void getIP(String token) {
        Call<IpResponse> ipResponseCall = UserService.service.retrieveIp("Token " + token);
        ipResponseCall.enqueue(new Callback<IpResponse>() {
            @Override
            public void onResponse(Call<IpResponse> call, Response<IpResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("IPADDRESS", response.body().getIp().toString());
                    Constants.BASE_URL = "http://" + response.body().getIp() + ":8000";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getDirectories();
                        }
                    }, 100);

                }
            }

            @Override
            public void onFailure(Call<IpResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        startActivity(new Intent(getIntent()));
                    }
                }, 700);
                Log.d("IPFAILED", t.toString());
            }
        });
    }

    public static void getDirectories() {
        Call<List<DirectoryDataItem>> getDirectory = DirectoryService.service.getDirectory();
        getDirectory.enqueue(new Callback<List<DirectoryDataItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<DirectoryDataItem>> call, Response<List<DirectoryDataItem>> response) {
                DirectoryList.DIRECTORY_CATEGORY = response.body();

                for (DirectoryDataItem item : DirectoryList.DIRECTORY_CATEGORY) {
                    Log.d("DIRECTORYDATA", item.dir_name.toString());
                    String[] dirName = item.getDir_name().split("/");
                    item.setDir_name(dirName[dirName.length - 1]);
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<DirectoryDataItem>> call, Throwable t) {

                Log.d("DATAITEM", "No dataitem found", t);
            }
        });
    }
}
