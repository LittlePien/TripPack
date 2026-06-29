package com.example.trippack.data.scheduler

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.trippack.domain.scheduler.TripScheduler
import com.example.trippack.worker.TripReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TripSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TripScheduler {
    override fun scheduleTripReminder(destinationName: String, startDateMillis: Long) {
        val delayMs = startDateMillis - System.currentTimeMillis() - (24 * 60 * 60 * 1000L)
        if (delayMs <= 0) return

        val request = OneTimeWorkRequestBuilder<TripReminderWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(TripReminderWorker.KEY_DESTINATION_NAME to destinationName))
            .addTag("trip_reminder_${destinationName}")
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}