package com.example.uipractice;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.example.uipractice.MovieList.dirSelected;
import static com.example.uipractice.MovieList.movieList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilesDisplayFragment extends BrowseSupportFragment {
    public static ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
    private BackgroundManager mBackgroundManager;
    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final int NUM_ROWS = 20;
    private static final int NUM_COLS = 15;
    public static int prevPosition = -1;
    private final Handler mHandler = new Handler(Looper.myLooper());
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private static String[] directories;
    public static int iterator;
    ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeadersState(HEADERS_DISABLED);
        mBackgroundUri = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTzy4ghMjcnwq8MiHmX4SSfpEEpUcSxis9B3A&usqp=CAU";
        loadRows();
        setTitle("");
        setupUIElements();
        prepareBackgroundManager();

        setupEventListeners();
    }

    private void loadRows() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenterMovie cardPresenter = new CardPresenterMovie();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
//        String dirName = (String) getActivity().getIntent().getSerializableExtra("DIRECTORY");
//        int i = 0;
        HeaderItem header = new HeaderItem(0, "Showing content from " + MovieList.dirSelected);
//        int rows = (movieList.size() / 5 )+ 1;
//        for(int i = 0; i < rows; i++) {
//            HeaderItem header = new HeaderItem(i, "");
//            rowsAdapter.add(new ListRow(header, new ArrayObjectAdapter(new CardPresenterMovie())));
//        }
//
//        setAdapter(rowsAdapter);

//        int count = 0;
//        int currentRow = 0;
//        while(count < movieList.size()) {
//            ListRow listRow = (ListRow) rowsAdapter.get(currentRow);
//            ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
//            int num = 5;
//            while(count < movieList.size() && num-->0) {
//                arrayObjectAdapter.add(movieList.get(count));
//                count++;
//            }
//            if(count >= movieList.size())
//                break;
//            currentRow++;
//        }


        for(int i = 0; i < movieList.size(); i++) {
            listRowAdapter.add(movieList.get(i));
        }
        rowsAdapter.add(new ListRow(header, listRowAdapter));
        setAdapter(rowsAdapter);

    }

    public void setRows() {

    }

    private void setupUIElements() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle("Now sharing files from " + MovieList.dirSelected); // Badge, when set, takes precedent
        // over title
//        setHeadersState(HEADERS_ENABLED);
  //      setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
//        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
//         set search icon color
//        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupEventListeners() {
//        setOnSearchClickedListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
//                        .show();
//            }
//        });

        setOnItemViewClickedListener(new FilesDisplayFragment.ItemViewClickedListener());
        setOnItemViewSelectedListener(new FilesDisplayFragment.ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            //Log.d("item", item.toString());
            if (item instanceof Movie) {
                mBackgroundUri = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTzy4ghMjcnwq8MiHmX4SSfpEEpUcSxis9B3A&usqp=CAU";
                Movie movie = (Movie) item;
                Log.d("MOVIEITEM", "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(),
                                ((ImageCardView) itemViewHolder.view).getMainImageView(),
                                DetailsActivity.SHARED_ELEMENT_NAME)
                        .toBundle();
                getActivity().startActivity(intent, bundle);
            } else if (item instanceof String) {
                Log.d("LEFTPANEL", item.toString());
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }


    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            //Log.d("item", item.toString());
            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Log.d("MOVIEITEM", "Item: " + item.toString());
                if(movie.getTitle() != null) {
                    mBackgroundUri = ((Movie) item).getBackgroundUrl();
                }


                startBackgroundTimer();
                if(movie.getTitle() != null) {
                    String name = ((Movie) item).getTitle().split("\\.")[0];
                    setTitle(name);
                }




//                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                intent.putExtra(DetailsActivity.MOVIE, movie);
//
//                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                getActivity(),
//                                ((ImageCardView) itemViewHolder.view).getMainImageView(),
//                                DetailsActivity.SHARED_ELEMENT_NAME)
//                        .toBundle();
//                getActivity().startActivity(intent, bundle);
            }


        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateBackground(mBackgroundUri);
                }
            });
        }
    }


    private void updateBackground(String uri) {
                int width = mMetrics.widthPixels;
                int height = mMetrics.heightPixels;
                //imageView = (ImageView) imageView.findViewById(R.id.bg);
                Glide.with(getActivity())
                        .load(uri)
                        .centerCrop()
                        .error(mDefaultBackground)
                        .into(new SimpleTarget<Drawable>(width, height) {
                            @Override
                            public void onResourceReady(@NonNull Drawable drawable,
                                                        @Nullable Transition<? super Drawable> transition) {
                                mBackgroundManager.setDrawable(drawable);
                            }
                        });
                mBackgroundTimer.cancel();
            }
    }

