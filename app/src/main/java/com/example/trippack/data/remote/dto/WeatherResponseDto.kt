package com.example.trippack.data.remote.dto

data class WeatherResponseDto(
    val main: MainDto,
    val weather: List<WeatherConditionDto>,
    val wind: WindDto
)

data class MainDto(
    val temp: Double,
    val humidity: Int
)

data class WeatherConditionDto(
    val main: String,
    val description: String,
)

data class WindDto(
    val speed: Double
)