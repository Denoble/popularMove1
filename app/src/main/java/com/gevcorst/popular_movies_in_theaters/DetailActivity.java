package com.gevcorst.popular_movies_in_theaters;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BuildCompat;
import androidx.lifecycle.ViewModelProvider;

import com.gevcorst.popular_movies_in_theaters.Database.AppDatabase;
import com.gevcorst.popular_movies_in_theaters.Model.Movie;
import com.gevcorst.popular_movies_in_theaters.Model.MovieReview;
import com.gevcorst.popular_movies_in_theaters.Utilities.ImageLoader;
import com.gevcorst.popular_movies_in_theaters.Utilities.JsonUtil;
import com.gevcorst.popular_movies_in_theaters.databinding.ActivityDetailBinding;
import com.gevcorst.popular_movies_in_theaters.viewModel.MainMovieViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    final String favoriteTextViewText = "MARK AS FAVORITE";
    final String favoriteTextViewText2 = "REMOVE AS A FAVORITE";
    private com.gevcorst.popular_movies_in_theaters.Model.Movie movie;
    private AppDatabase db;
    private TextView mReviewList;
    private List<MovieReview> mReviews;
    private MainMovieViewModel mainMovieViewModel;
    private ActivityDetailBinding binding;
    private Boolean isAFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainMovieViewModel = new ViewModelProvider(this).get(MainMovieViewModel.class);
        db = AppDatabase.Companion.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setViewsData();
        getSupportActionBar().setTitle(movie.getTitle());
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorAccent,
                        getTheme())));
        mainMovieViewModel.fetchReviews(movie.getMovieId());
        mainMovieViewModel.fetchVideos(movie.getMovieId());
        mainMovieViewModel.checkIsFaveMovie(db, movie.getMovieId());

    }

    @Override
    protected void onResume() {
        super.onResume();
        observeFavoriteState();
        setUpMovieTrailer();
        setMovieReviews();
        binding.tumbnail.favoriteButton.setOnClickListener(view -> {
            Log.i("IsFave", isAFavorite.toString());
            if (!isAFavorite) {
                addToFavoriteMovies(movie);

            } else {
                removeFromFavoriteMovies(movie);
            }
        });

    }


    private void removeFaveIcon() {
        binding.tumbnail.tvForFavorite.setText(favoriteTextViewText);
        binding.tumbnail.favoriteButton.setBackgroundResource(
                R.drawable.favorite_boarder);
    }

    private void addFavoriteIcon() {
        binding.tumbnail.tvForFavorite.setText(favoriteTextViewText2);
        binding.tumbnail.favoriteButton.setBackgroundResource(
                R.drawable.ic_favorite_black_48dp);
    }

    private void setViewsData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            movie = bundle.getParcelable("MOVIE_KEY");
        }
        String mImageUrl = "http://image.tmdb.org/t/p/";
        String mImageSize = "w342/";
        String completeImageUrl = mImageUrl +
                mImageSize +
                movie.getBackdropPath();
        ImageLoader.INSTANCE.bindImage(binding.tumbnail.ivThumbnail, completeImageUrl);
        binding.tumbnail.tvDate.setText(movie.getReleaseDate());
        binding.tumbnail.tvRating.setText(String.valueOf(
                movie.getVoteAverage()));
        binding.synopsis.tvSynopsis.setText(movie.getOverview());
    }

    private void setUpMovieTrailer() {
        mainMovieViewModel.getVideos().observe(this, videos -> {
            binding.trailer.videoView.setOnClickListener(view -> {
                String youtubeUrl = videos.getResults().get(0).getKey();
                ;
                Log.i("YouTube", youtubeUrl);
                playTrailer(binding.trailer.videoView,
                        JsonUtil.YOUTUBE_URL + youtubeUrl);
            });
            binding.trailer.videoView2.setOnClickListener(view -> {
                String youtubeUrl = videos.getResults().get(1).getKey();
                if (youtubeUrl == null) {
                    binding.trailer.videoView2.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), "No trailer link available",
                            Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    playTrailer(binding.trailer.videoView2,
                            JsonUtil.YOUTUBE_URL + youtubeUrl);
                }
            });
        });
    }

    private void setMovieReviews() {
        mainMovieViewModel.getReviews().observe(this, review -> {
            var adapter = new MovieReviewAdapter(review,
                    this);
            binding.trailer.movieReviews.setAdapter(adapter);
        });
    }


    private void addToFavoriteMovies(Movie movie) {
        try {
            mainMovieViewModel.addToFavorite(movie, db);
            Log.i("AddsToDb", "Movie Added");
        } catch (Exception e) {
            Log.i("AddsToDb", Arrays.toString(e.getStackTrace()));
        }
    }

    private void removeFromFavoriteMovies(Movie movie) {
        try {
            mainMovieViewModel.removeFromFavorite(movie, db);
        } catch (Exception e) {
            Log.i("AddsToDb", Arrays.toString(e.getStackTrace()));
        }
    }

    private void playTrailer(View view, String youTubeUrl) {
        openYoutubePage(youTubeUrl);
    }

    private void openYoutubePage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        try {
            startActivity(intent);
        } catch (Exception exception) {
            AlertDialog.Builder alertDialog = createAlertDiaglog(exception.getMessage());
            alertDialog.create();
            alertDialog.setTitle("Error");
            alertDialog.show();
        }
    }

    private AlertDialog.Builder createAlertDiaglog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
           /* .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            })*/;
        return alertDialog;
    }


    private void observeFavoriteState() {
        mainMovieViewModel.getFavoriteState().observe(this, favState -> {
            isAFavorite = favState;
            if (isAFavorite) {
                addFavoriteIcon();
            } else {
                removeFaveIcon();
            }
        });
    }
}