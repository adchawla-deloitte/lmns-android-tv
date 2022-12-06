package com.example.uipractice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageFullScreenActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_full_screen);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.image_full_screen_fragment, new ImageFullScreenFragment())
//                    .commitNow();
//        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.myLooper());
        // Once the executor parses the URL
        // and receives the image, handler will load it
        // in the ImageView
        //val handler = Handler(Looper.getMainLooper())

        final Movie movie =
                (Movie) getIntent().getSerializableExtra(DetailsActivity.MOVIE);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ImageView i = (ImageView)findViewById(R.id.imageView);
                    Log.d("IMAGEPATH", movie.getCardImageUrl().toString());
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(movie.getCardImageUrl()).getContent());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            i.setImageBitmap(bitmap);
                        }
                    });



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }
}
