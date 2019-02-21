package com.example.user.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.popularmoviesapp.FavDatabase.MovieEntery;
import com.example.user.popularmoviesapp.Utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{

    final private ItemClickListener mItemClickListener;
    private Context mContext;

    private List<MovieEntery> mMovieEntries;

    public FavoriteAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    public List<MovieEntery> getFavoriteMovies() {
        return mMovieEntries;
    }

    public void setFavoriteMovies(List<MovieEntery> movieEnteries) {
        mMovieEntries = movieEnteries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.favorite_layout, viewGroup, false);

        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int i) {
        MovieEntery movieEntry = mMovieEntries.get(i);

        favoriteViewHolder.titleTV.setText(movieEntry.getMovieName());
        Picasso.with(mContext).load(NetworkUtilities.MOVIES_POSTER + movieEntry.getPosterPath()).into(favoriteViewHolder.posterIV);
    }

    @Override
    public int getItemCount() {
        if (mMovieEntries == null) {
            return 0;
        }
        return mMovieEntries.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView titleTV;
        ImageView posterIV;

        FavoriteViewHolder (View itemView)
        {
            super(itemView);

            titleTV= (TextView) itemView.findViewById(R.id.tv_favorite);
            posterIV= (ImageView) itemView.findViewById(R.id.iv_favorite_poster);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            // TODO navigate to Details activity
            int movieId = mMovieEntries.get(getAdapterPosition()).getMovieId();
            mItemClickListener.onItemClickListener(movieId);
        }
    }
}
