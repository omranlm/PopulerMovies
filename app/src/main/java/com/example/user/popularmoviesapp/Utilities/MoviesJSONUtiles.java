package com.example.user.popularmoviesapp.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MoviesJSONUtiles {

    static String SUCCESS = "success";
    static String OWM_MESSAGE_CODE = "cod";
    public static MoviesContainer parseContainer(String moviesResults)  throws JSONException {


        JSONObject containerJSON = new JSONObject(moviesResults);

        //Check for errors

        if (containerJSON.has(OWM_MESSAGE_CODE)) {
            int errorCode = containerJSON.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:

                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        if (containerJSON.has(SUCCESS))
        {
            // in case of error in paging or invalid access key
            boolean success = containerJSON.getBoolean(SUCCESS);
            if (!success)
                return null;
        }
        // the returned object
        MoviesContainer moviesContainer = new MoviesContainer();

        String attribute = "page";
        if (containerJSON.has(attribute))
        {
            moviesContainer.page = containerJSON.getInt(attribute);
        }
        attribute = "total_results";
        if (containerJSON.has(attribute))
        {
            moviesContainer.total_results = containerJSON.getInt(attribute);
        }
        attribute = "total_pages";
        if (containerJSON.has(attribute))
        {
            moviesContainer.total_pages = containerJSON.getInt(attribute);
        }

        attribute = "results";
        if (containerJSON.has(attribute))
        {

            JSONArray moviesArrayJSON =  containerJSON.getJSONArray(attribute);

            if (moviesArrayJSON == null || moviesArrayJSON.length() == 0)
                return null;

            moviesContainer.movieList = new Movie[moviesArrayJSON.length()];
            for (int i = 0; i < moviesArrayJSON.length(); i++)
            {
                JSONObject movieJSON = moviesArrayJSON.getJSONObject(i);

                Movie movie = new Movie();

                attribute = "vote_count";
                if (movieJSON.has(attribute))
                {
                    movie.vote_count = movieJSON.getInt(attribute);
                }

                attribute = "id";
                if (movieJSON.has(attribute))
                {
                    movie.id = movieJSON.getInt(attribute);
                }

                attribute = "video";
                if (movieJSON.has(attribute))
                {
                    movie.video = movieJSON.getBoolean(attribute);
                }

                attribute = "vote_average";
                if (movieJSON.has(attribute))
                {
                    movie.vote_average = movieJSON.getDouble(attribute);
                }

                attribute = "title";
                if (movieJSON.has(attribute))
                {
                    movie.title = movieJSON.getString(attribute);
                }

                attribute = "popularity";
                if (movieJSON.has(attribute))
                {
                    movie.popularity = movieJSON.getDouble(attribute);
                }

                attribute = "poster_path";
                if (movieJSON.has(attribute))
                {
                    movie.poster_path = movieJSON.getString(attribute);
                }

                attribute = "original_language";
                if (movieJSON.has(attribute))
                {
                    movie.original_language = movieJSON.getString(attribute);
                }

                attribute = "original_title";
                if (movieJSON.has(attribute))
                {
                    movie.original_title = movieJSON.getString(attribute);
                }
                attribute = "genre_ids";
                if (movieJSON.has(attribute)) {
                    JSONArray genreArrayJSON = movieJSON.getJSONArray(attribute);

                    movie.genre_ids = new int[genreArrayJSON.length()];
                    for (int j = 0; j < genreArrayJSON.length(); j++)
                    {
                        movie.genre_ids[j] = genreArrayJSON.getInt(j);
                    }

                   // movie.genre_ids = movieJSON.getJSONArray(attribute);
                }

                attribute = "backdrop_path";
                if (movieJSON.has(attribute))
                {
                    movie.backdrop_path = movieJSON.getString(attribute);
                }

                attribute = "adult";
                if (movieJSON.has(attribute))
                {
                    movie.adult = movieJSON.getBoolean(attribute);
                }

                attribute = "overview";
                if (movieJSON.has(attribute))
                {
                    movie.overview = movieJSON.getString(attribute);
                }

                attribute = "release_date";
                if (movieJSON.has(attribute))
                {
                    movie.release_date =  getDatefromJSONString (movieJSON.getString(attribute));
                }

                moviesContainer.movieList[i] = movie;

            }


        }

        return moviesContainer;
    }

    private static Date getDatefromJSONString(String string) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format( new Date() );

        try {
            return format.parse (string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
}
