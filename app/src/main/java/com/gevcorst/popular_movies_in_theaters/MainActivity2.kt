package com.gevcorst.popular_movies_in_theaters

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.gevcorst.popular_movies_in_theaters.Database.UserFavoriteDataBase
import com.gevcorst.popular_movies_in_theaters.databinding.ActivityMainBinding
import com.gevcorst.popular_movies_in_theaters.viewModel.MainMovieViewModel

class MainActivity2 : AppCompatActivity(){
    private var isAfavoriteList = false
    private  var getTopRatedMovies = false
    private val BOOLEAN_INSTANCE_STATE_KEY = "isAFovriteKey"
    lateinit var binding: ActivityMainBinding
    val mainViewModel: MainMovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userFavoriteDataBase = UserFavoriteDataBase.getInstance(
            applicationContext
        )


    }
    private fun isNetworkAvailable(): Boolean {
        val cm = (application.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager)
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
    override fun onResume() {
        super.onResume()
        mainViewModel.popularMovieList.observe(this, Observer {
           // binding.rvNumbers.adapter = ImageAdapter(it)
        })
    }

}