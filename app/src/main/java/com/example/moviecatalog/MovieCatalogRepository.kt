package com.example.moviecatalog

import androidx.lifecycle.LiveData
import com.example.moviecatalog.database.Favorite
import com.example.moviecatalog.database.FavoriteDao
import com.example.moviecatalog.model.Movie
import com.example.moviecatalog.network.TMDBApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieCatalogRepository(private val favoriteDao: FavoriteDao) {

    val allFavorites: LiveData<List<Favorite>> = favoriteDao.getAll()

    val favoriteMovies: LiveData<List<Favorite>> = favoriteDao.getFavoriteMovies()

    val favoriteTvShow: LiveData<List<Favorite>> = favoriteDao.getFavoriteTVShows()

    suspend fun getListFavoriteMovie(): List<Favorite> {
        var favoriteMovie = emptyList<Favorite>()
        withContext(Dispatchers.IO){
            favoriteMovie = favoriteDao.getListFavoriteMovies()
        }
        return favoriteMovie
    }

    suspend fun getPopularMovies(language: String): List<Movie> {
        var movies = emptyList<Movie>()
        withContext(Dispatchers.IO) {
            val response = TMDBApi.tmdb.getMovies(language)
            movies = response.results.map {
                Movie(it.title, it.posterUrl ?: "", it.overview)
            }
        }
        return movies
    }

    suspend fun getPopularTvShows(language: String): List<Movie> {
        var movies = emptyList<Movie>()
        withContext(Dispatchers.IO) {
            val response = TMDBApi.tmdb.getTvShows(language)
            movies = response.results.map {
                Movie(it.name, it.posterUrl ?: "", it.overview)
            }
        }
        return movies
    }

    suspend fun searchMovie(query: String): List<Movie> {
        var movies = emptyList<Movie>()
        withContext(Dispatchers.IO) {
            val response = TMDBApi.tmdb.searchMovie(query)
            movies = response.results.map {
                Movie(it.title, it.posterUrl ?: "", it.overview)
            }
        }
        return movies
    }

    suspend fun searchTvShow(query: String): List<Movie> {
        var movies = emptyList<Movie>()
        withContext(Dispatchers.IO) {
            val response = TMDBApi.tmdb.searchTvShow(query)
            movies = response.results.map {
                Movie(it.name, it.posterUrl ?: "", it.overview)
            }
        }
        return movies
    }

    suspend fun getDailyRelease(startDate: String, endDate: String): List<Movie> {
        var movies = emptyList<Movie>()
        withContext(Dispatchers.IO) {
            val response = TMDBApi.tmdb.getDailyRelease(startDate, endDate)
            movies = response.results.map {
                Movie(it.title, it.posterUrl ?: "", it.overview)
            }
        }
        return movies
    }

    suspend fun getFavoriteByName(movieName: String): List<Favorite> {
        var favoriteByName: List<Favorite> = emptyList()
        withContext(Dispatchers.IO) {
            favoriteByName = favoriteDao.getFavoriteByName(movieName)
        }
        return favoriteByName
    }

    suspend fun insert(favorite: Favorite) {
        withContext(Dispatchers.IO) {
            favoriteDao.insert(favorite)
        }
    }

    suspend fun delete(favorite: Favorite) {
        withContext(Dispatchers.IO) {
            favoriteDao.deleteFavorite(favorite)
        }
    }
}