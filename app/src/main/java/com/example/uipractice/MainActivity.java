package com.example.uipractice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
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