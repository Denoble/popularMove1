package com.gevcorst.popularmoviesstage1.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Network {
    private final static String  BASED_URL_API = "http://api.themoviedb.org/3/movie/popular/";
    private final static String MOVIE_STRING = "https://api.themoviedb.org/3/movie/{movie_id}?api_key=1dd6561c4403821b76cbf88ec92d820f&append_to_response=videos";
    private final static String REVIEWANDVIDEO_BASED_URL = "https://api.themoviedb.org/3/movie/";
    private final static String APIKEY = "1dd6561c4403821b76cbf88ec92d820f";
    private final static String APPEND_TO_RESPONSE_TRAILERS_PARAM = "videos";
    private final static String APPEND_TO_RESPONSE_REVIEWS_PARAM ="reviews";


    /**
     * Builds the URL used to query Popular Movie API.
     */
    public static URL buildUrl(){
        Uri builtUri = Uri.parse(BASED_URL_API).buildUpon()
                .appendQueryParameter("api_key",APIKEY)
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
                .appendQueryParameter("api_key",APIKEY)
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
                .appendQueryParameter("api_key",APIKEY)

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
   /* public static byte[] getUrlBytes(String urlSpec) throws
            IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection =
                (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new
                    ByteArrayOutputStream();
            InputStream in =
                    connection.getInputStream();
            if (connection.getResponseCode() !=
                    HttpURLConnection.HTTP_OK) {
                throw new
                        IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public static String getUrlString(String urlSpec) throws
            IOException {
        return new String(getUrlBytes(urlSpec));
    }*/
}
