package com.example.uipractice;

import static com.example.uipractice.MovieList.movieList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.SearchOrbView;
import androidx.leanback.widget.TitleViewAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectoriesFragment extends BrowseSupportFragment {
    public static ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
    private BackgroundManager mBackgroundManager;
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
    private static String[] directories;
    public static int iterator;
    private ProgressBar prg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeadersState(HEADERS_DISABLED);
        loadRows();
        setupUIElements();
        prepareBackgroundManager();
        setRows();
        setupEventListeners();
    }

    private void loadRows() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();
//        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
//        int i = 0;
//        HeaderItem header = new HeaderItem(i, "Continue Watching");
//        rowsAdapter.add(new ListRow(header, new ArrayObjectAdapter(new CardPresenter())));
        int i = 0;
        i++;
        rowsAdapter.add(new ListRow(new HeaderItem(i, "Video Directories"), new ArrayObjectAdapter(new CardPresenter())));
        i++;
        rowsAdapter.add(new ListRow(new HeaderItem(i, "Audio Directories"), new ArrayObjectAdapter(new CardPresenter())));
        i++;
        rowsAdapter.add(new ListRow(new HeaderItem(i, "Images Directories"), new ArrayObjectAdapter(new CardPresenter())));
        setAdapter(rowsAdapter);
    }

    public void setRows() {
        ListRow listRow1 = (ListRow) rowsAdapter.get(0);
//        ArrayObjectAdapter arrayObjectAdapter1 = (ArrayObjectAdapter) listRow1.getAdapter();
//        arrayObjectAdapter1.clear();
//        for(int i = 0; i < 5; i++) {
//            Directory directory = new Directory();
//            directory.setCardImageUrl("https://icons.veryicon.com/256/Application/Style%204%20Megapack/Folder%20Music.png");
//            directory.setId(i);
//            directory.setTitle("Content " + i);
//            arrayObjectAdapter1.add(directory);
//        }

//        ListRow listRow2 = (ListRow) rowsAdapter.get(1);
//        ArrayObjectAdapter arrayObjectAdapter2 = (ArrayObjectAdapter) listRow2.getAdapter();
//        arrayObjectAdapter2.clear();
        int length = DirectoryList.DIRECTORY_CATEGORY.size();

        for(int i = 0; i < length; i++) {
            Directory directory = new Directory();
            if(DirectoryList.DIRECTORY_CATEGORY.get(i).getDir_type().equals("1")) {
                ListRow listRow = (ListRow) rowsAdapter.get(0);
                ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
                arrayObjectAdapter.add(directory);
                directory.setVideoUrl("");
                directory.setId(DirectoryList.DIRECTORY_CATEGORY.get(i).pk);
                directory.setTitle(DirectoryList.DIRECTORY_CATEGORY.get(i).getDir_name());
                directory.setDir_type(DirectoryList.DIRECTORY_CATEGORY.get(i).getDir_type());
                directory.setCardImageUrl("https://github.com/adhyanchawla/banner-repo/blob/master/red-folder-video.png?raw=true");
            } else if(DirectoryList.DIRECTORY_CATEGORY.get(i).getDir_type().equals("2")) {
                ListRow listRow = (ListRow) rowsAdapter.get(1);
                ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
                directory.setVideoUrl("");
                directory.setId(DirectoryList.DIRECTORY_CATEGORY.get(i).pk);
                directory.setTitle(DirectoryList.DIRECTORY_CATEGORY.get(i).getDir_name());
                directory.setDir_type(DirectoryList.DIRECTORY_CATEGORY.get(i).getDir_type());
                directory.setCardImageUrl("https://github.com/adhyanchawla/banner-repo/blob/master/red-folder-music.png?raw=true");
                arrayObjectAdapter.add(directory);
            } else {
                ListRow listRow = (ListRow) rowsAdapter.get(2);
                ArrayObjectAdapter arrayObjectAdapter = (ArrayObjectAdapter) listRow.getAdapter();
                directory.setVideoUrl("");
                directory.setId(DirectoryList.DIRECTORY_CATEGORY.get(i).pk);
                directory.setTitle(DirectoryList.DIRECTORY_CATEGORY.get(i).getDir_name());
                directory.setDir_type(DirectoryList.DIRECTORY_CATEGORY.get(i).getDir_type());
                directory.setCardImageUrl("https://github.com/adhyanchawla/banner-repo/blob/master/red-folder-pictures.png?raw=true");
                arrayObjectAdapter.add(directory);
            }

        }


    }

    private void setupUIElements() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle("Content from Desktop"); // Badge, when set, takes precedent
        // over title
//        setHeadersState(HEADERS_ENABLED);
//        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
//        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
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

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.directories_main, container, false);
//        prg = (ProgressBar)view.findViewById(R.id.progressBar1);
//
//        return view;
//    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            //Log.d("item", item.toString());
            if (item instanceof Directory) {
                if(movieList.size() > 0)
                    movieList = new ArrayList<>();

                Log.d("DIRECTORYDATAITEM", item.toString());
                int pk = ((Directory) item).getId();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("PK", pk + "");
                        Call<ServedDirectoryResponse> servedDirectoryResponseCall = DirectoryService.service.serveDirectory(pk);

                        servedDirectoryResponseCall.enqueue(new Callback<ServedDirectoryResponse>() {
                            @Override
                            public void onResponse(Call<ServedDirectoryResponse> call, Response<ServedDirectoryResponse> response) {
                                ServedDirectoryResponse resp = response.body();
                                List<Movie> list = new ArrayList<>();
                                Log.d("SERVERIP", resp.serverip.toString());
                                Call<JsonObject> directoryContent = DirectoryService.service.getDirectoryContent(pk);
                                Log.d("ROW ADAPTER", rowsAdapter.toString());
                                directoryContent.enqueue(new Callback<JsonObject>() {

                                    @Override
                                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
//                                        prg.setVisibility(View.INVISIBLE);
                                        final JsonObject content;
                                        content = response.body();
                                        assert content != null;
                                        JsonArray array = content.getAsJsonArray("directoryContent");

                                        int count = 1;
                                        for (int j = 0; j < array.size(); j++) {

                                            String str = (String) array.get(j).getAsJsonArray().get(1).getAsString();
                                            String[] arr = str.split("\\.");
                                            StringBuilder sb = new StringBuilder();
                                            for (int i = 0; i < arr.length - 1; i++) {
                                                sb.append(arr[i]);
                                            }
                                            int len = arr.length;
                                            Movie movie = new Movie();
                                            //movie.setVideoUrl("http://" + resp.serverip + "/" + (String)array.get(j).getAsJsonArray().get(1).getAsString());
                                            if (((Directory) item).getDir_type().equals("1")) {
                                                if (arr[len - 1].equals("mp4")) {
                                                    movie.setVideoUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(1).getAsString());
                                                    movie.setId(count);
                                                    movie.setTitle(array.get(j).getAsJsonArray().get(0).getAsString());
                                                    movie.setType(1);
                                                    movie.setCardImageUrl("http://" + resp.serverip + "/" + "Thumbnails/" + sb.toString() + ".jpg");
                                                }
                                            } else if (((Directory) item).getDir_type().equals("2")) {
                                                if (arr[len - 1].equals("mp3")) {
                                                    Log.d("URL", "http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(1).getAsString());
                                                    movie.setVideoUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(1).getAsString());
                                                    movie.setId(count);
                                                    movie.setTitle(array.get(j).getAsJsonArray().get(0).getAsString());
                                                    movie.setType(1);
                                                    movie.setCardImageUrl("https://cdn-icons-png.flaticon.com/512/59/59343.png");
                                                }
                                            } else if (((Directory) item).getDir_type().equals("3")) {
                                                if (arr[len - 1].equals("png") || arr[len - 1].equals("jpg") || arr[len - 1].equals("jpeg")) {
                                                    movie.setTitle(array.get(j).getAsJsonArray().get(0).getAsString());
                                                    movie.setId(count);
                                                    movie.setCardImageUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(1).getAsString());
                                                    movie.setVideoUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(0).getAsString());
                                                    movie.setType(2);
                                                }
                                            }

                                            movieList.add(movie);


                                            count++;
                                        }


                                        Intent intent = new Intent(getActivity(), FilesDisplayActivity.class);
//                                        intent.putExtra("MOVIE-LIST", ArrayList<Movie>movieList);
                                        intent.putExtra("DIRECTORY", ((Directory) item).getDir_name());
                                        Log.d("DIRNAME", ((Directory) item).getTitle());
                                        MovieList.dirSelected = ((Directory) item).getTitle();
//                                        Toast.makeText(getActivity(), R.string.loading, Toast.LENGTH_LONG).show();
                                        startActivity(intent);


                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        prg.setVisibility(View.INVISIBLE);
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
                }, 1000);
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
            if(item instanceof Directory) {
                Log.d("DIRECTORYITEM", item.toString());
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onInflateTitleView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return super.onInflateTitleView(inflater, parent, savedInstanceState);
    }

    @Override
    public void installTitleView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.installTitleView(inflater, parent, savedInstanceState);
    }

    @Override
    public void setTitleView(View titleView) {
        super.setTitleView(titleView);
    }

    @Override
    public View getTitleView() {
        return super.getTitleView();
    }

    @Override
    public TitleViewAdapter getTitleViewAdapter() {
        return super.getTitleViewAdapter();
    }

    @Override
    public void showTitle(boolean show) {
        super.showTitle(show);
    }

    @Override
    public void showTitle(int flags) {
        super.showTitle(flags);
    }

    @Override
    public void setBadgeDrawable(Drawable drawable) {
        super.setBadgeDrawable(drawable);
    }

    @Override
    public Drawable getBadgeDrawable() {
        return super.getBadgeDrawable();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    public CharSequence getTitle() {
        return super.getTitle();
    }

    @Override
    public void setOnSearchClickedListener(View.OnClickListener listener) {
        super.setOnSearchClickedListener(listener);
    }

    @Override
    public void setSearchAffordanceColors(SearchOrbView.Colors colors) {
        super.setSearchAffordanceColors(colors);
    }

    @Override
    public SearchOrbView.Colors getSearchAffordanceColors() {
        return super.getSearchAffordanceColors();
    }

    @Override
    public void setSearchAffordanceColor(int color) {
        super.setSearchAffordanceColor(color);
    }

    @Override
    public int getSearchAffordanceColor() {
        return super.getSearchAffordanceColor();
    }
}
