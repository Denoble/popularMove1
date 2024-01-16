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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gevcorst.popular_movies_in_theaters.Database.UserFavoriteDataBase;
import com.gevcorst.popular_movies_in_theaters.Database.UsersFavorite;
import com.gevcorst.popular_movies_in_theaters.Model.Movie;
import com.gevcorst.popular_movies_in_theaters.Model.TheMovie;
import com.gevcorst.popular_movies_in_theaters.databinding.ActivityMainBinding;
import com.gevcorst.popular_movies_in_theaters.viewModel.MainMovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ListItemClickListener {

    private RecyclerView mImageList;
    private List<TheMovie> movies;
    private ArrayList<Movie> topRateMovies;
    private List<Movie> favoritesMovies;
    private List<UsersFavorite> mFavoriteMovies;
    private final String LOG_MASSAGE = this.getClass().getSimpleName();
    private MainMovieViewModel movieViewModel;
    private boolean isAfavoriteList, getTopRatedMovies;
    private final String BOOLEAN_INSTANCE_STATE_KEY = "isAFovriteKey";

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(getApplicationContext(),
                        R.color.colorPrimary)));
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorPrimary,
                        getTheme())));
        mImageList = binding.rvNumbers;
        ProgressBar mProgressBar = binding.pbLoadingIndicator;
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        mImageList.setLayoutManager(mGridLayoutManager);
        mImageList.setHasFixedSize(true);
        UserFavoriteDataBase userFavoriteDataBase = UserFavoriteDataBase.getInstance(getApplicationContext());
        movieViewModel =
                ViewModelProviders.of(this).get(MainMovieViewModel.class);


        if (savedInstanceState != null) {
            boolean[] instanceStateBoolean = savedInstanceState.getBooleanArray(
                    BOOLEAN_INSTANCE_STATE_KEY);
            isAfavoriteList = instanceStateBoolean[0];
            getTopRatedMovies = instanceStateBoolean[1];
            if (isAfavoriteList && getTopRatedMovies) {
                setUpTopRatedViewModel();
               // setUpFavoriteMovieViewModel();
            }
            if (isAfavoriteList && !getTopRatedMovies) {
                observePopularMovies();
                //setUpFavoriteMovieViewModel();
            }
            if (getTopRatedMovies && !isAfavoriteList) {
                setUpTopRatedViewModel();
            }
            if(!isAfavoriteList && !getTopRatedMovies){
                observePopularMovies();
            }
        } else {
            observePopularMovies();
        }

    }

    /*
     * The method sets up the RecyclerView Adapter class
     * @param movies.
     */
    private void setupAdapter(List<TheMovie> movies) {
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
        TheMovie movie = movies.get(clickedItemIndex);
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

    private void observePopularMovies() {
            movieViewModel.getPopularMovieList().observe(this, latestMovies -> {
                movies = (ArrayList<TheMovie>) latestMovies;
                Log.i("SetUp",latestMovies.toString());
                setupAdapter(movies);
            });

    }

    private void setUpTopRatedViewModel() {
        if (movies == null && !isNetworkAvailable()) {
            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            "No INTERNET connection!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            movieViewModel.getTopRatedMovieList() .observe(this, latestMovies -> {
                movies =  latestMovies;
                setupAdapter(movies);
            });

        }

    }

   /* private void setUpFavoriteMovieViewModel() {
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
    }*/

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
            // action with ID sortByRating was selected
            getTopRatedMovies = true;
            isAfavoriteList = false;
            setUpTopRatedViewModel();
            //movies.sort(Movie.movieRatingComparator);
            setupAdapter(movies);
        } else if (id == R.id.sortByPopularity) {
            getTopRatedMovies = false;
            isAfavoriteList = false;
            observePopularMovies();
            // movies.sort(Movie.moviePopularityComparator);
            setupAdapter(movies);
        } else if (id == R.id.sortByFavorite) {
            isAfavoriteList = true;
           // setUpFavoriteMovieViewModel();
        } else {

        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean[] bools = new boolean[2];
        bools[0] = isAfavoriteList;
        bools[1] = getTopRatedMovies;
        outState.putBooleanArray(BOOLEAN_INSTANCE_STATE_KEY, bools);
    }

    @Override
    public void onResume() {
        super.onResume();
        observePopularMovies();

    }


}
