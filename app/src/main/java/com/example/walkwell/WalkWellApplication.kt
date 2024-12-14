package com.example.walkwell

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.walkwell.utilities.ComponentTags
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WalkWellApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ComponentTags.TRACKING_NOTIFICATION_CHANNEL_ID,
                ComponentTags.TRACKING_NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}