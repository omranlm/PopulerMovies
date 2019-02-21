package com.example.user.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.popularmoviesapp.Utilities.Videos;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailersAdapterViewHolder>
{
    private Videos [] mTrailer;
    private final TrailerAdapterOnClickHandler mClickHandler;
    private Context _context;

    public TrailerAdapter(TrailerAdapterOnClickHandler handler, Context context) {
        mClickHandler = handler;
        _context = context;
    }

    interface TrailerAdapterOnClickHandler
    {
        void onClick(Videos videos);
    }
    public void setTrailersData(Videos[] trailer) {

        mTrailer = trailer;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.trailer_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,viewGroup,false);

        return new TrailersAdapterViewHolder(view);
        
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapterViewHolder trailersAdapterViewHolder, int i) {
        if (mTrailer != null)
            trailersAdapterViewHolder.bind(mTrailer[i]);
    }

    @Override
    public int getItemCount() {
        if (mTrailer == null) return 0;
        return mTrailer.length;
    }
    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTrailerTV;

        TrailersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mTrailerTV = itemView.findViewById(R.id.tv_trailer_title);

            itemView.setOnClickListener(this);

        }

        public void bind(Videos trailer) {
            mTrailerTV.setText(trailer.name);
        }


        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mTrailer[getAdapterPosition()]);
            Toast.makeText(_context,mTrailer[getAdapterPosition()].name + " Clicked",Toast.LENGTH_LONG).show();
        }
    }
}


