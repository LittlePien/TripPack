package com.example.trippack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packing_items")
data class PackingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tripId: Int,
    val name: String,
    val isChecked: Boolean = false,
    val isAutoSuggested: Boolean = false,
    val rating: String = "NONE" // USED, NOT_NEEDED, SHOULD_HAVE_BROUGHT, NONE
)