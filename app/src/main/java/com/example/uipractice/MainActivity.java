package com.example.uipractice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends FragmentActivity {
    Button send;
    public static Toast transitionToast;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
//        LoginActivity.getDirectories();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
//        getDirectories();
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_browse_fragment, new MainFragment())
//                    .commitNow();
//        }

        send = (Button) findViewById(R.id.get_started);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressBar p = (ProgressBar)findViewById(R.id.progressBar1);
                if(p.getVisibility() != View.VISIBLE){ // check if it is visible
                    p.setVisibility(View.VISIBLE); // if not set it to visible
                    send.setVisibility(View.VISIBLE); // use 1 or 2 as parameters.. arg0 is the view(your button) from the onclick listener
                }
//                transitionToast.makeText(MainActivity.this, R.string.loading, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(intent);
                finish();
            }

        });



    }





//    @Override
//    public void onClick(View view) {
////        Intent intent;
////        if(mSelectedMovie.getType() == 1) {
////            intent = new Intent(getActivity(), PlaybackActivity.class);
////        } else {
////            intent = new Intent(getActivity(), ImageFullScreenActivity.class);
////        }
////        intent.putExtra(DetailsActivity.MOVIE, mSelectedMovie);
////        startActivity(intent);
//    }
}