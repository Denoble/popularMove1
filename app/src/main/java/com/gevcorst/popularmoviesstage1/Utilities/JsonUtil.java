package com.gevcorst.popularmoviesstage1.Utilities;

import android.util.Log;

import com.gevcorst.popularmoviesstage1.Model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JsonUtil {
    public static ArrayList<Movie> parsePopularMovieJson(String json) {
        ArrayList<Movie> mMovies =  new ArrayList<>();
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
                movie.setRelease_date( new JsonUtil().convertStringToDate(releaseDate) );
                movie.setReleaseDate(releaseDate);
                movie.setPopularity(jsonObject.getDouble("popularity"));
                mMovies.add(movie);
            }

        } catch (Exception ee) {
            Log.e("A", "Fail to parse JSON: " + ee.getMessage());
        }
        return mMovies;
    }

    private  Date convertStringToDate(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(str);
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

}
