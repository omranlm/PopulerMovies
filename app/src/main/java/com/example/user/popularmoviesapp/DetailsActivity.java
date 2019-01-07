package com.example.user.popularmoviesapp;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.user.popularmoviesapp.Utilities.Genre;
import com.example.user.popularmoviesapp.Utilities.MovieDetails;
import com.example.user.popularmoviesapp.Utilities.MoviesJSONUtiles;
import com.example.user.popularmoviesapp.Utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.user.popularmoviesapp.Utilities.NetworkUtilities.isOnline;

public class DetailsActivity extends AppCompatActivity {

    private static final String UNKNOWN = "UNKNOWN";
    int movieId;

    private String DATE_FORMAT_FULL = "dd MMMM yyyy";
    TextView mMovieHeadlineTextView;

    TextView mMovieDetailsVotesTextView;
    TextView mMovieDetailsVotesAvgTextView;
    TextView mMovieDetailsLangTextView;
    TextView mMovieDetailsPopularityTextView;
    TextView mMovieDetailsOverviewTextView;
    TextView mMovieDetailsStatusTextView;
    TextView mMovieDetailsOrgNameTextView;
    TextView mMovieDetailsErrorTextView;

    ProgressBar mDetailsLoadingPB;
    ScrollView mDetailsLayoutSF;
    ImageView mMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        mMovieHeadlineTextView = (TextView)findViewById(R.id.tv_movie_info);
        mMovieDetailsVotesTextView = (TextView)findViewById(R.id.tv_movie_details_votes);
        mMovieDetailsVotesAvgTextView = (TextView)findViewById(R.id.tc_details_votes_average);
        mMovieDetailsLangTextView= (TextView)findViewById(R.id.tv_movie_details_lang);
        mMovieDetailsPopularityTextView = (TextView)findViewById(R.id.tv_movie_details_popularity);
        mMovieDetailsOverviewTextView = (TextView)findViewById(R.id.tv_movie_details_overview);
        mMovieDetailsStatusTextView = (TextView)findViewById(R.id.tv_movie_details_status);
        mMovieDetailsOrgNameTextView = (TextView)findViewById(R.id.tv_movie_details_original_name);
        mMovieDetailsErrorTextView = (TextView)findViewById(R.id.tv_details_error);
        mDetailsLayoutSF = (ScrollView)findViewById(R.id.sv_details_layout);
        mDetailsLoadingPB = (ProgressBar)findViewById(R.id.pb_details_loading);

        mMoviePoster = (ImageView) findViewById(R.id.iv_movie_poster);

        Intent detailsIntent = getIntent();

        if (detailsIntent.hasExtra(Intent.EXTRA_INDEX)) {
            movieId = detailsIntent.getIntExtra(Intent.EXTRA_INDEX, 0);


            loadDetails();


        } else {

            Log.d(getResources().getString(R.string.no_extra),getResources().getString(R.string.error_message));
            // DONE log error that this movie id it invalid or connection error

        }


    }
    private void loadDetails() {
        if (isOnline(this)) {
            URL movieDetailsAPI = NetworkUtilities.MovieDetailsbyIdURL(movieId);
            new MovieDetailsTask().execute(movieDetailsAPI);
        }
        else
        {
            mDetailsLoadingPB.setVisibility(View.INVISIBLE);
            mDetailsLayoutSF.setVisibility(View.INVISIBLE);
            mMovieDetailsErrorTextView.setVisibility(View.VISIBLE);
            mMovieDetailsErrorTextView.setText(getResources().getString(R.string.no_intenet_connection_details));
        }

    }

    public class MovieDetailsTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMovieDetailsErrorTextView.setVisibility(View.INVISIBLE);
            mDetailsLoadingPB.setVisibility(View.VISIBLE);
            mDetailsLayoutSF.setVisibility(View.INVISIBLE);
            //  DONE ADD PROGRESS BAR
        }

        @Override
        protected String doInBackground(URL... urls) {

            if (urls == null || urls.length != 1)
                return "";
            URL queryURL = urls[0];

            String moviesDetailsResults = null;
            try {
                moviesDetailsResults = NetworkUtilities.getResponseFromHttpUrl(queryURL);
            } catch (IOException e) {
                mMovieDetailsErrorTextView.setText(e.getMessage());
                e.printStackTrace();
            }
            return moviesDetailsResults;
        }

        @Override
        protected void onPostExecute(String movieDetailsResults) {
            super.onPostExecute(movieDetailsResults);


            if (movieDetailsResults != null && !movieDetailsResults.equals("")) {
                // DONE parse & load the details
                try {
                    MovieDetails movieDetails = MoviesJSONUtiles.parseMovieDetails(movieDetailsResults);

                    mDetailsLoadingPB.setVisibility(View.INVISIBLE);
                    mMovieDetailsErrorTextView.setVisibility(View.INVISIBLE);
                    mDetailsLayoutSF.setVisibility(View.VISIBLE);
                    loadUI(movieDetails);
                } catch (JSONException e) {
                    mDetailsLoadingPB.setVisibility(View.INVISIBLE);
                    mMovieDetailsErrorTextView.setText(e.getMessage());
                    mMovieDetailsErrorTextView.setVisibility(View.VISIBLE);
                    // error parsing

                    e.printStackTrace();

                }

            } else {
                mDetailsLoadingPB.setVisibility(View.INVISIBLE);
                mMovieDetailsErrorTextView.setText(getResources().getString(R.string.error_message));
                mMovieDetailsErrorTextView.setVisibility(View.VISIBLE);
                // error
            }

        }
    }

    private void loadUI(MovieDetails movieDetails) {

        String movieInfo = getHeadline(movieDetails);
        mMovieHeadlineTextView.setText(movieInfo);
        Picasso.with(this)
                .load(NetworkUtilities.MOVIES_POSTER_W500 + movieDetails.poster_path)
                .into(mMoviePoster);

        mMovieDetailsVotesTextView.setText(String.valueOf(movieDetails.vote_count));
        mMovieDetailsVotesAvgTextView.setText(String.valueOf(movieDetails.vote_average));
        mMovieDetailsLangTextView.setText(getLangs(movieDetails));
        mMovieDetailsPopularityTextView.setText(String.valueOf(movieDetails.popularity));
        mMovieDetailsOverviewTextView.setText(movieDetails.overview);

        mMovieDetailsStatusTextView.setText(movieDetails.status);
        mMovieDetailsOrgNameTextView.setText(movieDetails.original_title);

        setTitle(getMovieTitle(movieDetails));
    }

    private String getLangs(MovieDetails movieDetails) {
        String allLangs = "";

        for (int i = 0; i <movieDetails.spoken_languages.length; i++) {
            allLangs +=  movieDetails.spoken_languages[i].name + ", ";
        }
        if (allLangs =="")
            return UNKNOWN;

        return allLangs.substring(0,allLangs.length()-2);
    }

    private String getHeadline(MovieDetails movieDetails) {

        return movieDetails.runtime + " min | " + getGenre(movieDetails.genres) + " | " + android.text.format.DateFormat.format(DATE_FORMAT_FULL,movieDetails.release_date);
    }

    private String getGenre(Genre[] genres) {
        String allGenre = "";

        for (int i = 0; i <genres.length; i++) {
            allGenre +=  genres[i].name + ", ";
        }

        if (allGenre =="")
            return UNKNOWN;
        return allGenre.substring(0,allGenre.length()-2);
    }

    private String getMovieTitle(MovieDetails movieDetails) {
        String headline = movieDetails.title;
        headline += " (" + android.text.format.DateFormat.format("yyyy",movieDetails.release_date) + ")";
        return headline;
    }

}
