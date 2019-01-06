package com.example.user.popularmoviesapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.popularmoviesapp.Utilities.MoviesContainer;
import com.example.user.popularmoviesapp.Utilities.MoviesJSONUtiles;
import com.example.user.popularmoviesapp.Utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler{


    private RecyclerView mRecyclerView;

    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    int currentPageId = 1;

    @Override
    public void onClick(int movieId) {
        // TODO call the details activity with movie id
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO assign the adapter, recycler view ....
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_view);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this,this);

        mRecyclerView.setAdapter(mMoviesAdapter);

        // TODO work on paging
        GetMovies(currentPageId);

    }
    private void GetMovies(int pageId) {

        URL popularMoviesAPI = NetworkUtilities.popularMoviesURL(currentPageId);

        new MoviesTask().execute(popularMoviesAPI);


    }

    public class MoviesTask extends AsyncTask<URL, Void,String> {
        @Override
        protected void onPreExecute() {
            // TODO Add progress bar
        }
        @Override
        protected String doInBackground(URL... urls) {
            //TODO query the movies API
            if (urls == null || urls.length != 1)
                return "";
            URL queryURL = urls[0];


            String moviesResults = null;
            try {
                moviesResults = NetworkUtilities.getResponseFromHttpUrl(queryURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return moviesResults;
        }

        @Override
        protected void onPostExecute(String moviesResults) {
            super.onPostExecute(moviesResults);
            //TODO add the progress bar and check for errors
            if (moviesResults != null && !moviesResults.equals(""))
            {
                // TODO load the movies
                try {
                    MoviesContainer container = MoviesJSONUtiles.parseContainer(moviesResults);

                    mMoviesAdapter.setMoviesData(container);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                //TODO manage errors
            }

        }
    }

    private void loadPopularMovies() {

        // Load the recycler view
    }

}

