package com.example.trippack

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TripPackApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "trip_reminder_channel",
            "Trip Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifikasi pengingat H-1 sebelum perjalanan"
        }
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }
}