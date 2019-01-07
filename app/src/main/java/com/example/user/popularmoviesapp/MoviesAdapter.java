package com.example.user.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.popularmoviesapp.Utilities.Movie;
import com.example.user.popularmoviesapp.Utilities.MoviesContainer;
import com.example.user.popularmoviesapp.Utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.nio.charset.Charset;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private MoviesContainer mMovies;

    private MoviesAdapterOnClickHandler mClickHandler;

    private Context _context;

    public void setMoviesData(MoviesContainer container) {

        mMovies = container;
        notifyDataSetChanged();
    }

    interface MoviesAdapterOnClickHandler
    {
        void onClick(int movieId);
    }
    public MoviesAdapter(MoviesAdapterOnClickHandler holder, Context context)
    {
        mClickHandler = holder;
        _context = context;
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.movie_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,viewGroup,false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder moviesAdapterViewHolder, int i) {

        if (mMovies.movieList != null)
            moviesAdapterViewHolder.bind(mMovies.movieList[i]);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) return 0;
        return mMovies.movieList.length;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView mMovieImage ;
        TextView mTitleTextView;
        TextView mPopularityTextView;
        TextView mVoteTextView;
        TextView mOverviewTextView;
        TextView mReleaseDate;

        private CharSequence DATE_FORMAT = "dd MMM yyyy";
        public MoviesAdapterViewHolder (View view)
        {
            super(view);

            // TODO set the whole item design
            mMovieImage = (ImageView) view.findViewById(R.id.image_iv);
            mTitleTextView = (TextView) view.findViewById(R.id.tv_movie_title);
            mPopularityTextView = (TextView) view.findViewById(R.id.tv_popularity);
            mVoteTextView = (TextView) view.findViewById(R.id.tv_votes);
            mOverviewTextView = (TextView) view.findViewById(R.id.tv_overview);
            mReleaseDate = (TextView) view.findViewById(R.id.tv_release_date);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // TODO implement movie click
            int clickedPosition = getAdapterPosition();
            mClickHandler.onClick(mMovies.movieList[clickedPosition].id);
        }

        public void bind(Movie movie) {
            Picasso.with(_context).load(NetworkUtilities.MOVIES_POSTER + movie.poster_path).into(mMovieImage);
            mTitleTextView.setText(movie.title);

            //if (movie.popularity != null)
            mPopularityTextView.setText(String.valueOf(movie.popularity));
            mVoteTextView.setText(String.valueOf(movie.vote_count));

            String releaseDateString = String.valueOf(android.text.format.DateFormat.format(DATE_FORMAT,movie.release_date));

            mReleaseDate.setText(releaseDateString);

            if (movie.overview != null && movie.overview.length() > 200)
            {
                String shortOverview = movie.overview.substring(0,200) + "...";
                mOverviewTextView.setText(shortOverview);
            }
            else
                mOverviewTextView.setText(movie.overview);

        }
    }
}
