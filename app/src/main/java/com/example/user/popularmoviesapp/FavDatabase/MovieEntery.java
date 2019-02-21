package com.example.user.popularmoviesapp.FavDatabase;



import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "movie")
public class MovieEntery {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int movieId;

    private String movieName;

    private String posterPath;

    @Ignore
    public MovieEntery(int movieId,String movieName, String posterPath) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.posterPath = posterPath;
    }


    public MovieEntery(int id, int movieId,String movieName, String posterPath) {
        this.id = id;
        this.movieId = movieId;
        this.movieName = movieName;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName(){return movieName;}

    public void setMovieName(String movieName){this.movieName = movieName;}

    public String getPosterPath(){return posterPath;}

    public void setPosterPath(String posterPath){this.posterPath = posterPath;}
}