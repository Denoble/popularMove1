package com.gevcorst.popular_movies_in_theaters;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gevcorst.popular_movies_in_theaters.Database.UserFavoriteDataBase;
import com.gevcorst.popular_movies_in_theaters.Database.UsersFavorite;
import com.gevcorst.popular_movies_in_theaters.viewModel.MovieViewModel;
import com.gevcorst.popular_movies_in_theaters.Model.MovieReview;
import com.gevcorst.popular_movies_in_theaters.Model.TheMovie;
import com.gevcorst.popular_movies_in_theaters.Utilities.ImageLoader;
import com.gevcorst.popular_movies_in_theaters.Utilities.JsonUtil;
import com.gevcorst.popular_movies_in_theaters.databinding.ActivityDetailBinding;
import com.gevcorst.popular_movies_in_theaters.viewModel.MainMovieViewModel;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private TheMovie movie;
    private UserFavoriteDataBase mFavoriteMovies;
    private TextView mReviewList;
    private MovieViewModel movieViewModel;
    private List<MovieReview> mReviews;
    private boolean isAFavorite;
    private MainMovieViewModel mainMovieViewModel;
    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainMovieViewModel = ViewModelProviders.of(
                this).get(MainMovieViewModel.class);

    }

    /**
     * Find views in the activity_layout
     * to be used in DetailActivity class
     */
    /**
     * Sets Data to the DetailActivity views
     */

    private void setViewsData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            movie = bundle.getParcelable("MOVIE_KEY");
        }
        movieViewModel =
                ViewModelProviders.of(this).get(MovieViewModel.class);

        String mImageUrl = "http://image.tmdb.org/t/p/";
        String mImageSize = "w342/";
        String completeImageUrl = mImageUrl +
                mImageSize +
                movie.getBackdropPath();
        ImageLoader.INSTANCE.bindImage(binding.tumbnail.ivThumbnail, completeImageUrl);
        binding.tvTitle.setText(movie.getOriginalTitle());
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


    private void monitorFavoriteInDataBase() {
        movieViewModel.getFavoriteList().observe(this, new Observer<List<UsersFavorite>>() {
            final String favoriteTextViewText = "MARK AS FAVORITE";
            final String favoriteTextViewText2 = "REMOVE AS A FAVORITE";

            @Override
            public void onChanged(@Nullable List<UsersFavorite> usersFavorites) {
                assert usersFavorites != null;
                UsersFavorite usersFavorite = usersFavorites.stream()
                        .filter(u -> u.getMovieId() == movie.getId()).findAny().orElse(null);
                if (usersFavorite == null) {
                    isAFavorite = false;
                    binding.tumbnail.tvForFavorite.setText(favoriteTextViewText);
                    binding.tumbnail.favoriteButton.setBackgroundResource(R.drawable.favorite_boarder);
                } else {
                    isAFavorite = true;
                    binding.tumbnail.tvForFavorite.setText(favoriteTextViewText2);
                    binding.tumbnail.favoriteButton.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
                }
                binding.tumbnail.favoriteButton.setOnClickListener(view -> {
                    if (isAFavorite) {
                        removeFromFavoriteMovies(movie.getId());
                    } else {
                        addToFavoriteMovies();
                    }
                });
            }
        });
    }

    private void addToFavoriteMovies() {
        final UsersFavorite usersFavorite = new UsersFavorite(movie.getOriginalTitle(),
                movie.getId());
        AppExecutors.getInstance()
                .diskIO()
                .execute(() -> mFavoriteMovies.favoriteDao().insertFavorite(usersFavorite));
    }

    private void removeFromFavoriteMovies(int movieId) {
        AppExecutors.getInstance()
                .diskIO()
                .execute(() -> mFavoriteMovies.favoriteDao().deleteById(movieId));
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

    @Override
    protected void onResume() {
        super.onResume();
        setUpMovieTrailer();
        setMovieReviews();
        monitorFavoriteInDataBase();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mFavoriteMovies = UserFavoriteDataBase.getInstance(getApplicationContext());
        setViewsData();
        mainMovieViewModel.fetchReviews(movie.getId());
        mainMovieViewModel.fetchVideos(movie.getId());
    }
}
