package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends BrowseSupportFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 15;

    private final Handler mHandler = new Handler(Looper.myLooper());
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;
    public static HashMap<Integer, List<Movie>> movieMap;
    private static String[] directories;
    public static int iterator;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setupEventListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void loadRows() {
//        List<Movie> list = MovieList.setupMovies();
        //getDirectories();
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();

        int i;
        for (i = 0; i < MovieList.MOVIE_CATEGORY.length; i++) {
//            Log.d("Movie", movieMap.get(i).size() + "");
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            for (int j = 0; j < movieMap.get(i).size(); j++) {
                listRowAdapter.add(movieMap.get(i).get(j % movieMap.get(i).size()));
            }
            HeaderItem header = new HeaderItem(i, MovieList.MOVIE_CATEGORY[i]);
            rowsAdapter.add(new ListRow(header, listRowAdapter));
        }

        HeaderItem gridHeader = new HeaderItem(i, "PREFERENCES");

        GridItemPresenter mGridPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add(getResources().getString(R.string.grid_view));
        gridRowAdapter.add(getString(R.string.error_fragment));
        gridRowAdapter.add(getResources().getString(R.string.personal_settings));
        rowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

        setAdapter(rowsAdapter);
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
                        .show();
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
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

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(),
                                ((ImageCardView) itemViewHolder.view).getMainImageView(),
                                DetailsActivity.SHARED_ELEMENT_NAME)
                        .toBundle();
                getActivity().startActivity(intent, bundle);
            } else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            if (item instanceof Movie) {
//                mBackgroundUri = ((Movie) item).getBackgroundImageUrl();
                startBackgroundTimer();
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

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

    public static void getDirectories() {
        if(movieMap == null) {
            movieMap = new HashMap<>();
        }
        Call<List<DirectoryDataItem>> getDirectory = DirectoryService.service.getDirectory();
        getDirectory.enqueue(new Callback<List<DirectoryDataItem>>() {
            @Override
            public void onResponse(Call<List<DirectoryDataItem>> call, Response<List<DirectoryDataItem>> response) {
                List<DirectoryDataItem> items = response.body();
                List<String> dirs = new ArrayList<>();
                List<Integer> pks = new ArrayList<>();
                String[] movieCategories = new String[items.size()];
                for(int i = 0; i < movieCategories.length; i++) {
                    movieCategories[i] = items.get(i).dir_name;
                    if(!movieMap.containsKey(i)) {
                        Log.d("MAPKEYS", i + "");
                        movieMap.put(i, new ArrayList<>());
                    }
                }
                for(DirectoryDataItem d: items) {
                        dirs.add(d.dir_name);
                        pks.add(d.pk);
                }
                getDirectoryContent(pks);
//                Log.d("DATAITEM", pks.toString());
                MovieList.MOVIE_CATEGORY = movieCategories;

            }

            @Override
            public void onFailure(Call<List<DirectoryDataItem>> call, Throwable t) {

                Log.d("DATAITEM", "No dataitem found", t);
            }
        });
    }

    public static void getDirectoryContent(@NonNull List<Integer> pks) {
//        HashMap<Integer, List<Movie>> map = new HashMap<>();
//        int i;
        iterator = 0;
        while(iterator < pks.size()) {
//            Log.d("Hello", "get directory content of pk " + pks.get(iterator));
            Call<DirectoryContent> directoryContent = DirectoryService.service.getDirectoryContent(pks.get(iterator));
//            int finalI = i;


            directoryContent.enqueue(new Callback<DirectoryContent>() {

                @Override
                public void onResponse(Call<DirectoryContent> call, Response<DirectoryContent> response) {
                    final DirectoryContent content;
                    content = response.body();
                    List<String> res = content.directoryContent;
                    Log.d("Response", res.toString());

                    Log.d("FinalI", "Before passing to 2nd API" + (iterator - 1));
                    Call<ServedDirectoryResponse> servedDirectoryResponseCall = DirectoryService.service.serveDirectory(pks.get(iterator - 1));
                    servedDirectoryResponseCall.enqueue(new Callback<ServedDirectoryResponse>() {
                        @Override
                        public void onResponse(Call<ServedDirectoryResponse> call, Response<ServedDirectoryResponse> response) {
                            ServedDirectoryResponse resp = response.body();
                            List<Movie> list = new ArrayList<>();
                            if(resp == null)
                                return;
                            int count = 0;
                            for (String r : res) {
                                String[] arr = r.split("\\.");
                                int len = arr.length;
                                Movie movie = new Movie();
                                if (arr[len - 1].equals("mp3") || arr[len - 1].equals("mp4")) {
//                                    Movie movie = new Movie();
                                    movie.setVideoUrl("http://" + resp.serverip + "/" + r);
                                    movie.setId(count);
                                    movie.setTitle(r);
                                    movie.setType(1);
                                    movie.setCardImageUrl("https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg");
                                    //finalRes.add("http://" + resp.serverip + "/" + r);

                                } else if(arr[len - 1].equals("png") || arr[len - 1].equals("jpg") || arr[len - 1].equals("jpeg")) {
                                    movie.setTitle(r);
                                    movie.setId(count);
                                    movie.setCardImageUrl("http://" + resp.serverip + "/" + r);
                                    movie.setVideoUrl("http://" + resp.serverip + "/" + r);
                                    movie.setType(2);
                                }
                                list.add(movie);
                                count++;
                            }
                            //String[] urls = new String[count];
//                            for(int j = 0; j < count; j++) {
//                                urls[j] = finalRes.get(j);
//                            }
                            //MovieList.videoUrls = urls;
                            int n = iterator;
                            while(movieMap.containsKey(n)) {
                                n++;
                            }


                            Log.d("ITERATOR", n + "");
                            movieMap.put(n - 1, list);
                            Log.d("Movie List", movieMap.toString());

                            //Log.d("DIRECTORY", finalRes.toString());
//                            "Zeitgeist 2010_ Year in Review"
//                            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg"
                        }

                        @Override
                        public void onFailure(Call<ServedDirectoryResponse> call, Throwable t) {
                            Log.d("ServedDirectory", "Unable to retrieve served directory", t);
                        }
                    });

                }

                @Override
                public void onFailure(Call<DirectoryContent> call, Throwable t) {
                    Log.d("DIRECTORY", "Could not fetch", t);
                }
            });
            iterator++;
        }

    }

}