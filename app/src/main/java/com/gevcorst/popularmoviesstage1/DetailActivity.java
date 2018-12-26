package com.gevcorst.popularmoviesstage1;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gevcorst.popularmoviesstage1.Database.FavoriteDao;
import com.gevcorst.popularmoviesstage1.Database.UserFavoriteDataBase;
import com.gevcorst.popularmoviesstage1.Database.UsersFavorite;
import com.gevcorst.popularmoviesstage1.Model.Movie;
import com.gevcorst.popularmoviesstage1.Model.MovieViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ImageView mImageView;
    private TextView mDateTextView;
    private TextView mSynopsisTextView;
    private TextView mRatingTextView;
    private TextView mTitleTextView;
    private TextView mDuration,favoriteTextView,forDuration;
    private Movie movie;
    private Button playTrailer1,playTrailer2,favoriteButton;
    private UserFavoriteDataBase mFavoriteMovies;
    private RecyclerView mReviewList;
    private MovieViewModel movieViewModel;
    private List<String> mReviews;
    private boolean isAFavorite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mFavoriteMovies = UserFavoriteDataBase.getInstance(getApplicationContext());
        configureViews();
        setViewsData();

    }

    /**
     * Find views in the activity_layout
     * to be used in DetailActivity class
     */
    private void configureViews() {
        mImageView = findViewById(R.id.iv_thumbnail);
        mTitleTextView = findViewById(R.id.tv_title);
        mDateTextView = findViewById(R.id.tv_date);
        mRatingTextView = findViewById(R.id.tv_rating);
        favoriteTextView = findViewById(R.id.tv_forFavorite);
        favoriteButton = findViewById(R.id.favoriteButton);
        mSynopsisTextView = findViewById(R.id.tv_synopsis);
        mDuration = findViewById(R.id.tv_duration);
        forDuration = findViewById(R.id.tv_forDuration);
        playTrailer1 = findViewById(R.id.videoView);
        playTrailer2 = findViewById(R.id.videoView2);
        mReviewList = findViewById(R.id.recyclerView);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(this);
        mlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mReviewList.setLayoutManager(mlayoutManager);
        mReviewList.setHasFixedSize(true);
        forDuration.setVisibility(View.GONE);
        mDuration.setVisibility(View.GONE);
    }
    private void setupAdapter(List<String> reviews) {
        ReviewAdapter mAdapter = new ReviewAdapter(reviews.size(),reviews, getApplicationContext());

        mReviewList.setAdapter(mAdapter);
    }

    /**
     * Sets Data to the DetailActivity views
     */

    private void setViewsData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
            movie = bundle.getParcelable("MOVIE_KEY");
        movieViewModel =
                ViewModelProviders.of(this).get(MovieViewModel.class);

        String mImageUrl = "http://image.tmdb.org/t/p/";
        String mImageSize = "w342/";
        String completeImageUrl = mImageUrl +
                mImageSize +
                movie.getThumbnail();
        Picasso.get().load(completeImageUrl).into(mImageView);
        mTitleTextView.setText(movie.getOriginalTitle());
        mDateTextView.setText(movie.getSReleaseDate());
        mRatingTextView.setText(movie.getUserRating().toString());
        mSynopsisTextView.setText(movie.getSynopsis());
        setUpMovieTrailer();
        setMovieReviews();
        monitorFavoriteInDataBase();


    }
    private void setUpMovieTrailer(){
        movieViewModel.getMovieTrailers(movie).observe(this, movie -> {
            mDuration.setText(movie.getDuration());
            playTrailer1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  String   youtubeUrl = movie.getYoutubeURL();
                   playTrailer(playTrailer1,youtubeUrl);
                }
            });
            playTrailer2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String   youtubeUrl = movie.getYoutubeURL2();
                    if(youtubeUrl == null){
                        playTrailer2.setVisibility(View.GONE);
                        Toast toast = Toast.makeText(getApplicationContext(),"No trailer link available", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else{
                        playTrailer(playTrailer2,youtubeUrl);
                    }

                }
            });
        });
    }
    private void setMovieReviews(){
        movieViewModel.getMovieReviews(movie).observe(this, movie -> {
            mReviews = movie.getReviews();
                setupAdapter(mReviews);


        });
    }
    private void monitorFavoriteInDataBase(){
        movieViewModel.getFavoriteList().observe(this, new Observer<List<UsersFavorite>>() {
            final String favoriteTextViewText = "MARK AS FAVORITE";
            final String favoriteTextViewText2 = "REMOVE AS A FAVORITE";
            @Override
            public void onChanged(@Nullable List<UsersFavorite> usersFavorites) {
                assert usersFavorites != null;
                UsersFavorite usersFavorite = usersFavorites.stream()
                        .filter( u -> u.getMovieId() == movie.getMovieId()).findAny().orElse(null);
                favoriteButton = findViewById(R.id.favoriteButton);
                if(usersFavorite == null){
                    isAFavorite = false;
                    favoriteTextView.setText(favoriteTextViewText);
                    favoriteButton.setBackgroundResource(R.drawable.favorite_boarder);
                }
                else{
                    isAFavorite = true;
                    favoriteTextView.setText(favoriteTextViewText2);
                    favoriteButton.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
                }
                favoriteButton.setOnClickListener(view -> {
                    if(isAFavorite){
                        removeFromFavoriteMovies(movie.getMovieId());
                    }
                    else{
                        addToFavoriteMovies();
                    }
                });
            }
        });
    }
    private void addToFavoriteMovies() {
        final UsersFavorite   usersFavorite = new UsersFavorite(movie.getOriginalTitle(),
                movie.getMovieId());
        AppExecutors.getInstance().diskIO().execute(() -> mFavoriteMovies.favoriteDao().insertFavorite(usersFavorite));
    }

    private void removeFromFavoriteMovies(int movieId) {
        AppExecutors.getInstance().diskIO().execute(() -> mFavoriteMovies.favoriteDao().deleteById(movieId));
    }
    private void playTrailer(View view, String youTubeUrl){
        openYoutubePage( youTubeUrl);
    }
    private void openYoutubePage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
