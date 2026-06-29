package com.example.trippack.domain.usecase

import com.example.trippack.domain.model.Weather
import com.example.trippack.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): Result<Weather> {
        return repository.getWeatherForcity(cityName)
    }

    suspend fun refresh(cityName: String): Result<Weather> {
        return repository.refreshWeatherForCity(cityName)
    }
}