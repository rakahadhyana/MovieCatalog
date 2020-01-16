package com.example.moviecatalog.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_table")
    fun getAll(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite_table WHERE type='movie'")
    fun getFavoriteMovies(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite_table WHERE type='movie'")
    fun getListFavoriteMovies() : List<Favorite>

    @Query("SELECT * FROM favorite_table WHERE type='tvshow'")
    fun getFavoriteTVShows(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite_table WHERE movie_name LIKE :name")
    suspend fun getFavoriteByName(name: String): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)
}