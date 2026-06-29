package com.example.trippack.data.local.db

import androidx.room.*
import com.example.trippack.data.local.entity.WeatherCacheEntity

@Dao
interface WeatherCacheDao {
    @Query("SELECT * FROM weather_cache WHERE LOWER(cityName) = LOWER(:cityName)")
    suspend fun getWeatherByCity(cityName: String): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherCacheEntity)

    @Query("DELETE FROM weather_cache WHERE cachedAt < :expiryTime")
    suspend fun deleteExpiredCache(expiryTime: Long)
}