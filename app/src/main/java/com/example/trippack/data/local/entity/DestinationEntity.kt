package com.example.trippack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "destinations")
data class DestinationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val notes: String = "",
    val estimatedBudget: Double = 0.0,
    val isWishlist: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)