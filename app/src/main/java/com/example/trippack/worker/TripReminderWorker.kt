package com.example.trippack.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.trippack.R

class TripReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val destinationName = inputData.getString(KEY_DESTINATION_NAME)
            ?: return Result.failure()

        val notification = NotificationCompat.Builder(context, "trip_reminder_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Trip besok!")
            .setContentText("Perjalananmu ke $destinationName berangkat besok. Cek packing list-mu!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        context.getSystemService(NotificationManager::class.java)
            .notify(destinationName.hashCode(), notification)
        return Result.success()
    }

    companion object {
        const val KEY_DESTINATION_NAME = "destination_name"
    }
}