package com.example.trippack.domain.model

data class Destination(
    val id: Int = 0,
    val name: String,
    val notes: String = "",
    val estimatedBudget: Double = 0.0,
    val isWishlist: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)