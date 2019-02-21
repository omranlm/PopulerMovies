package com.example.user.popularmoviesapp.FavDatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie")
    LiveData<List<MovieEntery>> loadAllFav();

    @Query("SELECT * FROM movie WHERE movieId = :movieId")
    LiveData<MovieEntery> getMovie(int movieId);

    @Query("SELECT * FROM movie WHERE movieId = :movieId")
    MovieEntery getMovieToDelete(int movieId);


    @Insert
    void insertMovie(MovieEntery movieEntery);

    @Delete
    void deleteMovie(MovieEntery movieEntery);
}
