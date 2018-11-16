package com.gevcorst.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import com.gevcorst.popularmoviesstage1.Model.Movie;
import com.gevcorst.popularmoviesstage1.Utilities.JsonUtil;
import com.gevcorst.popularmoviesstage1.Utilities.Network;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ListItemClickListener {

    private ProgressBar mProgressBar;
    private RecyclerView mImageList;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorDarkGray)));
        mImageList = findViewById(R.id.rv_numbers);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 3);
        mImageList.setLayoutManager(mGridLayoutManager);
        mImageList.setHasFixedSize(true);
            if(savedInstanceState == null){
              makePopularMovieQuery();
            }
    }
        /*
        * The method instantiate PopularMovieQueryTask class.
        */
        private void makePopularMovieQuery(){
        if(isNetworkAvailable()){
            new PopularMovieQueryTask().execute();
        }
        else{
            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            "No INTERNET connection!",Toast.LENGTH_LONG);
            toast.show();
        }


    }

    /*
     * The method sets up the RecyclerView Adapter class
     * @param movies.
     */
    private void setupAdapter(List<Movie> movies){
        ImageAdapter mAdapter = new ImageAdapter(movies.size(), this, movies, getApplicationContext());

        mImageList.setAdapter(mAdapter);
    }
    /**
     * The method sets onclickListener for
      * each imageView in the RecyclerView
     * @param  clickedItemIndex
     */
    @SuppressWarnings("JavaDoc")
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Movie movie = movies.get(clickedItemIndex);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MOVIE_KEY",movie);
        Intent intent =  new Intent(getApplicationContext(),DetailActivity.class);
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
                (ConnectivityManager)getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnected());
    }
    class  PopularMovieQueryTask extends AsyncTask<Void, Integer, String>{
       /* @Override
        protected void onPreExecute() {
           mImageList.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }*/


        @Override
        protected String doInBackground(Void ... params) {

            String popularMovieResults = null;
                try {
                    URL url = Network.buildUrl();
                    popularMovieResults = Network.getResponseFromHttpUrl(url);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            Log.i("Popular Movie API:", popularMovieResults);
            System.out.println("FROM PRINT: " + popularMovieResults);
            return popularMovieResults;

        }
        @Override
        protected void onPostExecute(String  popularMovieJson) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mImageList.setVisibility(View.VISIBLE);
            if (popularMovieJson != null && !popularMovieJson.equals("")) {

                  movies = JsonUtil.parsePopularMovieJson(popularMovieJson);
                setupAdapter(movies);

                Log.e("LIST SIZE IS ", String.valueOf (movies.size()));




            }
            else {

                Log.e("AsyncTask ERROR", "AsyncTask Could parse Movies");
            }
        }
       /* @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }*/
    }
    /**
     * This method creates menu items in the app toolbar
     *@param menu The URL to fetch the HTTP response from.
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
     * @param item
     * @return true
     */

    @SuppressWarnings("JavaDoc")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID sortByRating was selected
            case R.id.sortByRating:
                Collections.sort(movies,Movie.movieRatingComparator);
                setupAdapter(movies);
                break;
            // action with ID action_settings was selected
            case R.id.sortByPopularity:
                Collections.sort(movies,Movie.moviePopularityComparator);
                setupAdapter(movies);

                break;
            default:
                break;
        }

        return true;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("MOVIE_KEY", movies);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
            if(isNetworkAvailable()){
                movies = savedInstanceState.getParcelableArrayList("MOVIE_KEY");
                setupAdapter(movies);
            }
            else{
                Toast toast =
                        Toast.makeText(getApplicationContext(),
                                "No INTERNET connection!",Toast.LENGTH_LONG);
                toast.show();
            }
    }

}
