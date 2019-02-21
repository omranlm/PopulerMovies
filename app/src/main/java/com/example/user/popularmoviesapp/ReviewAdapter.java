package com.example.user.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.popularmoviesapp.Utilities.Reviews;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapterViewHolder>
{
    private Reviews [] mReview;

    private Context _context;

    public ReviewAdapter(Context context) {
        _context = context;
    }

    public void setReviewsData(Reviews[] reviews) {

        mReview = reviews;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.review_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,viewGroup,false);

        return new ReviewAdapterViewHolder(view,mReview[i]);
        
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder reviewAdapterViewHolder, int i) {
        if (mReview != null)
            reviewAdapterViewHolder.bind(mReview[i],i,mReview.length);
    }

    @Override
    public int getItemCount() {
        if (mReview == null) return 0;
        return mReview.length;
    }
}

class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
    TextView mAutherTV;
    TextView mContentTV;
    TextView mLinkTV;
    ImageView mSwipLeft;

    Reviews mReview;
    private static String READ_MORE ="Read more on ";

    public ReviewAdapterViewHolder(@NonNull View itemView, Reviews reviews) {
        super(itemView);

        mAutherTV = itemView.findViewById(R.id.tv_author);
        mContentTV = itemView.findViewById(R.id.tv_content);
        mLinkTV = itemView.findViewById(R.id.tv_link    );
        mSwipLeft = itemView.findViewById(R.id.iv_swip_left);
        mReview = reviews;
    }

    public void bind(Reviews reviews,int position,int length) {

        mAutherTV.setText(reviews.author);
        mContentTV.setText(reviews.content);
        mLinkTV.setText(READ_MORE + reviews.url);
        if (position+1 == length)
        {
            // Last item
            mSwipLeft.setVisibility(View.INVISIBLE);
        }
    }
}
