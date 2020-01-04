package com.example.moviecatalog.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviecatalog.database.AppDatabase
import com.example.moviecatalog.MovieCatalogRepository
import com.example.moviecatalog.database.Favorite

class FavoriteViewModel(private val type: Int, application: Application) : AndroidViewModel(application) {

    private val repository: MovieCatalogRepository
    val favorite: LiveData<List<Favorite>>

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        val favoriteDao = database.favoriteDao()
        repository = MovieCatalogRepository(favoriteDao)
        favorite = when(type){
            0 -> repository.favoriteMovies
            1 -> repository.favoriteTvShow
            else -> repository.allFavorites
        }
    }

}
