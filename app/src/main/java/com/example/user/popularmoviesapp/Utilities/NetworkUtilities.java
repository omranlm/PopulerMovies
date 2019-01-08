package com.example.user.popularmoviesapp.Utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtilities {


    private final static String MOVIE_DETAILS_DB_API = "http://api.themoviedb.org/3/movie/";

    final static String POPULAR_MOVIES_DB_API = "http://api.themoviedb.org/3/movie/";

    final public static  String MOVIES_POSTER = "http://image.tmdb.org/t/p/w185";

    final public static  String MOVIES_POSTER_W500 = "http://image.tmdb.org/t/p/w500";
    // DONE (1): Remove key before submitting to Udacity
    final static String MOVIES_DB_API_KEY ="Please add your API key here";

    final static String API_KEY_STRING = "api_key";

    final static String PAGE_QUERY_PARAM = "page";

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        // recalled this from other Udacity exercises
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static URL popularMoviesURL(String currentSort, int currentPageId) {
        Uri builtUri = Uri.parse(POPULAR_MOVIES_DB_API+ currentSort).buildUpon()
                .appendQueryParameter(API_KEY_STRING, MOVIES_DB_API_KEY)
                .appendQueryParameter(PAGE_QUERY_PARAM, String.valueOf(currentPageId))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }

    public static URL MovieDetailsByIdURL(int movieId) {
        Uri builtUri = Uri.parse(MOVIE_DETAILS_DB_API).buildUpon()
                .appendEncodedPath(String.valueOf(movieId))
                .appendQueryParameter(API_KEY_STRING, MOVIES_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
