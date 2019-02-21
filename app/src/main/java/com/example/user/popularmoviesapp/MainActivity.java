package com.example.user.popularmoviesapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.popularmoviesapp.Utilities.MoviesContainer;
import com.example.user.popularmoviesapp.Utilities.MoviesJSONUtiles;
import com.example.user.popularmoviesapp.Utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import static com.example.user.popularmoviesapp.Utilities.NetworkUtilities.isOnline;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {


    private RecyclerView mRecyclerView;

    private MoviesAdapter mMoviesAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar pbLoadingIndicator;

    private Menu mainMenu;
    private int currentPageId = 1;

    final static String TOP_RATED = "top_rated";
    final static String POPULAR = "popular";

    String currentSort = POPULAR;

    int totalPages;


    @Override
    public void onClick(int movieId) {


        Class<DetailActivity> destinationActivity = DetailActivity.class;

        Intent intent = new Intent(MainActivity.this, destinationActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.putExtra(Intent.EXTRA_INDEX, movieId);
        }

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DONE assign the adapter, recycler view ....
        mRecyclerView = findViewById(R.id.rv_movies_view);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this, this);

        mRecyclerView.setAdapter(mMoviesAdapter);
        pbLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        currentSort = POPULAR;
        // DONE work on paging
        GetMovies(currentSort);

    }

    private void GetMovies(String currentSort) {

        if (isOnline(this)) {
            URL popularMoviesAPI = NetworkUtilities.popularMoviesURL(currentSort, currentPageId);
            new MoviesTask().execute(popularMoviesAPI);
        } else {
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setText(getResources().getString(R.string.no_internet_connection));
        }
    }


    public class MoviesTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            // DONE Add progress bar
            pbLoadingIndicator.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            //DONE query the movies API
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
            //DONE add the progress bar and check for errors
            pbLoadingIndicator.setVisibility(View.INVISIBLE);


            if (moviesResults != null && !moviesResults.equals("")) {
                // DONE load the movies
                try {
                    MoviesContainer container = MoviesJSONUtiles.parseContainer(moviesResults);

                    totalPages = container.total_pages;
                    mMoviesAdapter.setMoviesData(container);
                    mRecyclerView.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    mErrorMessageDisplay.setVisibility(View.VISIBLE);
                    mErrorMessageDisplay.setText(e.getMessage());
                    e.printStackTrace();
                }
            } else {
                mErrorMessageDisplay.setVisibility(View.VISIBLE);
                mErrorMessageDisplay.setText(getResources().getString(R.string.error_message));
                //DONE manage errors

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mainMenu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.paging, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_next) {
            if (currentPageId == totalPages) return false;
            currentPageId++;
            MenuItem page = mainMenu.findItem(R.id.page);
            page.setTitle(String.valueOf(currentPageId));

            manageMenu();
            GetMovies(currentSort);
            return true;
        }
        if (id == R.id.action_previous) {
            if (currentPageId == 1) return false;
            currentPageId--;
            MenuItem page = mainMenu.findItem(R.id.page);
            page.setTitle(String.valueOf(currentPageId));
            manageMenu();
            GetMovies(currentSort);
            return true;
        }
        if (id == R.id.action_sort_by_popular) {
            currentSort = POPULAR;
            GetMovies(currentSort);
        }
        if (id == R.id.action_sort_by_top_rated) {
            currentSort = TOP_RATED;
            GetMovies(currentSort);
        }
        if (id == R.id.action_show_favorite) {
            // TODO navigate to favorite list

            Class<FavoriteActivity> destinationActivity = FavoriteActivity.class;
            Intent intent = new Intent(MainActivity.this, destinationActivity);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void manageMenu() {
        if (mainMenu != null) {
            MenuItem previous = mainMenu.findItem(R.id.action_previous);
            MenuItem next = mainMenu.findItem(R.id.action_next);

            if (currentPageId == totalPages) {
                next.setCheckable(false);
                previous.setCheckable(true);
            } else if (currentPageId <= 1) {

                next.setCheckable(true);
                previous.setCheckable(false);
            } else {
                next.setCheckable(true);
                previous.setCheckable(true);
            }

        }
    }
}

