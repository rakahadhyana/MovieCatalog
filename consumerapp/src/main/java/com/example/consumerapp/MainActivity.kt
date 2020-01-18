package com.example.consumerapp

import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.model.Movie
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object{
        private const val AUTHORITY = "com.example.moviecatalog"
        private const val SCHEME = "content"

        val CONTENT_URI: Uri =
            Uri.Builder().scheme(SCHEME).authority(AUTHORITY)
                .appendPath("favorite_table")
                .build()
    }


    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MovieAdapter()
        consumerRecyclerView.adapter = adapter
        consumerRecyclerView.layoutManager = LinearLayoutManager(this)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        loadNotesAsync()
    }

    private fun loadNotesAsync(){
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null) as Cursor
                val favoriteList = mutableListOf<Movie>()

                while (cursor.moveToNext()){
                    val title = cursor.getString(cursor.getColumnIndexOrThrow("movie_name"))
                    val movieImage = cursor.getString(cursor.getColumnIndexOrThrow("movie_image"))
                    val movieOverview = cursor.getString(cursor.getColumnIndexOrThrow("movie_description"))
                    favoriteList.add(Movie(title, movieImage, movieOverview))
                }
                favoriteList
            }
            val favorites = deferredFavorites.await()
            Log.i("favorites consumer", "${favorites.size}")
            if (favorites.size > 0){
                adapter.setData(favorites)
            } else {
                adapter.setData(emptyList())
            }
        }
    }
}
