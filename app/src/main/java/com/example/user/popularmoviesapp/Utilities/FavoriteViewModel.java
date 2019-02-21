package com.example.user.popularmoviesapp.Utilities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.user.popularmoviesapp.FavDatabase.AppDatabase;
import com.example.user.popularmoviesapp.FavDatabase.MovieEntery;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntery>> favorites;
    public FavoriteViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(this.getApplication());

        favorites = db.taskDao().loadAllFav();

    }

    public LiveData<List<MovieEntery>> getFavorites()
    {
        return favorites;
    }
}
