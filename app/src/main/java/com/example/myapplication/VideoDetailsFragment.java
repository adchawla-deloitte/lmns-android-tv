package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * LeanbackDetailsFragment extends DetailsFragment, a Wrapper fragment for leanback details screens.
 * It shows a detailed view of video and its meta plus related videos.
 */
public class VideoDetailsFragment extends DetailsSupportFragment {
    private static final String TAG = "VideoDetailsFragment";

    private static final int ACTION_WATCH_TRAILER = 1;
    private static final int ACTION_RENT = 2;
    private static final int ACTION_BUY = 3;

    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;

    private static final int NUM_COLS = 10;

    private Movie mSelectedMovie;

    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;

    private DetailsSupportFragmentBackgroundController mDetailsBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate DetailsFragment");
        super.onCreate(savedInstanceState);

        mDetailsBackground = new DetailsSupportFragmentBackgroundController(this);

        mSelectedMovie =
                (Movie) getActivity().getIntent().getSerializableExtra(DetailsActivity.MOVIE);
        if (mSelectedMovie != null) {
            mPresenterSelector = new ClassPresenterSelector();
            mAdapter = new ArrayObjectAdapter(mPresenterSelector);
            setupDetailsOverviewRow();
            setupDetailsOverviewRowPresenter();
            setupRelatedMovieListRow();
            setAdapter(mAdapter);
//            initializeBackground(mSelectedMovie);
            setOnItemViewClickedListener(new ItemViewClickedListener());
        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

//    private void initializeBackground(Movie data) {
//        mDetailsBackground.enableParallax();
//        Glide.with(getActivity())
//                .asBitmap()
//                .centerCrop()
//                .error(R.drawable.default_background)
//                .load(data.getBackgroundImageUrl())
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap bitmap,
//                                                @Nullable Transition<? super Bitmap> transition) {
//                        mDetailsBackground.setCoverBitmap(bitmap);
//                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
//                    }
//                });
//    }

    private void setupDetailsOverviewRow() {
        Log.d(TAG, "doInBackground: " + mSelectedMovie.toString());
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedMovie);
        row.setImageDrawable(
                ContextCompat.getDrawable(getActivity(), R.drawable.default_background));
        int width = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_WIDTH);
        int height = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_HEIGHT);
        Glide.with(getActivity())
                .load(mSelectedMovie.getCardImageUrl())
                .centerCrop()
                .error(R.drawable.default_background)
                .into(new SimpleTarget<Drawable>(width, height) {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable,
                                                @Nullable Transition<? super Drawable> transition) {
                        Log.d(TAG, "details overview card image url ready: " + drawable);
                        row.setImageDrawable(drawable);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }
                });

        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();

        actionAdapter.add(
                new Action(
                        ACTION_WATCH_TRAILER,
                        getResources().getString(R.string.watch_trailer_1)));

        row.setActionsAdapter(actionAdapter);

        mAdapter.add(row);
    }

    private void setupDetailsOverviewRowPresenter() {
        // Set detail background.
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
        detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.selected_background));

        // Hook up transition element.
        FullWidthDetailsOverviewSharedElementHelper sharedElementHelper =
                new FullWidthDetailsOverviewSharedElementHelper();
        sharedElementHelper.setSharedElementEnterTransition(
                getActivity(), DetailsActivity.SHARED_ELEMENT_NAME);
        detailsPresenter.setListener(sharedElementHelper);
        detailsPresenter.setParticipatingEntranceTransition(true);

        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_WATCH_TRAILER) {
                    Intent intent;
                    if(mSelectedMovie.getType() == 1) {
                        intent = new Intent(getActivity(), PlaybackActivity.class);
                    } else {
                        intent = new Intent(getActivity(), ImageFullScreenActivity.class);
                    }
                    intent.putExtra(DetailsActivity.MOVIE, mSelectedMovie);
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }

    private void setupRelatedMovieListRow() {
        String subcategories[] = {getString(R.string.related_movies)};
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        Call<ServedDirectoryResponse> servedDirectoryResponseCall = DirectoryService.service.serveDirectory(MovieList.MOVIE_CATEGORY.get(0).pk);
        servedDirectoryResponseCall.enqueue(new Callback<ServedDirectoryResponse>() {
            @Override
            public void onResponse(Call<ServedDirectoryResponse> call, Response<ServedDirectoryResponse> response) {
                ServedDirectoryResponse resp = response.body();
                List<Movie> list = new ArrayList<>();
                Log.d("SERVERIP", resp.serverip.toString());
                Call<JsonObject> directoryContent = DirectoryService.service.getDirectoryContent(MovieList.MOVIE_CATEGORY.get(0).pk);

                directoryContent.enqueue(new Callback<JsonObject>() {

                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                        final JsonObject content;
                        content = response.body();
                        assert content != null;
                        JsonArray array = content.getAsJsonArray("directoryContent");
//                            Log.d("CONTENT", array.toString());

                        int count = 0;
                        for (int j = 0; j < array.size(); j++) {
//                                Log.d("ITERATOR", array.get(j).getAsJsonArray().get(0).toString());
                            String str =  (String) array.get(j).getAsJsonArray().get(1).getAsString();
                            String[]arr = str.split("\\.");

                            int len = arr.length;

//                                Log.d("STRING", arr[len - 1].substring(0));
                            Movie movie = new Movie();
                            //movie.setVideoUrl("http://" + resp.serverip + "/" + (String)array.get(j).getAsJsonArray().get(1).getAsString());
                            if (arr[len - 1].equals("mp3") || arr[len - 1].equals("mp4")) {
                                movie.setVideoUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(1).getAsString());
                                movie.setId(count);
                                movie.setTitle(array.get(j).getAsJsonArray().get(0).getAsString());
                                movie.setType(1);
                                movie.setCardImageUrl("https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg");


                            } else if(arr[len - 1].equals("png") || arr[len - 1].equals("jpg") || arr[len - 1].equals("jpeg")) {
                                movie.setTitle(array.get(j).getAsJsonArray().get(0).toString());
                                movie.setId(count);
                                movie.setCardImageUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(1).getAsString());
                                movie.setVideoUrl("http://" + resp.serverip + "/" + array.get(j).getAsJsonArray().get(0).getAsString());
                                movie.setType(2);
                            }
                            listRowAdapter.add(movie);
                            count++;
                        }



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


        HeaderItem header = new HeaderItem(0, MovieList.MOVIE_CATEGORY.get(0).dir_name);
        mAdapter.add(new ListRow(header, listRowAdapter));
        mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
    }

    private int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {

            if (item instanceof Movie) {
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(getResources().getString(R.string.movie), mSelectedMovie);

                Bundle bundle =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),
                                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                                        DetailsActivity.SHARED_ELEMENT_NAME)
                                .toBundle();
                getActivity().startActivity(intent, bundle);
            }
        }
    }
}