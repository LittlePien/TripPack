package com.example.trippack.data.remote.dto

data class GeocodingResponseDto(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String?
)