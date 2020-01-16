package com.example.moviecatalog.ui.favorite

import android.app.Application
import androidx.lifecycle.*
import com.example.moviecatalog.database.AppDatabase
import com.example.moviecatalog.MovieCatalogRepository
import com.example.moviecatalog.database.Favorite

class FavoriteViewModel(type: Int, application: Application) : AndroidViewModel(application) {

    private val repository: MovieCatalogRepository
    val favorite: LiveData<List<Favorite>>

    init {
        val database = AppDatabase.getDatabase(application)
        val favoriteDao = database.favoriteDao()
        repository = MovieCatalogRepository(favoriteDao)
        favorite = when(type){
            0 -> repository.favoriteMovies
            1 -> repository.favoriteTvShow
            else -> repository.allFavorites
        }
    }

    class Factory constructor(private val type: Int, private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return FavoriteViewModel(type, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
