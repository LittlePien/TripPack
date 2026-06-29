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
    val travelerCount: Int = 1,
    val estimatedBudget: Double = 0.0,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)