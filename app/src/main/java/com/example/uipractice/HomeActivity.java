package com.example.uipractice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeActivity extends FragmentActivity {
    String url1 = "https://github.com/adhyanchawla/banner-repo/blob/master/LOCAL%20NETWORK%20MEDIA%20SCTREAMING%20(1)_page-0001.jpg?raw=true";
    String url2 = "https://github.com/adhyanchawla/banner-repo/blob/master/LOCAL%20NETWORK%20MEDIA%20SCTREAMING%20(2)_page-0001.jpg?raw=true";
    String url3 = "https://github.com/adhyanchawla/banner-repo/blob/master/LOCAL%20NETWORK%20MEDIA%20SCTREAMING%20(3)_page-0001.jpg?raw=true";
    String url4 = "https://github.com/adhyanchawla/banner-repo/blob/master/LOCAL%20NETWORK%20MEDIA%20SCTREAMING%20(4)_page-0001.jpg?raw=true";
    ProgressBar prg;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MainActivity.transitionToast.cancel();
        setContentView(R.layout.dashboard_main);
        prg = (ProgressBar) findViewById(R.id.progressBar1);
        if(prg.getVisibility() != View.VISIBLE){ // check if it is visible
            prg.setVisibility(View.VISIBLE); // if not set it to visible
//                            item.setVisibility(View.VISIBLE); // use 1 or 2 as parameters.. arg0 is the view(your button) from the onclick listener
        }


        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);

        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));
        sliderDataArrayList.add(new SliderData(url4));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // we are creating array list for storing our image urls.


                // below method is used to set auto cycle direction in left to
                // right direction you can change according to requirement.
                sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

                // below method is used to
                // setadapter to sliderview.
                sliderView.setSliderAdapter(adapter);

                // below method is use to set
                // scroll time in seconds.
                sliderView.setScrollTimeInSec(5);



                // to set it scrollable automatically
                // we use below method.
                sliderView.setAutoCycle(true);

                // to start autocycle below method is used.
                sliderView.startAutoCycle();
            }
        });

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                prg.setVisibility(View.INVISIBLE);
                //Second fragment after 5 seconds appears
                if(savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_browse_fragment, new HomeFragment())
                            .commit();
                }
            }
        };
        handler.postDelayed(runnable, 3000);
    }
}
