package com.gevcorst.popular_movies_in_theaters;


import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.gevcorst.popular_movies_in_theaters.Database.AppDatabase;
import com.gevcorst.popular_movies_in_theaters.Model.Movie;
import com.gevcorst.popular_movies_in_theaters.Utilities.CustomAlertDialog;
import com.gevcorst.popular_movies_in_theaters.Utilities.MenuOptions;
import com.gevcorst.popular_movies_in_theaters.databinding.ActivityMainBinding;
import com.gevcorst.popular_movies_in_theaters.viewModel.MainMovieViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ImageAdapter.ListItemClickListener {
    private RecyclerView mImageList;
    private List<com.gevcorst.popular_movies_in_theaters.Model.Movie> movies;
    static MainMovieViewModel movieViewModel;
    static AppDatabase db;
    private ActivityMainBinding binding;
    public boolean isFaveListEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = AppDatabase.getInstance(this);
        mImageList = binding.rvNumbers;
        movieViewModel = new ViewModelProvider(this).get(MainMovieViewModel.class);
        observeLastVisitedList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        observeMoviesList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            movieViewModel.updateLastVisitedList(MenuOptions.TOP_RATING);
        } else if (id == R.id.sortByPopularity) {
            movieViewModel.updateLastVisitedList(MenuOptions.POPULAR);
        } else if (id == R.id.sortByFavorite) {
            movieViewModel.updateLastVisitedList(MenuOptions.FAVORITE);
        } else if (id == R.id.sortByPlayingNow) {
            movieViewModel.updateLastVisitedList(MenuOptions.NowPLAYING);
        } else if (id == R.id.sortByUpComing) {
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
        Movie movie = movies.get(clickedItemIndex);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MOVIE_KEY", movie);
        Intent intent = new Intent(getApplicationContext(),
                DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void observeMoviesList() {
        movieViewModel.getMovieList().observe(this, latestMovies -> {
            if (latestMovies.size() > 0) {
                movies = latestMovies;
                binding.imageViewLoading.setVisibility(View.GONE);
                binding.rvNumbers.setVisibility(View.VISIBLE);
                setupAdapter(movies);
                Log.i("SetUp", latestMovies.toString());
            } else {
                binding.imageViewLoading.setVisibility(View.VISIBLE);
                binding.rvNumbers.setVisibility(View.GONE);
            }

        });
        movieViewModel.isFavoriteEmpty().observe(this, isListEmpty -> {
            if(isListEmpty){
               CustomAlertDialog.showAlertDialog(
                       this,"Empty Favorite List",
                       "Add Movie to Favourite List by Clicking" +
                               "the Heart Button on the Detail View",movieViewModel
               );
            }
        });
        movieViewModel.getScreenTitle().observe(this, screenTitle ->{
            getSupportActionBar().setTitle(screenTitle);
        });
    }

    /* private void onBackButtonPress() {
         this.getOnBackPressedDispatcher().addCallback(this,
                 new OnBackPressedCallback(true) {
                     @Override
                     public void handleOnBackPressed() {
                         setupAdapter(movies);
                         Log.i("BackPressed","Back Press !!!");
                     }
                 });
     }*/
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
                case NowPLAYING -> {
                    movieViewModel.fetchPlayingNow();
                }
                default -> {

                }
            }
        });
    }

}