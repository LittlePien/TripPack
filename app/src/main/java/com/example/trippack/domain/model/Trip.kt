package com.example.trippack.domain.model

data class Trip(
    val id: Int = 0,
    val destinationId: Int,
    val destinationName: String,
    val tripType: String,
    val startDate: Long,
    val endDate: Long,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)