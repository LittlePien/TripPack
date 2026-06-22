package com.example.trippack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val destinationId: Int,
    val destinationName: String,
    val tripType: String,
    val startDate: Long,
    val endDate: Long,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)