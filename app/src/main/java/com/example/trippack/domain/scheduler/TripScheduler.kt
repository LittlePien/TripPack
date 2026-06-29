package com.example.trippack.domain.scheduler

interface TripScheduler {
    fun scheduleTripReminder(destinationName: String, startDateMillis: Long)
}