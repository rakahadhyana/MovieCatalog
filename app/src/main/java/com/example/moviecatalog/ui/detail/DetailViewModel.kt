package com.example.moviecatalog.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.example.moviecatalog.MovieCatalogRepository
import com.example.moviecatalog.SingleLiveEvent
import com.example.moviecatalog.database.AppDatabase
import com.example.moviecatalog.database.Favorite
import com.example.moviecatalog.model.Movie
import kotlinx.coroutines.launch

class DetailViewModel(private val movie: Movie, application: Application): AndroidViewModel(application) {

    private val repository: MovieCatalogRepository
    private val isFavorite = MutableLiveData<Boolean>()
    private val favoriteClicked = SingleLiveEvent<Boolean>()

    init {
        val favoriteDao = AppDatabase.getDatabase(application).favoriteDao()
        repository = MovieCatalogRepository(favoriteDao)
        viewModelScope.launch {
            val favoriteByName = repository.getFavoriteByName(movie.name)
            if(favoriteByName.isEmpty()){
                isFavorite.postValue(false)
            }else{
                isFavorite.postValue(true)
            }
        }
    }

    fun insert(favorite: Favorite){
        viewModelScope.launch {
            repository.insert(favorite)
        }
        isFavorite.postValue(true)
    }

    fun delete(favorite: Favorite){
        viewModelScope.launch {
            repository.delete(favorite)
        }
        isFavorite.postValue(false)
    }

    fun getMovie(): Movie {
        return movie
    }

    fun getIsFavorite(): LiveData<Boolean>{
        return isFavorite
    }

    fun getFavoriteClicked(): SingleLiveEvent<Boolean>{
        return favoriteClicked
    }

    fun onFavoriteClicked() {
        favoriteClicked.postValue(isFavorite.value?.not())
        isFavorite.postValue(isFavorite.value?.not())
    }

    class Factory constructor(private val movie: Movie, private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(movie, application = application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}