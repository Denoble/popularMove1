package com.gevcorst.popular_movies_in_theaters;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.gevcorst.popular_movies_in_theaters.Database.UserFavoriteDataBase;
import com.gevcorst.popular_movies_in_theaters.Database.UsersFavorite;
import com.gevcorst.popular_movies_in_theaters.Model.Movie;
import com.gevcorst.popular_movies_in_theaters.Model.MovieViewModel;
import com.gevcorst.popular_movies_in_theaters.Model.Result;
import com.gevcorst.popular_movies_in_theaters.Model.TheMovie;
import com.gevcorst.popular_movies_in_theaters.Utilities.ImageLoader;
import com.gevcorst.popular_movies_in_theaters.Utilities.JsonUtil;
import com.gevcorst.popular_movies_in_theaters.databinding.ActivityDetailBinding;
import com.gevcorst.popular_movies_in_theaters.viewModel.MainMovieViewModel;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
  private ImageView mImageView;
  private TextView mDateTextView;
  private TextView mSynopsisTextView;
  private TextView mRatingTextView;
  private TextView mTitleTextView;
  private TextView mDuration;
  private TextView favoriteTextView;
  private TheMovie movie;
  private Button playTrailer1, playTrailer2, favoriteButton;
  private UserFavoriteDataBase mFavoriteMovies;
  private TextView mReviewList;
  private MovieViewModel movieViewModel;
  private List<Result> mReviews;
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
  private void configureViews() {
    mImageView = findViewById(R.id.iv_thumbnail);
    mTitleTextView = findViewById(R.id.tv_title);
    mDateTextView = findViewById(R.id.tv_date);
    mRatingTextView = findViewById(R.id.tv_rating);
    favoriteTextView = findViewById(R.id.tv_forFavorite);
    favoriteButton = findViewById(R.id.favoriteButton);
    mSynopsisTextView = findViewById(R.id.tv_synopsis);
    mDuration = findViewById(R.id.tv_duration);
    TextView forDuration = findViewById(R.id.tv_forDuration);
    playTrailer1 = findViewById(R.id.videoView);
    playTrailer2 = findViewById(R.id.videoView2);
    mReviewList = findViewById(R.id.recyclerView);
    forDuration.setVisibility(View.GONE);
    mDuration.setVisibility(View.GONE);
  }

  /**
   * Sets Data to the DetailActivity views
   */

  private void setViewsData() {
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
      if (bundle != null) {
          movie = bundle.getParcelable("MOVIE_KEY");
      }
      mainMovieViewModel.fetchVideos(movie.getId());
    movieViewModel =
        ViewModelProviders.of(this).get(MovieViewModel.class);

    String mImageUrl = "http://image.tmdb.org/t/p/";
    String mImageSize = "w342/";
    String completeImageUrl = mImageUrl +
        mImageSize +
        movie.getPosterPath();
    ImageLoader.INSTANCE.bindImage(binding.tumbnail.ivThumbnail,completeImageUrl);
    mTitleTextView.setText(movie.getOriginalTitle());
    mDateTextView.setText(movie.getReleaseDate());
    mRatingTextView.setText(String.format(Locale.getDefault(), movie.getPosterPath().toString()));
    mSynopsisTextView.setText(movie.getOverview());
    setUpMovieTrailer();
    setMovieReviews();
    monitorFavoriteInDataBase();
  }

  private void setUpMovieTrailer() {
    mainMovieViewModel.getVideos().observe(this,videos -> {
      playTrailer1.setOnClickListener(view -> {
        String youtubeUrl = videos.getResults().get(0).getKey();;
        Log.i("YouTube",youtubeUrl);
        playTrailer(playTrailer1,JsonUtil.YOUTUBE_URL+ youtubeUrl);
      });
      playTrailer2.setOnClickListener(view -> {
        String youtubeUrl = videos.getResults().get(1).getKey();
        if (youtubeUrl == null) {
          playTrailer2.setVisibility(View.GONE);
          Toast toast = Toast.makeText(getApplicationContext(), "No trailer link available",
                  Toast.LENGTH_LONG);
          toast.show();
        } else {
          playTrailer(playTrailer2, JsonUtil.YOUTUBE_URL+ youtubeUrl);
        }
      });
    });
  }

  private void setMovieReviews() {
    mainMovieViewModel.fetchReviews(movie.getId());
    mainMovieViewModel.getReviews().observe(this,review -> {
      mReviews = review.getResults();
      StringBuilder sb = new StringBuilder();
      for (Result s : mReviews) {
        sb.append(s.getAuthor());
        sb.append("\n\n");
        sb.append(s.getContent());
        sb.append("\n");
      }
      mReviewList.setText(sb.toString());
      mReviewList.setMovementMethod(new ScrollingMovementMethod());
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
        favoriteButton = findViewById(R.id.favoriteButton);
        if (usersFavorite == null) {
          isAFavorite = false;
          favoriteTextView.setText(favoriteTextViewText);
          favoriteButton.setBackgroundResource(R.drawable.favorite_boarder);
        } else {
          isAFavorite = true;
          favoriteTextView.setText(favoriteTextViewText2);
          favoriteButton.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
        }
        favoriteButton.setOnClickListener(view -> {
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
          @Override public void onClick(DialogInterface dialogInterface, int i) {
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


  }

  @Override
  protected void onStart() {
    super.onStart();
    mFavoriteMovies = UserFavoriteDataBase.getInstance(getApplicationContext());
    configureViews();
    setViewsData();
  }
}
