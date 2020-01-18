package com.example.moviecatalog.network

import com.example.moviecatalog.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_KEY = BuildConfig.TMDB_API_KEY

interface TMDBService {
    @GET("movie/popular?api_key=$API_KEY")
    suspend fun getMovies(@Query("language") language: String): NetworkMovieContainer

    @GET("tv/popular?api_key=$API_KEY")
    suspend fun getTvShows(@Query("language") language: String): NetworkTvContainer

    @GET("search/movie?api_key=$API_KEY")
    suspend fun searchMovie(@Query("query") query: String): NetworkMovieContainer

    @GET("search/tv?api_key=$API_KEY")
    suspend fun searchTvShow(@Query("query") query: String): NetworkTvContainer

    @GET("discover/movie?api_key=$API_KEY")
    suspend fun getDailyRelease(@Query("primary_release_date.gte") dateStart: String,
                                @Query("primary_release_date.lte") dateEnd: String) : NetworkMovieContainer
}

object TMDBApi {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    val tmdb: TMDBService by lazy {
        retrofit.create(TMDBService::class.java)
    }
}