package com.gevcorst.popularmoviesstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

@SuppressWarnings("unused")
public class ReviewAdapter extends  RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static final String TAG =  ReviewAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<String> mReviewList;
    private static int viewHolderCount;

    public ReviewAdapter(int itemNumber, List<String> reviews, Context context) {
        super();
       mContext = context;
       mReviewList = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_view_holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        ReviewAdapter.ViewHolder textViewHolder = new ReviewAdapter.ViewHolder(view);

        viewHolderCount++;

        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return textViewHolder;

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder textViewHolder, int i) {
        String review  = mReviewList.get(i);
        textViewHolder.bind(review,textViewHolder.textView);
        Log.d("ADAPTER BINDER",review);
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        final Context context;
        final TextView textView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = mContext;
            textView = itemView.findViewById(R.id.reviewsViewHolder);
        }

       void bind(String review, TextView view){
            view.setText(review);
       }
    }
}
