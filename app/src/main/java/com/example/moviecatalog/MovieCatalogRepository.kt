package com.example.moviecatalog

import androidx.lifecycle.LiveData
import com.example.moviecatalog.database.Favorite
import com.example.moviecatalog.database.FavoriteDao

class MovieCatalogRepository(private val favoriteDao: FavoriteDao) {

    val allFavorites: LiveData<List<Favorite>> = favoriteDao.getAll()

    val favoriteMovies: LiveData<List<Favorite>> = favoriteDao.getFavoriteMovies()

    val favoriteTvShow: LiveData<List<Favorite>> = favoriteDao.getFavoriteTVShows()

    suspend fun getFavoriteByName(movieName: String): List<Favorite>{
        return favoriteDao.getFavoriteByName(movieName)
    }

    suspend fun insert(favorite: Favorite){
        favoriteDao.insert(favorite)
    }

    suspend fun delete(favorite: Favorite){
        favoriteDao.deleteFavorite(favorite)
    }
}