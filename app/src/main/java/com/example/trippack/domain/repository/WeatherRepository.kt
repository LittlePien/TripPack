package com.example.trippack.domain.repository

import com.example.trippack.domain.model.Weather

interface WeatherRepository {
    suspend fun getWeatherForcity(cityName: String): Result<Weather>
    suspend fun refreshWeatherForCity(cityName: String): Result<Weather>
}