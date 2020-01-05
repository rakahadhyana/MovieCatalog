package com.example.moviecatalog.ui.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalog.BuildConfig
import com.example.moviecatalog.model.Movie
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MovieViewModel(private val lang: String) : ViewModel() {
    private val listMovie = MutableLiveData<ArrayList<Movie>>()
    private val isLoading = MutableLiveData<Boolean>()

    init {
        setMovie()
    }

    private fun setMovie(){
        val client = AsyncHttpClient()
        val listItems = ArrayList<Movie>()
        val language = lang
        val API_KEY = BuildConfig.TMDB_API_KEY
        val url = "https://api.themoviedb.org/3/movie/popular?api_key=$API_KEY&language=$language"
        isLoading.postValue(true)
        Log.i("MOVIE_VIEW_MODEL", "CALLING API")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                val responseObject = JSONObject(result)
                val list = responseObject.getJSONArray("results")

                for(i in 0 until list.length()){
                    val movie = list.getJSONObject(i)
                    val name = movie.getString("title")
                    val image = movie.getString("poster_path")
                    val overview = movie.getString("overview")
                    val movieItem = Movie(name, image, overview)
                    listItems.add(movieItem)
                }
                listMovie.postValue(listItems)
                isLoading.postValue(false)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
                isLoading.postValue(false)
            }
        })
    }

    internal fun getMovies(): LiveData<ArrayList<Movie>> {
        return listMovie
    }

    internal fun isLoading(): LiveData<Boolean>{
        return isLoading
    }
}