package com.example.user.popularmoviesapp.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MoviesJSONUtiles {

    static private final String SUCCESS = "success";
    private static final String TRAILERS ="videos" ;
    private static final String SITE = "site";
    private static final String SIZE = "size";
    private static final String TYPE = "type";
    private static final String REVIEWS = "reviews";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String URL = "url";
    static private String OWM_MESSAGE_CODE = "cod";
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

        String attribute = PAGE;
        if (containerJSON.has(attribute) && !containerJSON.isNull(attribute))
        {
            moviesContainer.page = containerJSON.getInt(attribute);
        }
        attribute = TOTAL_RESULTS;
        if (containerJSON.has(attribute) && !containerJSON.isNull(attribute))
        {
            moviesContainer.total_results = containerJSON.getInt(attribute);
        }
        attribute = TOTAL_PAGES;
        if (containerJSON.has(attribute) && !containerJSON.isNull(attribute))
        {
            moviesContainer.total_pages = containerJSON.getInt(attribute);
        }

        attribute = RESULTS;
        if (containerJSON.has(attribute) && !containerJSON.isNull(attribute))
        {

            JSONArray moviesArrayJSON =  containerJSON.getJSONArray(attribute);

            if (moviesArrayJSON == null || moviesArrayJSON.length() == 0)
                return null;

            moviesContainer.movieList = new Movie[moviesArrayJSON.length()];
            for (int i = 0; i < moviesArrayJSON.length(); i++)
            {
                JSONObject movieJSON = moviesArrayJSON.getJSONObject(i);

                Movie movie = new Movie();

                attribute = VOTE_COUNT;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.vote_count = movieJSON.getInt(attribute);
                }

                attribute = ID;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.id = movieJSON.getInt(attribute);
                }

                attribute = VIDEO;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.video = movieJSON.getBoolean(attribute);
                }

                attribute = VOTE_AVERAGE;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.vote_average = movieJSON.getDouble(attribute);
                }

                attribute = TITLE;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.title = movieJSON.getString(attribute);
                }

                attribute = POPULARITY;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.popularity = movieJSON.getDouble(attribute);
                }

                attribute = POSTER_PATH;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.poster_path = movieJSON.getString(attribute);
                }

                attribute = ORIGINAL_LANGUAGE;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.original_language = movieJSON.getString(attribute);
                }

                attribute = ORIGINAL_TITLE;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.original_title = movieJSON.getString(attribute);
                }
                attribute = GENRE_IDS;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute)) {
                    JSONArray genreArrayJSON = movieJSON.getJSONArray(attribute);

                    movie.genre_ids = new int[genreArrayJSON.length()];
                    for (int j = 0; j < genreArrayJSON.length(); j++)
                    {
                        movie.genre_ids[j] = genreArrayJSON.getInt(j);
                    }

                   // movie.genre_ids = movieJSON.getJSONArray(attribute);
                }

                attribute = BACKDROP_PATH;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.backdrop_path = movieJSON.getString(attribute);
                }

                attribute = ADULT;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.adult = movieJSON.getBoolean(attribute);
                }

                attribute = OVERVIEW;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.overview = movieJSON.getString(attribute);
                }

                attribute = RELEASE_DATE;
                if (movieJSON.has(attribute) && !movieJSON.isNull(attribute))
                {
                    movie.release_date =  getDateFromJSONString (movieJSON.getString(attribute));
                }

                moviesContainer.movieList[i] = movie;

            }


        }

        return moviesContainer;
    }
    private static Date getDateFromJSONString(String string) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_IN_API);
        try {
            return format.parse (string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    static private String DATE_FORMAT_IN_API = "yyyy-MM-dd";
    static private String PAGE = "page";
    static private String TOTAL_RESULTS = "total_results";
    static private String TOTAL_PAGES = "total_pages";
    static private String RESULTS = "results";
    static private String ADULT = "adult";
    static private String GENRE_IDS = "genre_ids";
    static private String BACKDROP_PATH = "backdrop_path";
    static private String BELONGS_TO_COLLECTION = "belongs_to_collection";
    static private String BUDGET = "budget";
    static private String GENRES = "genres";
    static private String HOMEPAGE = "homepage";
    static private String ID = "id";
    static private String IMDB_ID = "imdb_id";
    static private String ORIGINAL_LANGUAGE = "original_language";
    static private String ORIGINAL_TITLE = "original_title";
    static private String OVERVIEW = "overview";
    static private String POPULARITY = "popularity";
    static private String POSTER_PATH = "poster_path";
    static private String PRODUCTION_COMPANIES = "production_companies";
    static private String PRODUCTION_COUNTRIES = "production_countries";
    static private String RELEASE_DATE = "release_date";
    static private String REVENUE = "revenue";
    static private String RUNTIME = "runtime";
    static private String SPOKEN_LANGUAGES = "spoken_languages";
    static private String STATUS = "status";
    static private String TAGLINE = "tagline";
    static private String TITLE = "title";
    static private String VIDEO = "video";
    static private String VOTE_AVERAGE = "vote_average";
    static private String VOTE_COUNT = "vote_count";
    static private String NAME = "name";
    static private String ISO_639_1 = "iso_639_1";
    static private String ISO_3166_1 = "iso_3166_1";
    static private String LOGO_PATH = "logo_path";
    static private String ORIGIN_COUNTRY = "origin_country";
    static private String KEY = "key";

    public static MovieDetails parseMovieDetails(String movieDetailsResults) throws JSONException{

        JSONObject movieDetailsJSON = new JSONObject(movieDetailsResults);

        //Check for errors

        if (movieDetailsJSON.has(OWM_MESSAGE_CODE)) {
            int errorCode = movieDetailsJSON.getInt(OWM_MESSAGE_CODE);
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        MovieDetails movieDetails = new MovieDetails();

        String attribute = VOTE_COUNT;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.vote_count = movieDetailsJSON.getInt(attribute);
        }

        attribute = ID;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.id = movieDetailsJSON.getInt(attribute);
        }
        attribute = VIDEO;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.video = movieDetailsJSON.getBoolean(attribute);
        }

        attribute = VOTE_AVERAGE;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.vote_average = movieDetailsJSON.getInt(attribute);
        }

        attribute = TITLE;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.title = movieDetailsJSON.getString(attribute);
        }

        attribute = POPULARITY;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.popularity = movieDetailsJSON.getInt(attribute);
        }

        attribute = POSTER_PATH;

        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.poster_path = movieDetailsJSON.getString(attribute);
        }
        attribute = ORIGINAL_LANGUAGE;

        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.original_language = movieDetailsJSON.getString(attribute);
        }
        attribute = ORIGINAL_TITLE;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.original_title = movieDetailsJSON.getString(attribute);
        }


        attribute = BACKDROP_PATH;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.backdrop_path = movieDetailsJSON.getString(attribute);
        }
        attribute = ADULT;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.adult = movieDetailsJSON.getBoolean(attribute);
        }

        attribute = OVERVIEW;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.overview = movieDetailsJSON.getString(attribute);
        }

        attribute = RELEASE_DATE;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.release_date = getDateFromJSONString (movieDetailsJSON.getString(attribute));
        }

        attribute = STATUS;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.status = movieDetailsJSON.getString(attribute);
        }

        attribute = BUDGET;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.budget= movieDetailsJSON.getInt(attribute);
        }

        attribute = HOMEPAGE;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.homepage= movieDetailsJSON.getString(attribute);
        }
        attribute = IMDB_ID;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.imdb_id= movieDetailsJSON.getString(attribute);
        }

        attribute = REVENUE;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.revenue = movieDetailsJSON.getInt(attribute);
        }

        attribute = RUNTIME;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.runtime = movieDetailsJSON.getInt(attribute);
        }

        attribute = STATUS;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.status = movieDetailsJSON.getString(attribute);
        }


        attribute = VIDEO;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute))
        {
            movieDetails.video = movieDetailsJSON.getBoolean(attribute);
        }

        attribute = BELONGS_TO_COLLECTION;
        if (movieDetailsJSON.has(attribute) && movieDetailsJSON.isNull(attribute) && !movieDetailsJSON.getString(attribute).equals("null"))
        {
            // DONE get collection
            JSONObject collectionJSON = movieDetailsJSON.getJSONObject(attribute);


            Collection collection = new Collection();

            if (collectionJSON !=null)
            {
                if (collectionJSON.has(ID))
                {
                    collection.id = collectionJSON.getInt(ID);
                }
                if (collectionJSON.has(NAME))
                {
                    collection.id = collectionJSON.getInt(NAME);
                }
                if (collectionJSON.has(POSTER_PATH))
                {
                    collection.poster_path = collectionJSON.getString(POSTER_PATH);
                }
                if (collectionJSON.has(BACKDROP_PATH))
                {
                    collection.backdrop_path = collectionJSON.getString(BACKDROP_PATH);
                }
            }

            movieDetails.belongs_to_collection = collection;
        }

        attribute = PRODUCTION_COMPANIES;
        if (movieDetailsJSON.has(attribute) && !movieDetailsJSON.isNull(attribute) && movieDetailsJSON.getString(attribute) != "null")
        {
            JSONArray companyJsonArray= movieDetailsJSON.getJSONArray(attribute);

            movieDetails.production_companies = new Company[companyJsonArray.length()];
            for (int i = 0; i < companyJsonArray.length(); i++) {
                JSONObject companyJSON = companyJsonArray.getJSONObject(i);
                Company company = new Company();
                if (companyJSON.has(ID))
                {
                    company.id = companyJSON.getInt(ID);
                }

                if (companyJSON.has(NAME))
                {
                    company.name = companyJSON.getString(NAME);
                }
                if (companyJSON.has(LOGO_PATH))
                {
                    company.logo_path = companyJSON.getString(LOGO_PATH);
                }
                if (companyJSON.has(ORIGIN_COUNTRY))
                {
                    company.origin_country = companyJSON.getString(ORIGIN_COUNTRY);
                }

                movieDetails.production_companies[i] = company;
            }
            //  DONE companyes
        }

        attribute = SPOKEN_LANGUAGES;
        if (movieDetailsJSON.has(attribute)&& !movieDetailsJSON.isNull(attribute) && movieDetailsJSON.getString(attribute ) != "null")
        {
            JSONArray languageJSONArray = movieDetailsJSON.getJSONArray(attribute);

            movieDetails.spoken_languages = new Language[languageJSONArray.length()];
            for (int i = 0; i < languageJSONArray.length(); i++) {
                JSONObject langJSON = languageJSONArray.getJSONObject(i);
                Language lang = new Language();
                if (langJSON.has(ISO_639_1))
                {
                    lang.iso_639_1 = langJSON.getString(ISO_639_1);
                }

                if (langJSON.has(NAME))
                {
                    lang.name = langJSON.getString(NAME);
                }

                movieDetails.spoken_languages[i] = lang;
            }
            // DONE get lang
        }

        attribute = PRODUCTION_COUNTRIES;
        if (movieDetailsJSON.has(attribute)&& !movieDetailsJSON.isNull(attribute) && movieDetailsJSON.getString(attribute ) != "null")
        {
            JSONArray countryJSONArray = movieDetailsJSON.getJSONArray(attribute);

            movieDetails.production_countries = new Country[countryJSONArray.length()];
            for (int i = 0; i < countryJSONArray.length(); i++) {
                JSONObject countryJSON = countryJSONArray.getJSONObject(i);
                Country country = new Country();
                if (countryJSON.has(ISO_3166_1))
                {
                    country.iso_3166_1 = countryJSON.getString(ISO_3166_1);
                }

                if (countryJSON.has(NAME))
                {
                    country.name = countryJSON.getString(NAME);
                }

                movieDetails.production_countries[i] = country;
            }
        }

        attribute = GENRES;
        if (movieDetailsJSON.has(attribute)&& !movieDetailsJSON.isNull(attribute) && movieDetailsJSON.getString(attribute ) != "null")
        {
            // DONE get genres
            JSONArray genreJSONArray = movieDetailsJSON.getJSONArray(attribute);

            movieDetails.genres = new Genre[genreJSONArray.length()];
            for (int i = 0; i < genreJSONArray.length(); i++) {
                JSONObject genreJSON = genreJSONArray.getJSONObject(i);
                Genre genre = new Genre();
                if (genreJSON.has(ID))
                {
                    genre.id = genreJSON.getInt(ID);
                }

                if (genreJSON.has(NAME))
                {
                    genre.name = genreJSON.getString(NAME);
                }

                movieDetails.genres[i] = genre;
            }
        }
        attribute = TRAILERS;
        if (movieDetailsJSON.has(attribute)&& !movieDetailsJSON.isNull(attribute) && movieDetailsJSON.getString(attribute ) != "null")
        {
            JSONObject trailerJSONObject = movieDetailsJSON.getJSONObject(attribute);

            if (trailerJSONObject.has(RESULTS))
            {
                JSONArray trailerJSONArray = trailerJSONObject.getJSONArray(RESULTS);

                movieDetails.videos = new Videos[trailerJSONArray.length()];
                for (int i = 0; i < trailerJSONArray.length(); i++) {

                    JSONObject trailer = trailerJSONArray.getJSONObject(i);

                    movieDetails.videos[i] = new Videos();
                    // TODO parse trailer object
                    attribute = ID;
                    if (trailer.has(attribute) && !trailer.isNull(attribute))
                    {
                        movieDetails.videos[i].id = trailer.getString(attribute);
                    }

                    attribute = KEY;
                    if (trailer.has(attribute) && !trailer.isNull(attribute))
                    {
                        movieDetails.videos[i].key = trailer.getString(attribute);
                    }
                    attribute = NAME;
                    if (trailer.has(attribute) && !trailer.isNull(attribute))
                    {
                        movieDetails.videos[i].name = trailer.getString(attribute);
                    }
                    attribute = SITE;
                    if (trailer.has(attribute) && !trailer.isNull(attribute))
                    {
                        movieDetails.videos[i].site = trailer.getString(attribute);
                    }
                    attribute = SIZE;
                    if (trailer.has(attribute) && !trailer.isNull(attribute))
                    {
                        movieDetails.videos[i].size = trailer.getInt(attribute);
                    }
                    attribute = TYPE;
                    if (trailer.has(attribute) && !trailer.isNull(attribute))
                    {
                        movieDetails.videos[i].type = trailer.getString(attribute);
                    }
                    ///

                }
            }

        }

        attribute = REVIEWS;
        if (movieDetailsJSON.has(attribute)&& !movieDetailsJSON.isNull(attribute) && movieDetailsJSON.getString(attribute ) != "null")
        {
            JSONObject reviewsJSONObject = movieDetailsJSON.getJSONObject(attribute);

            if (reviewsJSONObject.has(RESULTS))
            {
                JSONArray reviewsJSONArray = reviewsJSONObject.getJSONArray(RESULTS);

                movieDetails.reviews = new Reviews[reviewsJSONArray.length()];

                for (int i = 0; i < reviewsJSONArray.length(); i++) {

                    JSONObject review = reviewsJSONArray.getJSONObject(i);

                    movieDetails.reviews[i] = new Reviews();

                    attribute = AUTHOR;
                    if (review.has(attribute) && !review.isNull(attribute))
                    {
                        movieDetails.reviews[i].author = review.getString(attribute);
                    }
                    attribute = CONTENT;
                    if (review.has(attribute) && !review.isNull(attribute))
                    {
                        movieDetails.reviews[i].content = review.getString(attribute);
                    }

                    attribute = URL;
                    if (review.has(attribute) && !review.isNull(attribute))
                    {
                        movieDetails.reviews[i].url = review.getString(attribute);
                    }
                }
            }
        }

        return movieDetails;
    }
}
