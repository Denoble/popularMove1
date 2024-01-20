package com.gevcorst.popular_movies_in_theaters;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.gevcorst.popular_movies_in_theaters.Database.AppDatabase;
import com.gevcorst.popular_movies_in_theaters.Model.Movie;
import com.gevcorst.popular_movies_in_theaters.Utilities.MenuOptions;
import com.gevcorst.popular_movies_in_theaters.databinding.ActivityMainBinding;
import com.gevcorst.popular_movies_in_theaters.viewModel.MainMovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ImageAdapter.ListItemClickListener {

    private RecyclerView mImageList;
    private List<com.gevcorst.popular_movies_in_theaters.Model.Movie> movies;
    private ArrayList<Movie> topRateMovies;
    private List<Movie> favoritesMovies;
    private List<Movie> mFavoriteMovies;
    private final String LOG_MASSAGE = this.getClass().getSimpleName();
    private MainMovieViewModel movieViewModel;
    private boolean isAfavoriteList, getTopRatedMovies;
    private final String BOOLEAN_INSTANCE_STATE_KEY = "isAFovriteKey";
    private AppDatabase db;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(getApplicationContext(),
                        R.color.colorWhite)));
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorPrimary,
                        getTheme())));
        db = AppDatabase.Companion.getInstance(this);
        mImageList = binding.rvNumbers;
        movieViewModel = new ViewModelProvider(this).get(MainMovieViewModel.class);
        observeLastVisitedList();
        Log.i("CREATE", "We entered here !");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Start", "We entered here !");

    }

    @Override
    public void onResume() {
        super.onResume();
        observeMoviesList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Stop", "We got here !!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Destory", "We got here !!");
    }

    /**
     * This method creates menu items in the app toolbar
     *
     * @param menu The URL to fetch the HTTP response from.
     * @return boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortmovie, menu);
        return true;
    }

    /**
     * Adds action to selected menu item
     *
     * @param item
     * @return true
     */

    @SuppressWarnings("JavaDoc")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sortByRating) {
            movieViewModel.fetchTopRatedMovies();
            movieViewModel.updateLastVisitedList(MenuOptions.TOP_RATING);
        } else if (id == R.id.sortByPopularity) {
            movieViewModel.fetchPopularMovies();
            movieViewModel.updateLastVisitedList(MenuOptions.POPULAR);
        } else if (id == R.id.sortByFavorite) {
            movieViewModel.fetchUserFavorites(db);
            movieViewModel.updateLastVisitedList(MenuOptions.FAVORITE);
        } else if (id == R.id.sortByPlayingNow) {
            movieViewModel.fetchPlayingNow();
            movieViewModel.updateLastVisitedList(MenuOptions.NowPLAYING);
        } else if (id == R.id.sortByUpComing) {
            movieViewModel.fetchUpComingMovies();
            movieViewModel.updateLastVisitedList(MenuOptions.UPCOMING);

        } else {

        }
        return true;
    }

    /*
     * The method sets up the RecyclerView Adapter class
     * @param movies.
     */
    private void setupAdapter(List<Movie> movies) {
        ImageAdapter mAdapter = new ImageAdapter(movies.size(), this,
                movies, getApplicationContext());

        mImageList.setAdapter(mAdapter);
    }

    /**
     * The method sets onclickListener for
     * each imageView in the RecyclerView
     *
     * @param clickedItemIndex
     */
    @SuppressWarnings("JavaDoc")
    @Override
    public void onListItemClick(int clickedItemIndex) {
        com.gevcorst.popular_movies_in_theaters.Model.Movie movie = movies.get(clickedItemIndex);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MOVIE_KEY", movie);
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private Boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnected());
    }

    private void observeMoviesList() {
        movieViewModel.getMovieList().observe(this, latestMovies -> {
            movies = latestMovies;
            Log.i("SetUp", latestMovies.toString());
            setupAdapter(movies);
        });

    }

    private void observeLastVisitedList() {
        movieViewModel.getLastViewedList().observe(this, option -> {
            switch (option) {
                case FAVORITE -> {
                    movieViewModel.fetchUserFavorites(db);
                }
                case TOP_RATING -> {
                    movieViewModel.fetchTopRatedMovies();
                }
                case POPULAR -> {
                    movieViewModel.fetchPopularMovies();
                }
                case UPCOMING -> {
                    movieViewModel.fetchUpComingMovies();
                }
                default -> {
                    movieViewModel.fetchPlayingNow();
                }
            }
        });
    }

}
