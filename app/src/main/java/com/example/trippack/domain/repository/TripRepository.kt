package com.example.trippack.domain.repository

import com.example.trippack.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getAllTrips(): Flow<List<Trip>>
    fun getUpcomingTrip(): Flow<Trip?>
    fun getCompletedTrips(): Flow<List<Trip>>
    suspend fun getTripById(id: Int): Trip?
    suspend fun saveTrip(trip: Trip): Long
    suspend fun updateTrip(trip: Trip)
    suspend fun deleteTrip(trip: Trip)
}