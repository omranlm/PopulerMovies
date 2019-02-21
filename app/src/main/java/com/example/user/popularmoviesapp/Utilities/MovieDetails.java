package com.example.user.popularmoviesapp.Utilities;

public class MovieDetails extends Movie{

    public int budget;
    public String homepage;
    public String imdb_id;
    public Company[] production_companies;
    public Country[] production_countries;
    public int revenue;
    public int runtime;
    public Language[] spoken_languages;
    public String status;
    public String tagline;
    public Genre[] genres;
    public Collection belongs_to_collection;
    public boolean video;

    public Videos [] videos;
    public Reviews [] reviews;

    public MovieDetails ()
    {}
}


class Collection {
    public int id;
    public String name;
    public String poster_path;
    public String backdrop_path;

}
class Company {
    public int id;
    public String logo_path;
    public String name;
    public String origin_country;

}

class Country {
    public String iso_3166_1;
    public String name;
}



