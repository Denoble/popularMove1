package com.gevcorst.popular_movies_in_theaters.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gevcorst.popular_movies_in_theaters.Database.AppDatabase
import com.gevcorst.popular_movies_in_theaters.Model.MovieReview
import com.gevcorst.popular_movies_in_theaters.Model.Movie
import com.gevcorst.popular_movies_in_theaters.Model.Videos
import com.gevcorst.popular_movies_in_theaters.Utilities.MenuOptions
import com.gevcorst.popular_movies_in_theaters.repository.services.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainMovieViewModel(
    val repository: MovieRepository
    = MovieRepository()
) : ViewModel() {
    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>> = _movieList
    private val _videos = MutableLiveData<Videos>()
    val videos: LiveData<Videos> = _videos
    private val _reviews = MutableLiveData<List<MovieReview>>()
    val reviews: LiveData<List<MovieReview>> = _reviews
    private val _lastViewList = MutableLiveData<MenuOptions>()
    val lastViewedList: LiveData<MenuOptions> = _lastViewList
    private val _favoriteState = MutableLiveData<Boolean>()
    val favoriteState: LiveData<Boolean> = _favoriteState

    init {
        fetchPlayingNow()
    }

    fun fetchPlayingNow() {
        viewModelScope.launch {
            try {
                repository.getMoviesPlayingNow().collect {
                    _movieList.value = it.movieDbResults
                    Log.i("MainMovieViewModel", it.movieDbResults.toString())
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
                    _movieList.value = it.movieDbResults
                    Log.i("MainMovieViewModel", it.movieDbResults.toString())
                }
            } catch (e: Exception) {
                Log.i("MainMovieViewModel Error", e.stackTraceToString())
            }

        }
    }

    fun fetchUpComingMovies() {
        viewModelScope.launch {
            try {
                repository.getUpComingMovies().collect {
                    _movieList.value = it
                }

            } catch (e: Exception) {

            }
        }
    }

    fun fetchUserFavorites(db: AppDatabase) {
        viewModelScope.launch {
            try {
                val tempList = flow {
                    val favorites = db.userFavObjectDao().loadAllTasks()
                    emit(favorites)
                }.flowOn(Dispatchers.IO)
                tempList.collect {
                    _movieList.value = it
                    Log.i("Favorites", it.toString())
                }
            } catch (e: Exception) {
                Log.i("Favorites", e.stackTraceToString())
            }
        }

    }

    fun fetchTopRatedMovies() {
        viewModelScope.launch {
            try {
                repository.getTopRatedMovies().collect {
                    _movieList.value = it.movieDbResults
                    Log.i("MainMovieViewModel", it.movieDbResults.toString())
                }
            } catch (e: Exception) {
                Log.i("MainMovieViewModel Error", e.stackTraceToString())
            }
        }
    }

    fun fetchReviews(id: Int) {
        viewModelScope.launch {
            try {
                repository.getReviews(id).collect {
                    _reviews.value = it.movieReviews
                    Log.i("MovieReviews", it.toString())
                }

            } catch (e: Exception) {
                Log.i("MainMovieViewModel Error", e.stackTraceToString())
            }
        }
    }

    fun fetchVideos(id: Int) {
        viewModelScope.launch {
            try {
                repository.getVideos(id).collect {
                    _videos.value = it
                    Log.i("MovieVideos", it.toString())
                }

            } catch (e: Exception) {
                Log.i("MainMovieViewModel Error", e.stackTraceToString())
            }
        }
    }

    fun checkIsFaveMovie(db: AppDatabase, id: Int) {
        viewModelScope.launch {
            try {
                val tempValue = flow {
                    val result = db.userFavObjectDao().loadFavoriteById(id)
                    if (result == null) {
                        emit(false)
                    } else {
                        emit(true)
                    }
                }.flowOn(Dispatchers.IO)
                tempValue.collect {
                    _favoriteState.value = it
                    Log.i("LoadingFave", it.toString())
                }

            } catch (e: Exception) {
                _favoriteState.value = false
                Log.i("LoadingFaveError", e.stackTraceToString())
            }
        }
    }

    fun removeFromFavorite(movie: Movie, db: AppDatabase) {
        viewModelScope.launch {
            try {
                val isFave = flow {
                    db.userFavObjectDao().deleteById(movie.movieId)
                    emit(false)
                }.flowOn(Dispatchers.IO)
                isFave.collect {
                    _favoriteState.value = it
                    Log.i("MovieDB", "Removed Successfully ${_favoriteState.value}")
                }
            } catch (e: Exception) {
                Log.i("MovieDB", e.stackTraceToString())
            }
        }
    }

    fun addToFavorite(movie: Movie, db: AppDatabase) {
        viewModelScope.launch {
            try {
                val isFave = flow {
                    db.userFavObjectDao().insertFavorite(movie)
                    emit(true)
                }.flowOn(Dispatchers.IO)
                isFave.collect {
                    _favoriteState.value = it
                }
            } catch (e: Exception) {
                Log.i("MovieDB", e.stackTraceToString())
            }
        }
    }

    fun updateLastVisitedList(type: MenuOptions) {
        _lastViewList.value = type
    }
}