package com.example.moviecatalog.widget

import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.widget.RemoteViews
import com.example.moviecatalog.R

class UpdateWidgetService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val manager = AppWidgetManager.getInstance(this)
        val view = RemoteViews(packageName, R.layout.image_banner_widget)
        val theWidget = ComponentName(this, ImagesBannerWidget::class.java)
        val appWidgetIds = manager.getAppWidgetIds(theWidget)
        manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)
        jobFinished(params, false)
        return true
    }

}