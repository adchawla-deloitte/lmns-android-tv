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
import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.DiffCallback;
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
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
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
    public static int prevPosition = -1;
    private final Handler mHandler = new Handler(Looper.myLooper());
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;
    public static HashMap<Integer, List<Movie>> movieMap;
    private static String[] directories;
    public static int iterator;
    public static ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
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

        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
        int i;
        for (i = 0; i < MovieList.MOVIE_CATEGORY.size(); i++) {
//            Log.d("Movie", movieMap.get(i).size() + "");
//            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
//            for (int j = 0; j < movieMap.get(i).size(); j++) {
//                listRowAdapter.add(movieMap.get(i).get(j % movieMap.get(i).size()));
//            }
            HeaderItem header = new HeaderItem(i, MovieList.MOVIE_CATEGORY.get(i).dir_name);
            rowsAdapter.add(new ListRow(header, new ArrayObjectAdapter(new CardPresenter())));
        }
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

//    class FocusedItemAtStartListRowPresenter extends ListRowPresenter {
//        override fun createRowViewHolder(parent: ViewGroup): RowPresenter.ViewHolder {
//            val viewHolder = super.createRowViewHolder(parent)
//
//            with((viewHolder.view as ListRowView).gridView) {
//                windowAlignment = BaseGridView.WINDOW_ALIGN_LOW_EDGE
//                windowAlignmentOffsetPercent = 0f
//                windowAlignmentOffset = parent.resources.getDimensionPixelSize(R.dimen.lb_browse_padding_start)
//                itemAlignmentOffsetPercent = 0f
//            }
//
//            return viewHolder
//        }
//    }

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
                Log.d("LEFTPANEL", item.toString());
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
            }
//            if(getHeadersSupportFragment() != null){
//                Log.d("LEFTPANEL", getHeadersSupportFragment().getSelectedPosition() + "");
//            }
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
            } else if(item instanceof HeaderItem) {
                Log.d("LEFTPANEL", item.toString());
            }
            if(getHeadersSupportFragment() != null && getHeadersSupportFragment().getSelectedPosition() != prevPosition){
                prevPosition = getHeadersSupportFragment().getSelectedPosition();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("hello", "hello");
                        //ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
                        for(int i = 0; i < MovieList.MOVIE_CATEGORY.size(); i++) {
                            ListRow listRow = (ListRow) rowsAdapter.get(i);
                            ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
                            Log.d("ARRAYOBJECT1", arrayObjectAdapter.size() + "");
                            arrayObjectAdapter.clear();
                        }

                        ListRow listRow = (ListRow) rowsAdapter.get(getHeadersSupportFragment().getSelectedPosition());
                        ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();

                        //Log.d("ARRAYOBJECT1", arrayObjectAdapter.size() + "");

//                        Log.d();

                        Log.d("LEFTPANEL", getHeadersSupportFragment().getSelectedPosition() + "");
                        Call<ServedDirectoryResponse> servedDirectoryResponseCall = DirectoryService.service.serveDirectory(MovieList.MOVIE_CATEGORY.get(getHeadersSupportFragment().getSelectedPosition()).pk);
                        servedDirectoryResponseCall.enqueue(new Callback<ServedDirectoryResponse>() {
                            @Override
                            public void onResponse(Call<ServedDirectoryResponse> call, Response<ServedDirectoryResponse> response) {
                                ServedDirectoryResponse resp = response.body();
                                List<Movie> list = new ArrayList<>();
                                Log.d("SERVERIP", resp.serverip.toString());
                                Call<JsonObject> directoryContent = DirectoryService.service.getDirectoryContent(MovieList.MOVIE_CATEGORY.get(getHeadersSupportFragment().getSelectedPosition()).pk);
                                Log.d("ROW ADAPTER", rowsAdapter.toString());
                                directoryContent.enqueue(new Callback<JsonObject>() {

                                    @Override
                                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                                        final JsonObject content;
                                        content = response.body();
                                        assert content != null;
                                        JsonArray array = content.getAsJsonArray("directoryContent");
                                        List<Movie> list = new ArrayList<>();
                                        int count = 0;
                                        for (int j = 0; j < array.size(); j++) {

                                            String str =  (String) array.get(j).getAsJsonArray().get(1).getAsString();
                                            String[]arr = str.split("\\.");
                                            StringBuilder sb = new StringBuilder();
                                            for(int i = 0; i < arr.length - 1; i++) {
                                                sb.append(arr[i]);
                                            }
                                            int len = arr.length;
                                            Movie movie = new Movie();
                                            //movie.setVideoUrl("http://" + resp.serverip + "/" + (String)array.get(j).getAsJsonArray().get(1).getAsString());
                                            if (MovieList.MOVIE_CATEGORY.get(getHeadersSupportFragment().getSelectedPosition()).dir_type.equals("1")) {
                                                if(arr[len - 1].equals("mp4")) {
                                                    movie.setVideoUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(1).getAsString());
                                                    movie.setId(count);
                                                    movie.setTitle(array.get(j).getAsJsonArray().get(0).getAsString());
                                                    movie.setType(1);
                                                    movie.setCardImageUrl("http://" + resp.serverip + "/" + "Thumbnails/" + sb.toString() + ".jpg");
                                                }
                                            } else if(MovieList.MOVIE_CATEGORY.get(getHeadersSupportFragment().getSelectedPosition()).dir_type.equals("2")) {
                                                if(arr[len - 1].equals("mp3")) {
                                                    movie.setVideoUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(1).getAsString());
                                                    movie.setId(count);
                                                    movie.setTitle(array.get(j).getAsJsonArray().get(0).getAsString());
                                                    movie.setType(1);
                                                    movie.setCardImageUrl("https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg");
                                                }
                                            } else if(MovieList.MOVIE_CATEGORY.get(getHeadersSupportFragment().getSelectedPosition()).dir_type.equals("3")) {
                                                if(arr[len - 1].equals("png") || arr[len - 1].equals("jpg") || arr[len - 1].equals("jpeg")) {
                                                    movie.setTitle(array.get(j).getAsJsonArray().get(1).getAsString());
                                                    movie.setId(count);
                                                    movie.setCardImageUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(1).getAsString());
                                                    movie.setVideoUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(0).getAsString());
                                                    movie.setType(2);
                                                }
                                            }
                                            list.add(movie);
                                            arrayObjectAdapter.add(movie);

                                            count++;
                                        }

                                        Log.d("ARRAYOBJECT2", arrayObjectAdapter.size() + "");

//                                        rowsAdapter.get(0).add(new ListRow(new HeaderItem("HELLO"), new ArrayObjectAdapter()))
//                                        arrayObjectAdapter.add
                                        //rowsAdapter.add(new ListRow(headerItem, listRowAdapter));
//                                        setAdapter(rowsAdapter);
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        Log.d("DIRECTORY", "Could not fetch", t);
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<ServedDirectoryResponse> call, Throwable t) {
                                Log.d("ServedDirectory", "Unable to retrieve served directory", t);
                            }
                        });
                    }
                }, 2000);

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
            public void onResponse(@NonNull Call<List<DirectoryDataItem>> call, Response<List<DirectoryDataItem>> response) {
                MovieList.MOVIE_CATEGORY = response.body();
                for(DirectoryDataItem item: MovieList.MOVIE_CATEGORY) {
                    String[] dirName = item.dir_name.split("/");
                    item.dir_name = dirName[dirName.length - 1];
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<DirectoryDataItem>> call, Throwable t) {

                Log.d("DATAITEM", "No dataitem found", t);
            }
        });
    }

}