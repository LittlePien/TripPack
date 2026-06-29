package com.example.trippack.domain.repository

import com.example.trippack.domain.model.PackingItem
import kotlinx.coroutines.flow.Flow

interface PackingItemRepository {
    fun getPackingItemsByTripId(tripId: Int): Flow<List<PackingItem>>
    suspend fun saveAll(items: List<PackingItem>)
    suspend fun saveItem(item: PackingItem)
    suspend fun updateItem(item: PackingItem)
    suspend fun deleteItem(item: PackingItem)
    suspend fun deleteAllForTrip(tripId: Int)
    suspend fun deleteAutoSuggestedItemsForTrip(tripId: Int)
}