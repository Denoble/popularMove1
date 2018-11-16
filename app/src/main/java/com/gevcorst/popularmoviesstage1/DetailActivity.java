package com.gevcorst.popularmoviesstage1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gevcorst.popularmoviesstage1.Model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private ImageView mImageView;
    private TextView mDateTextView;
    private TextView mSynopsisTextView;
    private TextView mRatingTextView;
    private TextView mTitleTextView;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        configureViews();
        setViewsData();

    }

    /**
     * Find views in the activity_layout
     * to be used in DetailActivity class
     */
    private void configureViews(){
        mImageView = findViewById(R.id.iv_thumbnail);
        mTitleTextView = findViewById(R.id.tv_title);
        mDateTextView = findViewById(R.id.tv_date);
        mRatingTextView = findViewById(R.id.tv_rating);
        mSynopsisTextView = findViewById(R.id.tv_synopsis);
    }

    /**
     * Sets Data to the DetailActivity views
     */
    @SuppressLint("SetTextI18n")
    private void setViewsData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
            movie = bundle.getParcelable("MOVIE_KEY");
        StringBuilder mStringBuilder = new StringBuilder();
        String mImageUrl = "http://image.tmdb.org/t/p/";
        mStringBuilder.append(mImageUrl);
        String mImageSize = "w185/";
        mStringBuilder.append(mImageSize);
        mStringBuilder.append(movie.getThumbnail());
        String completeImageUrl = mStringBuilder.toString();
        Picasso.get().load(completeImageUrl).into(mImageView);
        mTitleTextView.setText(movie.getOriginalTitle());
        mDateTextView.setText(movie.getSReleaseDate());
        mRatingTextView.setText(movie.getUserRating().toString());
        mSynopsisTextView.setText(movie.getSynopsis());
    }
}
