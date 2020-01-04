package com.example.moviecatalog.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FavoriteViewModelFactory constructor(private val type: Int, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return FavoriteViewModel(type, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}