package com.example.user.popularmoviesapp.Utilities;

import java.util.Date;

public class MoviesContainer {

    public int page;
    public int total_results;
    public int total_pages;

    public Movie movieList [];

}

class Movie {
    public int vote_count ;
    public int id;
    public boolean video;
    public double vote_average;
    public String title;
    public double popularity;
    public String poster_path;
    public String original_language;
    public String original_title;
    public int [] genre_ids;
    public String backdrop_path;
    public boolean adult;
    public String overview;
    public Date release_date;

}