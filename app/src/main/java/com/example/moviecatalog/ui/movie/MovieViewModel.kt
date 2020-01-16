package com.example.moviecatalog.ui.movie

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.moviecatalog.MovieCatalogRepository
import com.example.moviecatalog.database.AppDatabase
import com.example.moviecatalog.model.Movie
import kotlinx.coroutines.launch

class MovieViewModel(private val lang: String, application: Application) :
    AndroidViewModel(application) {

    private val repository: MovieCatalogRepository
    private val listMovie = MutableLiveData<List<Movie>>()
    private val isLoading = MutableLiveData<Boolean>()

    init {
        val favoriteDao = AppDatabase.getDatabase(application).favoriteDao()
        repository = MovieCatalogRepository(favoriteDao)
        setMovies()
    }

    private fun setMovies() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val movies = repository.getPopularMovies(lang)
            try {
                Log.i("Response", "Success: ${movies.size} Movies retrieved!")
                listMovie.postValue(movies)
            } catch (e: Exception) {
                Log.i("Response", "Failure: ${e.message}")
            }
            isLoading.postValue(false)
        }
    }

    fun restoreMovie() {
        listMovie.postValue(emptyList())
        setMovies()
    }

    fun searchMovie(query: String) {
        listMovie.postValue(emptyList())
        viewModelScope.launch {
            isLoading.postValue(true)
            val movies = repository.searchMovie(query)
            try {
                Log.i("Response", "Success: ${movies.size} Movies retrieved!")
                listMovie.postValue(movies)
            }catch (e: Exception) {
                Log.i("Response", "Failure: ${e.message}")
            }
            isLoading.postValue(false)
        }
    }

    internal fun getMovies(): LiveData<List<Movie>> {
        return listMovie
    }

    internal fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    class Factory(private val language: String, private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(language, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}