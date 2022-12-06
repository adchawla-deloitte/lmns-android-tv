package com.example.uipractice;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

public class FilesDisplayActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.app_name));
        Log.d("FILEDISPLAY", "WE ARE IN FILES DISPLAY ACTIVITY");
        setContentView(R.layout.files_display_main);


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Second fragment after 5 seconds appears
                if(savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_browse_fragment, new FilesDisplayFragment())
                            .commit();
                }
            }
        };
        handler.postDelayed(runnable, 5000);
    }
}
