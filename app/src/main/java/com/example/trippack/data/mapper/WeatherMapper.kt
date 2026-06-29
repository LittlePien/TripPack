package com.example.trippack.data.mapper

import com.example.trippack.data.local.entity.WeatherCacheEntity
import com.example.trippack.domain.model.Weather

fun WeatherCacheEntity.toDomain(): Weather {
    return Weather(
        cityName = cityName,
        temperature = temperature,
        condition = condition,
        humidity = humidity,
        windSpeed = windSpeed
    )
}

fun Weather.toEntity(): WeatherCacheEntity {
    return WeatherCacheEntity(
        cityName = cityName,
        temperature = temperature,
        condition = condition,
        humidity = humidity,
        windSpeed = windSpeed
    )
}