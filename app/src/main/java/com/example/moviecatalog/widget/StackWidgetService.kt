package com.example.moviecatalog.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.moviecatalog.MovieCatalogRepository
import com.example.moviecatalog.database.AppDatabase
import com.example.moviecatalog.database.Favorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory{
        return StackRemoteViewsFactory(this.applicationContext)
    }
}