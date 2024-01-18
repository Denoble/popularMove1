package com.gevcorst.popular_movies_in_theaters.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gevcorst.popular_movies_in_theaters.Database.UsersFavorite
import com.gevcorst.popular_movies_in_theaters.Model.MovieReview
import com.gevcorst.popular_movies_in_theaters.Model.Review
import com.gevcorst.popular_movies_in_theaters.Model.TheMovie
import com.gevcorst.popular_movies_in_theaters.Model.Videos
import com.gevcorst.popular_movies_in_theaters.repository.services.MovieRepository
import kotlinx.coroutines.launch

class MainMovieViewModel(
    val repository: MovieRepository
    = MovieRepository()
) : ViewModel() {
    private val _popularMovieList = MutableLiveData<List<TheMovie>>()
    val popularMovieList:LiveData<List<TheMovie>> = _popularMovieList
    private val _topRatedMovieList =  MutableLiveData<List<TheMovie>>()
    val topRatedMovieList:LiveData<List<TheMovie>> = _topRatedMovieList
    private val _favoriteMovieList = MutableLiveData<List<UsersFavorite>>()
    val favoriteList: LiveData<List<UsersFavorite>> = _favoriteMovieList
    private val _videos = MutableLiveData<Videos>()
    val videos:LiveData<Videos> = _videos
    private val _reviews = MutableLiveData<List<MovieReview>>()
    val reviews:LiveData<List<MovieReview>> = _reviews
    private val _playingNow = MutableLiveData<List<TheMovie>>()
    val playingNow:LiveData<List<TheMovie>> = _playingNow
    private val _upcomingMovies = MutableLiveData<List<TheMovie>>()
    val upcomingMovies:LiveData<List<TheMovie>> = _upcomingMovies
    init {
        fetchPopularMovies()
        fetchPlayingNow()
        fetchTopRatedMovies()
        fetchUpComingMovies()
    }

    fun fetchPlayingNow() {
        viewModelScope.launch {
            try {
                repository.getMoviesPlayingNow().collect {
                    _playingNow.value = it.theMovieDbResults
                    Log.i("MainMovieViewModel", it.theMovieDbResults.toString())
                }
            } catch (e: Exception) {
                Log.i("MainMovieViewModel Error", e.stackTraceToString())
            }

        }

    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            try {
                repository.getPopularMovies().collect {
                    _popularMovieList.value = it.theMovieDbResults
                    Log.i("MainMovieViewModel", it.theMovieDbResults.toString())
                   fetchVideos( it.theMovieDbResults[0].id)
                }
            } catch (e: Exception) {
                Log.i("MainMovieViewModel Error", e.stackTraceToString())
            }

        }
    }
    fun fetchUpComingMovies(){
        viewModelScope.launch {
            try {
                repository.getUpComingMovies().collect{
                    _upcomingMovies.value = it
                }

            }catch (e:Exception){

            }
        }
    }
    fun fetchTopRatedMovies(){
        viewModelScope.launch {
            try {
                repository.getTopRatedMovies().collect{
                    _topRatedMovieList.value = it.theMovieDbResults
                    Log.i("MainMovieViewModel", it.theMovieDbResults.toString())
                }
            }catch (e: Exception) {
                Log.i("MainMovieViewModel Error", e.stackTraceToString())
            }
        }
    }
    fun fetchReviews(id:Int){
        viewModelScope.launch {
            try {
                repository.getReviews(id).collect{
                    _reviews.value = it.movieReviews
                    Log.i("MovieReviews", it.toString())
                }

            }catch (e:Exception){
                Log.i("MainMovieViewModel Error", e.stackTraceToString())
            }
        }
    }
    fun fetchVideos(id: Int){
        viewModelScope.launch {
            try {
                repository.getVideos (id).collect{
                    _videos.value = it
                    Log.i("MovieVideos", it.toString())
                }

            }catch (e:Exception){
                Log.i("MainMovieViewModel Error", e.stackTraceToString())
            }
        }
    }
}