package com.example.moviecatalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalog.ui.movie.MovieViewModel
import com.example.moviecatalog.ui.tvshow.TvShowViewModel


class CustomViewModelFactory constructor(private val language: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieViewModel::class.java)){
            return MovieViewModel(language) as T
        } else if (modelClass.isAssignableFrom(TvShowViewModel::class.java)){
            return TvShowViewModel(language) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}