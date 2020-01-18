package com.example.moviecatalog.ui.reminderSetting

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.moviecatalog.MovieCatalogRepository
import com.example.moviecatalog.R
import com.example.moviecatalog.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var repository: MovieCatalogRepository

    companion object {
        const val TYPE_DAILY = "daily"
        const val TYPE_RELEASE = "release"
        const val EXTRA_TYPE = "type"

        const val TIME_FORMAT = "HH:mm"

        private const val ID_DAILY_REMINDER = 100
        private const val ID_DAILY_RELEASE = 101

        const val CHANNEL_ID = "Channel_1"
        const val CHANNEL_NAME = "AlarmManager channel"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val favoriteDao = AppDatabase.getDatabase(context).favoriteDao()
        repository = MovieCatalogRepository(favoriteDao)
        val type = intent.getStringExtra(EXTRA_TYPE)
        val notificationId = if (type == TYPE_DAILY) ID_DAILY_REMINDER else ID_DAILY_RELEASE

        if (type == TYPE_RELEASE) {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayDate = dateFormat.format(calendar.time)
            Log.i("date", todayDate)
            CoroutineScope(Dispatchers.Default).launch {
                val releasedMovies = repository.getDailyRelease(todayDate, todayDate)
                releasedMovies.mapIndexed { index, movie ->
                    val title = movie.name
                    val message = "${movie.name} has been released today"
                    showAlarmNotification(context, title, message, notificationId + index)
                }
            }
        } else {
            val title = "Catalog Movie"
            val message = "Catalog Movie missing you"
            showAlarmNotification(context, title, message, notificationId)
        }
        Log.i("onReceive", "Alarm received")
    }

    fun setRepeatingAlarm(context: Context, type: String, time: String) {
        if (isDateInvalid(
                time,
                TIME_FORMAT
            )
        ) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, type)
        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        val notificationId = if (type == TYPE_DAILY) ID_DAILY_REMINDER else ID_DAILY_RELEASE
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId, intent, 0
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode =
            if (type.equals(TYPE_DAILY, ignoreCase = true)) ID_DAILY_REMINDER else ID_DAILY_RELEASE
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int
    ) {
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_movie_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notificationId, notification)
    }
}
