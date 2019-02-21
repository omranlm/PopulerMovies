package com.example.user.popularmoviesapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.user.popularmoviesapp.FavDatabase.AppDatabase;
import com.example.user.popularmoviesapp.FavDatabase.MovieEntery;
import com.example.user.popularmoviesapp.Utilities.FavoriteViewModel;

import java.util.List;

import static android.widget.GridLayout.VERTICAL;

public class FavoriteActivity extends AppCompatActivity implements FavoriteAdapter.ItemClickListener{

    private RecyclerView mRecyclerView;
    private FavoriteAdapter mFavoriteAdapter;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        setTitle(getResources().getString(R.string.favorite_activity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRecyclerView = findViewById(R.id.rv_favorite_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFavoriteAdapter = new FavoriteAdapter(this,this);
        mRecyclerView.setAdapter(mFavoriteAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        mDb = AppDatabase.getInstance(getApplicationContext());
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int swipeDir) {
                // TODO Delete from Fav DB
                final int position = viewHolder.getAdapterPosition();
                final List<MovieEntery> favoriteMovies = mFavoriteAdapter.getFavoriteMovies();

               Thread thread = new Thread((new Runnable() {
                    @Override
                    public void run() {
                        mDb.taskDao().deleteMovie(favoriteMovies.get(position));
                    }
                })) ;
                thread.start();
            }
        }).attachToRecyclerView(mRecyclerView);




        setupViewModel();
    }

    private void setupViewModel() {

        FavoriteViewModel favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        favoriteViewModel.getFavorites().observe(this, new Observer<List<MovieEntery>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntery> movieEnteries) {
                mFavoriteAdapter.setFavoriteMovies(movieEnteries);
            }
        });
    }

    @Override
    public void onItemClickListener(int movieId) {
        // TODO implementation
        Class<DetailActivity> destinationActivity = DetailActivity.class;
        Intent intent = new Intent(FavoriteActivity.this, destinationActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.putExtra(Intent.EXTRA_INDEX, movieId);
        }
        startActivity(intent);
    }
}
