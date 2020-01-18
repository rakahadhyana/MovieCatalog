package com.example.moviecatalog.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.moviecatalog.MovieCatalogRepository
import com.example.moviecatalog.R
import com.example.moviecatalog.database.AppDatabase
import com.example.moviecatalog.database.Favorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class StackRemoteViewsFactory(
    private val mContext: Context
) : RemoteViewsService.RemoteViewsFactory {
    private val favoriteDao = AppDatabase.getDatabase(mContext.applicationContext).favoriteDao()
    private val repository = MovieCatalogRepository(favoriteDao)
    private var favoriteMovies: List<Favorite> = emptyList()
    private var isLoaded = false
    override fun onCreate() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {
        isLoaded = false
        CoroutineScope(Dispatchers.Default).launch{
            favoriteMovies = repository.getListFavoriteMovie()
            Log.i("onDataSetChange", "$favoriteMovies")
            isLoaded = true
        }
        while (!isLoaded){
            Log.i("onDataSetChange", "still loading database")
        }
    }

    override fun hasStableIds(): Boolean = true

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        val extras = bundleOf(
            ImagesBannerWidget.EXTRA_ITEM to position
        )
        val movie = favoriteMovies[position]
        val bitmap = Glide.with(mContext).asBitmap()
            .load("https://image.tmdb.org/t/p/original${movie.movieImage}").submit().get()
        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getCount(): Int{
        Log.i("getCount", "${favoriteMovies.size}")
        return favoriteMovies.size
    }

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}
}