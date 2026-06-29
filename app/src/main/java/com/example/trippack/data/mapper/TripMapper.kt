package com.example.trippack.data.mapper

import com.example.trippack.data.local.entity.TripEntity
import com.example.trippack.domain.model.Trip

fun TripEntity.toDomain(): Trip {
    return Trip(
        id = id,
        destinationId = destinationId,
        destinationName = destinationName,
        tripType = tripType,
        startDate = startDate,
        endDate = endDate,
        travelerCount = travelerCount,
        estimatedBudget = estimatedBudget,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}

fun Trip.toEntity(): TripEntity {
    return TripEntity(
        id = id,
        destinationId = destinationId,
        destinationName = destinationName,
        tripType = tripType,
        startDate = startDate,
        endDate = endDate,
        travelerCount = travelerCount,
        estimatedBudget = estimatedBudget,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}