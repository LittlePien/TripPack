package com.example.trippack.domain.model

data class PackingItem(
    val id: Int = 0,
    val tripId: Int,
    val name: String,
    val isChecked: Boolean = false,
    val isAutoSuggested: Boolean = false
)