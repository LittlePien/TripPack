package com.example.trippack.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trippack.data.local.entity.PackingItemEntity
import com.example.trippack.data.local.entity.WeatherCacheEntity
import com.example.trippack.data.local.entity.TripEntity
import com.example.trippack.data.local.entity.DestinationEntity

@Database(
    entities = [
        DestinationEntity::class,
        TripEntity::class,
        PackingItemEntity::class,
        WeatherCacheEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TripPackDatabase : RoomDatabase() {
    abstract fun destinationDao(): DestinationDao
    abstract fun tripDao(): TripDao
    abstract fun packingItemDao(): PackingItemDao
    abstract fun weatherCacheDao(): WeatherCacheDao
}