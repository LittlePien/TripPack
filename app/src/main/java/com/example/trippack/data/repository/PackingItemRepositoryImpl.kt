package com.example.trippack.data.repository

import com.example.trippack.data.local.db.PackingItemDao
import com.example.trippack.data.mapper.toDomain
import com.example.trippack.data.mapper.toEntity
import com.example.trippack.domain.model.PackingItem
import com.example.trippack.domain.repository.PackingItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PackingItemRepositoryImpl @Inject constructor(
    private val dao: PackingItemDao
) : PackingItemRepository {
    override fun getPackingItemsByyTripId(tripId: Int): Flow<List<PackingItem>> {
        return dao.getPackingItemsByTripId(tripId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun saveAll(items: List<PackingItem>) {
        dao.insertPackingItems(items.map { it.toEntity() })
    }

    override suspend fun saveItem(item: PackingItem) {
        dao.insertPackingItem(item.toEntity())
    }

    override suspend fun updateItem(item: PackingItem) {
        dao.updatePackingItem(item.toEntity())
    }

    override suspend fun deleteItem(item: PackingItem) {
        dao.deletePackingItem(item.toEntity())
    }

    override suspend fun deleteAllForTrip(tripId: Int) {
        dao.deleteAllItemsForTrip(tripId)
    }
}