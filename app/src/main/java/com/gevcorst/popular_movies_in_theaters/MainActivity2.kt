package com.gevcorst.popular_movies_in_theaters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.gevcorst.popular_movies_in_theaters.databinding.ActivityMainBinding
import com.gevcorst.popular_movies_in_theaters.viewModel.MovieViewModel

class MainActivity2 : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    val mainViewModel:MovieViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.fetchPlayingNow()
    }
}