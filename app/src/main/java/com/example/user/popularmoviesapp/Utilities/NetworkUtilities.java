package com.example.user.popularmoviesapp.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtilities {


    final static String MOVIES_DB_API = "http://api.themoviedb.org/3/";

    final static String POPULAR_MOVIES_DB_API = "http://api.themoviedb.org/3/movie/popular?";

    // TODO (1): Remove key before submitting to Udacity
    final static String MOVIES_DB_API_KEY = "none";

    final static String API_KEY_STRING = "api_key";

    final static String PAGE_QUERY_PARAM = "page";

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        // learned this from other exercises
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

    public static URL popularMoviesURL(int currentPageId) {
        Uri builtUri = Uri.parse(POPULAR_MOVIES_DB_API).buildUpon()
                .appendQueryParameter(API_KEY_STRING, MOVIES_DB_API_KEY)
                .appendQueryParameter(PAGE_QUERY_PARAM,String.valueOf(currentPageId))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }

}
