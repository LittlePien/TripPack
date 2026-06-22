package com.example.trippack.data.local.db

import androidx.room.*
import com.example.trippack.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY createdAt DESC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE isCompleted = 0 ORDER BY startDate ASC LIMIT 1")
    fun getUpcomingTrip(): Flow<TripEntity?>

    @Query("SELECT * FROM trips WHERE isCompleted = 1 ORDER BY createdAt DESC")
    fun getCompletedTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTripById(id: Int): TripEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity): Long

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Delete
    suspend fun deleteTrip(trip: TripEntity)
}