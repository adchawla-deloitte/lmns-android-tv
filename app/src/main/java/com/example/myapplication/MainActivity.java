package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends FragmentActivity {

    public MainActivity() throws IOException {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainFragment.getDirectories();

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Second fragment after 5 seconds appears
                if(savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_browse_fragment, new MainFragment())
                            .commit();
                }

            }
        };

        handler.postDelayed(runnable, 10000);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                 // MainFragment.getDirectories();
////                Intent i = new Intent(MainActivity.this,MainActivity.class);
////                startActivity(i);
//            }
//        }, 5000);

        //mainHandler.post(runnable);
        //getDirectoryContent();


    }

//    public void getDirectoryContent() {
//        final Call<DirectoryContent> directoryContent = DirectoryService.service.getDirectoryContent(21);
//        directoryContent.enqueue(new Callback<DirectoryContent>() {
//
//            @Override
//            public void onResponse(Call<DirectoryContent> call, Response<DirectoryContent> response) {
//                final DirectoryContent content;
//                content = response.body();
//                List<String> res = content.directoryContent;
//                List<String> finalRes = new ArrayList<>();
////                assert content != null;
//                final Call<ServedDirectoryResponse> servedDirectoryResponseCall = DirectoryService.service.serveDirectory(21);
//                servedDirectoryResponseCall.enqueue(new Callback<ServedDirectoryResponse>() {
//                    @Override
//                    public void onResponse(Call<ServedDirectoryResponse> call, Response<ServedDirectoryResponse> response) {
//                        ServedDirectoryResponse resp = response.body();
//                        for(String r: res) {
//                            String[]arr = r.split("\\.");
//                            int len = arr.length;
//                            if(arr[len - 1].equals("jpg") || arr[len - 1].equals("png") || arr[len - 1].equals("mp3") || arr[len - 1].equals("mp4"))
//                            finalRes.add("http://" + resp.serverip + "/" + r);
//                        }
//                        Log.d("DIRECTORY", finalRes.toString());
//                    }
//
//                    @Override
//                    public void onFailure(Call<ServedDirectoryResponse> call, Throwable t) {
//                        Log.d("ServedDirectory", "Unable to retrieve served directory", t);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(Call<DirectoryContent> call, Throwable t) {
//                Log.d("DIRECTORY", "Could not fetch", t);
//            }
//        });
//    }
}