package com.example.user.popularmoviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    TextView mMovieIdTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mMovieIdTextView = (TextView)findViewById(R.id.tv_movieId);

        Intent detailsIntent = getIntent();

        if (detailsIntent.hasExtra(Intent.EXTRA_INDEX)) {
            int movieId = detailsIntent.getIntExtra(Intent.EXTRA_INDEX,0);
            mMovieIdTextView.setText(String.valueOf(movieId));


        }
    }
}
