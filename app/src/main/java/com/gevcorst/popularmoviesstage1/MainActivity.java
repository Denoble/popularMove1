package com.gevcorst.popularmoviesstage1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gevcorst.popularmoviesstage1.Database.UserFavoriteDataBase;
import com.gevcorst.popularmoviesstage1.Database.UsersFavorite;
import com.gevcorst.popularmoviesstage1.Model.Movie;
import com.gevcorst.popularmoviesstage1.Model.MovieViewModel;
import com.gevcorst.popularmoviesstage1.Utilities.JsonUtil;
import com.gevcorst.popularmoviesstage1.Utilities.Network;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ListItemClickListener {

    private RecyclerView mImageList;
    private ArrayList<Movie> movies;
    private List<Movie> favoritesMovies;
    private List<UsersFavorite> mFavoriteMovies;
    private final String LOG_MASSAGE = this.getClass().getSimpleName();
    private MovieViewModel movieViewModel;
    private boolean isAfavoriteList;
    private final String ISAFAVORITELISTSTATEKEY = "isAFovriteKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorDarkGray)));
        mImageList = findViewById(R.id.rv_numbers);
        ProgressBar mProgressBar = findViewById(R.id.pb_loading_indicator);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        mImageList.setLayoutManager(mGridLayoutManager);
        mImageList.setHasFixedSize(true);
        UserFavoriteDataBase userFavoriteDataBase = UserFavoriteDataBase.getInstance(getApplicationContext());
        movieViewModel =
                ViewModelProviders.of(this).get(MovieViewModel.class);
        seUpViewModel();
        if(savedInstanceState != null){
            isAfavoriteList = savedInstanceState.getBoolean(ISAFAVORITELISTSTATEKEY);
            if(isAfavoriteList){
                setUpFavoriteMovieViewModel();
            }
        }

    }


    /*
     * The method sets up the RecyclerView Adapter class
     * @param movies.
     */
    private void setupAdapter(List<Movie> movies) {
        ImageAdapter mAdapter = new ImageAdapter(movies.size(), this, movies, getApplicationContext());

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
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * The PopularMovieQueryTask calls the Network class
     * for extracting data from the movie API and JsonUtil class
     * for parse Json data to the Movie class object
     */
    private Boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnected());
    }

    private void seUpViewModel() {
        if (movies == null && !isNetworkAvailable()) {
            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            "No INTERNET connection!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            movieViewModel.getMovieList().observe(this, latestMovies -> {
                movies = (ArrayList<Movie>) latestMovies;
                setupAdapter(movies);
            });

        }

    }

    private void setUpFavoriteMovieViewModel() {
        movieViewModel.getFavoriteList().observe(this, usersFavorites -> {
            favoritesMovies = new ArrayList<>();
            for (int i = 0; i < usersFavorites.size(); i++) {
                String movieTitle = usersFavorites.get(i).getMoveTitle();
                movies.stream()
                        .filter(m -> m.getOriginalTitle().equals(movieTitle)).findAny().ifPresent(favMovie -> favoritesMovies.add(favMovie));

            }
            movies = (ArrayList<Movie>) favoritesMovies;
            setupAdapter(movies);


        });
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
        switch (item.getItemId()) {
            // action with ID sortByRating was selected
            case R.id.sortByRating:
                movies.sort(Movie.movieRatingComparator);
                setupAdapter(movies);
                isAfavoriteList = false;
                break;
            // action with ID sortByPopularity was selected
            case R.id.sortByPopularity:
                movies.sort(Movie.moviePopularityComparator);
                setupAdapter(movies);
                isAfavoriteList = false;
                break;
            // action with ID sortByPopularity was selected
            case R.id.sortByFavorite:
                setUpFavoriteMovieViewModel();
                isAfavoriteList = true;
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(ISAFAVORITELISTSTATEKEY,isAfavoriteList);
    }

}
