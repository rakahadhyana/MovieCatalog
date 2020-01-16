package com.example.moviecatalog.network

import com.squareup.moshi.Json

data class NetworkMovieContainer(val results: List<NetworkMovie>)

data class NetworkMovie(
    val title: String,
    val overview: String,
    @Json(name = "poster_path") val posterUrl: String?
)

data class NetworkTvContainer(val results: List<NetworkTvShow>)

data class NetworkTvShow(
    val name: String,
    val overview: String,
    @Json(name = "poster_path") val posterUrl: String?
)
