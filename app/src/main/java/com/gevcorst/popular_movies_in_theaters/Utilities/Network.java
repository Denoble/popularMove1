package com.gevcorst.popular_movies_in_theaters.Utilities;

import android.net.Uri;
import com.gevcorst.popular_movies_in_theaters.BuildConfig;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Network {
    private final static String  BASED_URL_API = "http://api.themoviedb.org/3/movie/popular/";
    private final static String  TOP_RATED_URL_API = "http://api.themoviedb.org/3/movie/top_rated/";
    private final static String REVIEWANDVIDEO_BASED_URL = "https://api.themoviedb.org/3/movie/";
    public final static String THEMOVIEDB_APIKEY = BuildConfig.THEMOVIEDB_API_KEY;
    private final static String APPEND_TO_RESPONSE_TRAILERS_PARAM = "videos";
    private final static String APPEND_TO_RESPONSE_REVIEWS_PARAM ="reviews";


    /**
     * Builds the URL used to query Popular Movie API.
     */
    public static URL buildUrl(){
        Uri builtUri = Uri.parse(BASED_URL_API).buildUpon()
                .appendQueryParameter("api_key",THEMOVIEDB_APIKEY)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        return  url;
    }
    public static URL buildTopRatedUrl(){
        Uri builtUri = Uri.parse(TOP_RATED_URL_API).buildUpon()
                .appendQueryParameter("api_key",THEMOVIEDB_APIKEY)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        return  url;
    }

    /**
     * Query themoviedb.org RESTFUL API for Movie
     * Trailer and duration
     * @return URL
     */
    public static URL buildMovieUrl(String movieId){
        Uri builtUri = Uri.parse(REVIEWANDVIDEO_BASED_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(APPEND_TO_RESPONSE_TRAILERS_PARAM)
                .appendQueryParameter("api_key",THEMOVIEDB_APIKEY)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        return  url;
    }

    /**
     * Query themoviedb.org for a movie reviews
     * @return URL
     */
    public static URL buildReviewsUrl(String movieId){
        Uri builtUri = Uri.parse(REVIEWANDVIDEO_BASED_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(APPEND_TO_RESPONSE_REVIEWS_PARAM)
                .appendQueryParameter("api_key",THEMOVIEDB_APIKEY)

                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        return  url;
    }
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in,"UTF-8");
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
