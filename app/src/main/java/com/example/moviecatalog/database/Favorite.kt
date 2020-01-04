package com.example.moviecatalog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class Favorite(
    @PrimaryKey @ColumnInfo(name="movie_name") var movieName: String,
    @ColumnInfo(name="movie_image") var movieImage: String,
    @ColumnInfo(name="movie_description") var movieDescription: String,
    @ColumnInfo(name="type") var type: String
)