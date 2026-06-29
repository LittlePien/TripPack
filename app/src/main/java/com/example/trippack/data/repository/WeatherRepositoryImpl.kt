package com.example.trippack.data.repository

import com.example.trippack.BuildConfig
import com.example.trippack.data.local.db.WeatherCacheDao
import com.example.trippack.data.mapper.toDomain
import com.example.trippack.data.mapper.toEntity
import com.example.trippack.data.remote.api.GeocodingApiService
import com.example.trippack.data.remote.api.WeatherApiService
import com.example.trippack.domain.model.Weather
import com.example.trippack.domain.repository.WeatherRepository
import javax.inject.Inject

private const val CACHE_DURATION_MS = 60 * 60 * 1000L
class WeatherRepositoryImpl @Inject constructor(
    private val dao: WeatherCacheDao,
    private val weatherApi: WeatherApiService,
    private val geocodingApi: GeocodingApiService
) : WeatherRepository {
    override suspend fun getWeatherForcity(cityName: String): Result<Weather> {
        return try {
            val cached = dao.getWeatherByCity(cityName)
            val isFresh = cached != null && (System.currentTimeMillis() - cached.cachedAt) < CACHE_DURATION_MS
            if (cached != null && isFresh) {
                return Result.success(cached.toDomain())
            }

            val geocodeResult = geocodingApi.searchLocation(
                query = cityName,
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY
            )
            if (geocodeResult.isEmpty()) {
                return Result.failure(
                    Exception("Lokasi \"$cityName\" tidak ditemukan, coba nama tempat dalam Bahasa Inggris atau nama lokal yang lebih spesifik")
                )
            }

            val location = geocodeResult.first()
            val response = weatherApi.getCurrentWeather(
                lat = location.lat,
                lon = location.lon,
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY
            )
            val weather = Weather(
                cityName = cityName,
                temperature = response.main.temp,
                condition = response.weather.firstOrNull()?.main ?: "Unknown",
                humidity = response.main.humidity,
                windSpeed = response.wind.speed
            )

            dao.insertWeather(weather.toEntity())
            Result.success(weather)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshWeatherForCity(cityName: String): Result<Weather> {
        return try {
            val geocodeResult = geocodingApi.searchLocation(
                query = cityName,
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY
            )

            if (geocodeResult.isEmpty()) {
                return Result.failure(
                    Exception("Lokasi \"$cityName\" tidak ditemukan. Coba gunakan nama tempat dalam Bahasa Inggris atau nama lokal yang lebih spesifik")
                )
            }

            val location = geocodeResult.first()
            val response = weatherApi.getCurrentWeather(
                lat = location.lat,
                lon = location.lon,
                apiKey = BuildConfig.OPEN_WEATHER_API_KEY
            )

            val weather = Weather(
                cityName = cityName,
                temperature = response.main.temp,
                condition = response.weather.firstOrNull()?.main ?: "Unknown",
                humidity = response.main.humidity,
                windSpeed = response.wind.speed
            )

            dao.insertWeather(weather.toEntity())
            Result.success(weather)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}