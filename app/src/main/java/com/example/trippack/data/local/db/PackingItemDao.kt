package com.example.trippack.data.local.db

import androidx.room.*
import com.example.trippack.data.local.entity.PackingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackingItemDao {
    @Query("SELECT * FROM packing_items WHERE tripId = :tripId")
    fun getPackingItemsByTripId(tripId: Int): Flow<List<PackingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackingItem(item: PackingItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackingItems(items: List<PackingItemEntity>)

    @Update
    suspend fun updatePackingItem(item: PackingItemEntity)

    @Delete
    suspend fun deletePackingItem(item: PackingItemEntity)

    @Query("DELETE FROM packing_items WHERE tripId = :tripId")
    suspend fun deleteAllItemsForTrip(tripId: Int)
}