package com.example.uipractice;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;

public class DirectoriesActivity extends FragmentActivity {
    ProgressBar prg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MainActivity.transitionToast.cancel();
        setTitle(getResources().getString(R.string.app_name));
        Log.d("DETAILS", "WE ARE IN DETAILS ACTIVITY");
        setContentView(R.layout.directories_main);
        prg = (ProgressBar) findViewById(R.id.progressBar1);
        if(prg.getVisibility() != View.VISIBLE){ // check if it is visible
            prg.setVisibility(View.VISIBLE); // if not set it to visible
//                            item.setVisibility(View.VISIBLE); // use 1 or 2 as parameters.. arg0 is the view(your button) from the onclick listener
        }


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                prg.setVisibility(View.INVISIBLE);
                //Second fragment after 5 seconds appears
                if(savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_browse_fragment, new DirectoriesFragment())
                            .commit();
                }
            }
        };
        handler.postDelayed(runnable, 3000);
    }

}
