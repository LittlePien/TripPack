package com.example.trippack.data.mapper

import com.example.trippack.data.local.entity.PackingItemEntity
import com.example.trippack.domain.model.PackingItem

fun PackingItemEntity.toDomain(): PackingItem {
    return PackingItem(
        id = id,
        tripId = tripId,
        name = name,
        isChecked = isChecked,
        isAutoSuggested = isAutoSuggested
    )
}

fun PackingItem.toEntity(): PackingItemEntity {
    return PackingItemEntity(
        id = id,
        tripId = tripId,
        name = name,
        isChecked = isChecked,
        isAutoSuggested = isAutoSuggested
    )
}