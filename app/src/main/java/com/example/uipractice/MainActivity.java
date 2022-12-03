package com.example.uipractice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends FragmentActivity {
    Button send;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_browse_fragment, new MainFragment())
//                    .commitNow();
//        }

        send = (Button) findViewById(R.id.go);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NextActivity.class);
                MainActivity.this.startActivity(intent);
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