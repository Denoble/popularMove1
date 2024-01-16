package com.gevcorst.popular_movies_in_theaters.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gevcorst.popular_movies_in_theaters.repository.services.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(
    val repository: MovieRepository
    = MovieRepository()
) : ViewModel() {
    init {
        fetchPopularMovies()
        //fetchPlayingNow()
        //fetchTopRatedMovies()
    }

    fun fetchPlayingNow() {
        viewModelScope.launch {
            try {
                repository.getMoviesPlayingNow().collect {
                    Log.i("MovieViewModel", it.theMovieDbResults.toString())
                }
            } catch (e: Exception) {
                Log.i("MovieViewModel Error", e.stackTraceToString())
            }

        }

    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                repository.getPopularMovies().collect {
                    Log.i("MovieViewModel", it.theMovieDbResults.toString())
                   fetchVideos( it.theMovieDbResults[0].id)
                }
            } catch (e: Exception) {
                Log.i("MovieViewModel Error", e.stackTraceToString())
            }

        }
    }
    fun fetchTopRatedMovies(){
        viewModelScope.launch {
            try {
                repository.getTopRatedMovies().collect{
                    Log.i("MovieViewModel", it.theMovieDbResults.toString())
                }
            }catch (e: Exception) {
                Log.i("MovieViewModel Error", e.stackTraceToString())
            }
        }
    }
    fun fetchReviews(id:Int){
        viewModelScope.launch {
            try {
                repository.getReviews(id).collect{
                    Log.i("MovieReviews", it.toString())
                }

            }catch (e:Exception){
                Log.i("MovieViewModel Error", e.stackTraceToString())
            }
        }
    }
    fun fetchVideos(id: Int){
        viewModelScope.launch {
            try {
                repository.getVideos (id).collect{
                    Log.i("MovieVideos", it.toString())
                }

            }catch (e:Exception){
                Log.i("MovieViewModel Error", e.stackTraceToString())
            }
        }
    }
}