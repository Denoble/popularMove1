package com.gevcorst.popular_movies_in_theaters.repository.services

import android.content.Context
import com.gevcorst.popular_movies_in_theaters.Database.UsersFavorite
import com.gevcorst.popular_movies_in_theaters.Model.MovieData
import com.gevcorst.popular_movies_in_theaters.Model.MovieReview
import com.gevcorst.popular_movies_in_theaters.Model.Review
import com.gevcorst.popular_movies_in_theaters.Model.TheMovie
import com.gevcorst.popular_movies_in_theaters.Model.Videos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository {
    suspend fun getMoviesPlayingNow(): Flow<MovieData> = flow {
        val playingNow = MovieDbRESTService.movieDBObject.getNowPlaying().await()
        emit(playingNow)
    }
    suspend fun getPopularMovies():Flow<MovieData> = flow{
        val popularMovies = MovieDbRESTService.movieDBObject.getPopularMovie().await()
        emit(popularMovies)
    }
    suspend fun getTopRatedMovies():Flow<MovieData> = flow {
        val topRatedMovies = MovieDbRESTService.movieDBObject.getTopRated().await()
        emit(topRatedMovies)
    }
    suspend fun getUpComingMovies():Flow<List<TheMovie>> = flow{
        val upcomingMovies = MovieDbRESTService.movieDBObject.getUpcoming().await()
        emit(upcomingMovies.theMovieDbResults)
    }
    suspend fun getReviews(id:Int):Flow<Review> = flow {
        val reviews = MovieDbRESTService.movieDBObject.getReviews(id).await()
        emit(reviews)
    }
    suspend fun getVideos(id:Int):Flow<Videos> = flow {
        val videos = MovieDbRESTService.movieDBObject.getVideos(id).await()
        emit(videos)
    }
    suspend fun getFavorites(context: Context):Flow<List<UsersFavorite>> = flow{

    }
}