package com.example.uipractice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends FragmentActivity {
    Button send;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        getDirectories();

        send = (Button) findViewById(R.id.go);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, DirectoriesActivity.class);
                LoginActivity.this.startActivity(intent);
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
