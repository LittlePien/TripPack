package com.example.trippack.data.mapper

import com.example.trippack.data.local.entity.DestinationEntity
import com.example.trippack.domain.model.Destination

fun DestinationEntity.toDomain(): Destination {
    return Destination(
        id = id,
        name = name,
        notes = notes,
        estimatedBudget = estimatedBudget,
        isWishlist = isWishlist,
        createdAt = createdAt
    )
}

fun Destination.toEntity(): DestinationEntity {
    return DestinationEntity(
        id = id,
        name = name,
        notes = notes,
        estimatedBudget = estimatedBudget,
        isWishlist = isWishlist,
        createdAt = createdAt
    )
}