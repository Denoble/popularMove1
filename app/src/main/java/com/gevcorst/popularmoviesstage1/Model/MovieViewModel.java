package com.gevcorst.popularmoviesstage1.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;


import com.gevcorst.popularmoviesstage1.Database.UserFavoriteDataBase;
import com.gevcorst.popularmoviesstage1.Database.UsersFavorite;
import com.gevcorst.popularmoviesstage1.Utilities.JsonUtil;
import com.gevcorst.popularmoviesstage1.Utilities.Network;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private static MutableLiveData<List<Movie>> movieList;
    private final LiveData<List<UsersFavorite>> favoriteList;
    private static MutableLiveData<Movie> movie_WithTrailer,movie_WithReview;
    private static final String TAG = MovieViewModel.class.getSimpleName();

    public MovieViewModel(@NonNull Application application) {
        super(application);
        UserFavoriteDataBase database = UserFavoriteDataBase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        favoriteList = database.favoriteDao().loadAllTasks();

    }

    public MutableLiveData<Movie> getMovieTrailers(Movie tempMovie) {
        if (tempMovie.getYoutubeURL() == null) {
            movie_WithTrailer = new MutableLiveData<>();
            loadTrailer(tempMovie);
        }
        return movie_WithTrailer;
    }

    private static void loadTrailer(Movie tempMovie) {
        new  AsyncTask<Void, Void, Movie>() {
            Movie movieWithTrailer = tempMovie;

            @Override
            protected Movie doInBackground(Void... voids) {
                String movieId = String.valueOf(movieWithTrailer.getMovieId());
                URL url = Network.buildMovieUrl(movieId);
                try {
                    String movieTrailerString = Network.getResponseFromHttpUrl(url);
                    Log.d("MY VIDEO URL", movieTrailerString);
                    movieWithTrailer = JsonUtil.parseMovieTrailer(movieTrailerString, movieWithTrailer);


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return movieWithTrailer;

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Movie movie) {
                super.onPostExecute(movie);
               movie_WithTrailer.setValue(movie);
            }
        }.execute();
    }

    public MutableLiveData<Movie> getMovieReviews( Movie tempMovie) {
        if (tempMovie.getReviews() == null) {
            movie_WithReview = new MutableLiveData<>();
            loadReviews(tempMovie);
        }
        return movie_WithReview;
    }

    private static  void loadReviews(Movie tempMovie) {
        new AsyncTask<Void, Void, Movie>() {
            Movie movieWithReviews = tempMovie;

            @Override
            protected Movie doInBackground(Void... voids) {
                String movieId = String.valueOf(movieWithReviews.getMovieId());
                URL url = Network.buildReviewsUrl(movieId);
                Log.d("DE REVIEW URL", url.toString());
                try {
                    String movieReviewString = Network.getResponseFromHttpUrl(url);

                    movieWithReviews = JsonUtil.parseMovieReviews(movieReviewString, movieWithReviews);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return movieWithReviews;

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Movie movie) {
                super.onPostExecute(movie);
               movie_WithReview.setValue(movie);
            }
        }.execute();
    }

    public MutableLiveData<List<Movie>> getMovieList() {
        if (movieList == null) {
            movieList = new MutableLiveData<>();
            loadData();

        }
        return movieList;
    }

    private static  void loadData() {
        new AsyncTask<Void, Void, List<Movie>>() {


            @Override
            protected List<Movie> doInBackground(Void... voids) {
                List<Movie> movies = new ArrayList<>();
                URL url = Network.buildUrl();
                try {
                    String popularMovieResults = Network.getResponseFromHttpUrl(url);
                    movies = JsonUtil.parsePopularMovieJson(popularMovieResults);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return movies;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                super.onPostExecute(movies);
                movieList.setValue(movies);
            }

        }.execute();
    }

    public LiveData<List<UsersFavorite>> getFavoriteList() {
        return favoriteList;
    }

}
