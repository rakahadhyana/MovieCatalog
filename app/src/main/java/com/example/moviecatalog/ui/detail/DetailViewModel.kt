package com.example.moviecatalog.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviecatalog.database.AppDatabase
import com.example.moviecatalog.MovieCatalogRepository
import com.example.moviecatalog.database.Favorite
import com.example.moviecatalog.model.Movie
import kotlinx.coroutines.launch

class DetailViewModel(private val movie: Movie, application: Application): AndroidViewModel(application) {

    private val repository: MovieCatalogRepository
    val allFavorite: LiveData<List<Favorite>>
    var isFavorite: Boolean = false

    init {
        val favoriteDao = AppDatabase.getDatabase(application, viewModelScope).favoriteDao()
        repository = MovieCatalogRepository(favoriteDao)
        allFavorite = repository.allFavorites
    }

    fun insert(favorite: Favorite){
        viewModelScope.launch {
            repository.insert(favorite)
        }
        isFavorite = true
    }

    fun getMovie(): Movie {
        return movie
    }
}