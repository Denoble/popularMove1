package com.gevcorst.popular_movies_in_theaters.Utilities;

import com.gevcorst.popular_movies_in_theaters.Model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JsonUtil {
    private final static String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    public static ArrayList<Movie> parsePopularMovieJson(String json) {
        ArrayList<Movie> mMovies = new ArrayList<>();
        try {
            JSONObject jsonBody = new JSONObject(json);
            JSONArray results = jsonBody.getJSONArray("results");
            int arrayLength = results.length();
            for (int i = 0; i < arrayLength; i++) {
                Movie movie = new Movie();
                JSONObject jsonObject = results.getJSONObject(i);
                movie.setMovieId(jsonObject.getInt("id"));
                String thumbnail = jsonObject.getString("poster_path");
                movie.setImageThumbnail(thumbnail);
                String title = jsonObject.getString("original_title");
                movie.setOriginalTitle(title);
                Double userRating = jsonObject.getDouble("vote_average");
                movie.setUserRating(userRating);
                String synopsis = jsonObject.getString("overview");
                movie.setSynopsis(synopsis);
                String releaseDate = jsonObject.getString("release_date");
                movie.setRelease_date(new JsonUtil().convertStringToDate(releaseDate));
                movie.setReleaseDate(releaseDate);
                movie.setPopularity(jsonObject.getDouble("popularity"));
                mMovies.add(movie);
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return mMovies;
    }

    public static Movie parseMovieTrailer(String json, Movie movie) {
            boolean gotTrailerOneKey = false;
            boolean gotTrailerTwoKey  = false;
        try {
            JSONObject jsonBody = new JSONObject(json);
            JSONArray movieTrailers = jsonBody.getJSONArray("results");
            int arrayLength = movieTrailers.length();
            String youtubeURLKey;
            for (int i = 0; i < arrayLength; i++) {
                JSONObject jsonObject = movieTrailers.getJSONObject(i);
                String trailer = jsonObject.getString("key");
                    if (!gotTrailerOneKey) {
                        youtubeURLKey = trailer;
                        movie.setYoutubeURL(YOUTUBE_URL + youtubeURLKey);
                        gotTrailerOneKey = true;
                        continue;
                    }
                    if (!gotTrailerTwoKey) {
                        youtubeURLKey = trailer;
                        movie.setYoutubeURL2(YOUTUBE_URL + youtubeURLKey);
                        gotTrailerTwoKey = true;
                    }

            }

        }
        catch (Exception ee) {
            ee.printStackTrace();
        }
        return movie;
    }

    public static Movie parseMovieReviews(String json, Movie movie) {


        try {
            List<String> reviews = new ArrayList<>();
            JSONObject jsonBody = new JSONObject(json);
            JSONArray results = jsonBody.getJSONArray("results");
            int arrayLength = results.length();
            for (int i = 0; i < arrayLength; i++) {
                StringBuilder stringBuilder =  new StringBuilder();
                JSONObject jsonObject = results.getJSONObject(i);
                String author = jsonObject.getString("author").toUpperCase();
                String content = jsonObject.getString("content");
                stringBuilder.append("Author: ").append(author).append("\n");
                stringBuilder.append("Review:\n");
                stringBuilder.append(content).append("\n");
                String review = stringBuilder.toString();
                reviews.add(review);

                }


            movie.setReviews(reviews);
        }
        catch (Exception ee) {
           ee.printStackTrace();
        }
        return movie;
    }


    private Date convertStringToDate(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

}
