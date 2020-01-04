package com.example.moviecatalog.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalog.model.Movie

class DetailViewModelFactory constructor(private val movie: Movie, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(movie, application = application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}