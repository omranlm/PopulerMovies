package com.example.user.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.popularmoviesapp.FavDatabase.AppDatabase;
import com.example.user.popularmoviesapp.FavDatabase.MovieEntery;
import com.example.user.popularmoviesapp.Utilities.Genre;
import com.example.user.popularmoviesapp.Utilities.MovieDetails;
import com.example.user.popularmoviesapp.Utilities.MoviesJSONUtiles;
import com.example.user.popularmoviesapp.Utilities.NetworkUtilities;
import com.example.user.popularmoviesapp.Utilities.Videos;
import com.example.user.popularmoviesapp.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.example.user.popularmoviesapp.Utilities.NetworkUtilities.isOnline;

public class DetailActivity extends AppCompatActivity implements
        TrailerAdapter.TrailerAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String> {

    private static final String MOVIE_ID_EXTRA = "movie_id";
    private TrailerAdapter mTrailerApapter;
    private RecyclerView mTrailerRecyclerView;

    private int LOADER_ID = 22;
    private ScrollView mScrollContent;
    private ProgressBar mProgressBar;
    private TextView mReviewsLabel;
    private TextView mTrailerLabel;

    private static final String UNKNOWN = "UNKNOWN";
    private static final CharSequence Y_FORMAT = "yyyy";
    private static final String MINUTES_TEXT = "min";
    private static final String VOTE_OUT_OF_TEN = "/10";
    private int movieId;

    private ActivityDetailBinding mDetailBinding;
    private MovieDetails movieDetails;
    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewApapter;

    private boolean isFavorite;
    //TextView mMovieDetailsErrorTextView;

    //ProgressBar mDetailsLoadingPB;
    private AppDatabase mDB;
    private String YOUTUBE_PREFIX="http://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("MovieDetail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Attached the content
        mScrollContent = (ScrollView) findViewById(R.id.sc_content);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
        mReviewsLabel = (TextView) findViewById(R.id.tv_reviews_label);

        mTrailerLabel = (TextView) findViewById(R.id.tv_trailer_label);

        showLoading();

        //
        mDB = AppDatabase.getInstance(getApplicationContext());
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        // set adaptor
        mTrailerRecyclerView = mDetailBinding.bottomLayout.rvTrailers;

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(false);
        mTrailerRecyclerView.setLayoutManager(layoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerApapter = new TrailerAdapter(this, this);
        mTrailerRecyclerView.setAdapter(mTrailerApapter);

        // set reviews layout manager
        mReviewRecyclerView = mDetailBinding.reviewsLayout.rvReviews;
        GridLayoutManager reviewlayoutManager = new GridLayoutManager(this, 1);
        reviewlayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        reviewlayoutManager.setReverseLayout(false);
        mReviewRecyclerView.setLayoutManager(reviewlayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewApapter = new ReviewAdapter(this);
        mReviewRecyclerView.setAdapter(mReviewApapter);
        //mDetailsLoadingPB = (ProgressBar) findViewById(R.id.pb_details_loading);
        //mMovieDetailsErrorTextView =(TextView)findViewById(R.id.tv_details_error);

        // attach handler for favorite


        // TODO change the image button icon according to movie favorite status


        Intent detailsIntent = getIntent();

        if (detailsIntent.hasExtra(Intent.EXTRA_INDEX)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                movieId = detailsIntent.getIntExtra(Intent.EXTRA_INDEX, 0);
            }
            getFavorites();

            if (mDB.taskDao().getMovie(movieId).getValue() == null)
                isFavorite = false;
            else
                isFavorite = true;
            setFavoriteIcon();

            loadDetails();
            //TODO move to loader

        } else {

            Log.d(getResources().getString(R.string.no_extra), getResources().getString(R.string.error_message));
            // DONE log error that this movie id it invalid or connection error
        }

    }

    private void showLoading() {
        mScrollContent.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void showContent() {
        mScrollContent.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    private void setFavoriteIcon() {
        Drawable icon = null;
        if (isFavorite) {
            icon = getResources().getDrawable(android.R.drawable.btn_star_big_on);
        } else {
            icon = getResources().getDrawable(android.R.drawable.btn_star_big_off);
        }

        mDetailBinding.topLayout.ibFavorite.setImageDrawable(icon);
    }

    private void getFavorites() {

        LiveData<List<MovieEntery>> dataSet = mDB.taskDao().loadAllFav();

        dataSet.observe(this, new Observer<List<MovieEntery>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntery> movieEnteries) {
                // update UI
                isFavorite = false;
                for (MovieEntery fav : movieEnteries) {
                    if (fav.getMovieId() == movieId)
                        isFavorite = true;
                }
                setFavoriteIcon();
            }
        });
    }

    private void addListnerFavorite() {
        mDetailBinding.topLayout.ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO Add current movie to favorites .. in the database
                if (!isFavorite) {
                    final MovieEntery movieEntery = new MovieEntery(movieId, movieDetails.title, movieDetails.poster_path);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mDB.taskDao().insertMovie(movieEntery);
                        }
                    });
                    thread.start();
                } else {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            // We need this query to be in the same thread as the delete movie action, that's why we have 2 getMovie queries
                            // one for the normal get movie which is wrapped with LiveData and a get to delete one
                            MovieEntery movieEntery = mDB.taskDao().getMovieToDelete(movieId);
                            if (movieEntery == null) return;
                            mDB.taskDao().deleteMovie(movieEntery);
                        }
                    });
                    thread.start();
                }

            }
        });
    }

    private void loadDetails() {
        if (isOnline(this)) {


            //URL movieDetailsAPI = NetworkUtilities.movieDetailsByIdURL(movieId);
            //new MovieDetailsTask().execute(movieDetailsAPI);


            Bundle idBundle = new Bundle();
            idBundle.putInt(MOVIE_ID_EXTRA, movieId);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> moviesLoader = loaderManager.getLoader(LOADER_ID);
            if (moviesLoader == null)
                loaderManager.initLoader(LOADER_ID,idBundle,this).forceLoad();
            else
                loaderManager.restartLoader(LOADER_ID,idBundle,this).forceLoad();

        } else {

        }

    }

    @Override
    public void onClick(Videos video) {
        // TODO handle calling the you tube
        Toast.makeText(this, video.name, Toast.LENGTH_LONG).show();

        // Code reference : https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(YOUTUBE_PREFIX + video.key));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable final Bundle args) {


        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                showLoading();
            }

            @Nullable
            @Override
            public String loadInBackground() {

                int movieId = args.getInt(MOVIE_ID_EXTRA);

                URL movieDetailsAPI = NetworkUtilities.movieDetailsByIdURL(movieId);

                String results = null;
                try {
                    results = NetworkUtilities.getResponseFromHttpUrl(movieDetailsAPI);

                } catch (IOException e) {
                    // TODO manage exception in getting the data
                    e.printStackTrace();
                }

                return results;
            }

        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String results) {


        showContent();

        addListnerFavorite();

        String movieDetailsResults = results;

        if (movieDetailsResults != null && !movieDetailsResults.equals("")) {
            // DONE parse & load the details
            try {
                movieDetails = MoviesJSONUtiles.parseMovieDetails(movieDetailsResults);


                loadUI(movieDetails);
            } catch (JSONException e) {

                //TODO mange errors 1

                e.printStackTrace();

            }

        } else {
             /*   mDetailsLoadingPB.setVisibility(View.INVISIBLE);
                mMovieDetailsErrorTextView.setText(getResources().getString(R.string.error_message));
                mMovieDetailsErrorTextView.setVisibility(View.VISIBLE);
               */ // error
            //TODO mange errors 2
        }

    }


    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    private void loadUI(MovieDetails movieDetails) {

        Picasso.with(this)
                .load(NetworkUtilities.MOVIES_POSTER_W500 + movieDetails.poster_path)
                .into(mDetailBinding.topLayout.ivMoviePoster);

        mDetailBinding.topLayout.tvMovieName.setText(movieDetails.title);

        String releaseYearString = android.text.format.DateFormat.format(Y_FORMAT, movieDetails.release_date).toString();
        mDetailBinding.topLayout.tvMovieDetailsYear.setText(releaseYearString);

        mDetailBinding.topLayout.tvMovieDetailsOverview.setText(movieDetails.overview);

        mDetailBinding.topLayout.tvMovieDetailsVotes.setText(String.valueOf(movieDetails.vote_average) + VOTE_OUT_OF_TEN);

        mDetailBinding.topLayout.tcDetailsDuration.setText(String.valueOf(movieDetails.runtime) + MINUTES_TEXT);

        //
        if (movieDetails.videos != null & movieDetails.videos.length > 0) {
            mTrailerApapter.setTrailersData(movieDetails.videos);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDetailBinding.bottomLayout.rvTrailers.setVisibility(View.INVISIBLE);
                    mTrailerLabel.setVisibility(View.INVISIBLE);
                }
            });


            // TODO Manage that no trailers

        }


        if (movieDetails.reviews != null & movieDetails.reviews.length > 0) {

            mReviewApapter.setReviewsData(movieDetails.reviews);
        } else {
            // TODO mention that there is no Trailers

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDetailBinding.reviewsLayout.rvReviews.setVisibility(View.INVISIBLE);
                    mReviewsLabel.setVisibility(View.INVISIBLE);
                }
            });

        }
    }


    private String getLangs(MovieDetails movieDetails) {
        StringBuilder allLangs = new StringBuilder();

        for (int i = 0; i < movieDetails.spoken_languages.length; i++) {
            allLangs.append(movieDetails.spoken_languages[i].name).append(", ");
        }
        if (allLangs.toString().equals(""))
            return UNKNOWN;

        return allLangs.substring(0, allLangs.length() - 2);
    }

    private String getHeadline(MovieDetails movieDetails) {

        String DATE_FORMAT_FULL = "dd MMMM yyyy";
        return movieDetails.runtime + " min | " + getGenre(movieDetails.genres) + " | " + android.text.format.DateFormat.format(DATE_FORMAT_FULL, movieDetails.release_date);
    }

    private String getGenre(Genre[] genres) {
        String allGenre = "";

        for (Genre genre : genres) {
            allGenre += genre.name + ", ";
        }

        if (allGenre.equals(""))
            return UNKNOWN;
        return allGenre.substring(0, allGenre.length() - 2);
    }

    private String getMovieTitle(MovieDetails movieDetails) {
        String headline = movieDetails.title;
        headline += " (" + android.text.format.DateFormat.format("yyyy", movieDetails.release_date) + ")";
        return headline;
    }


}
