package com.example.trippack.domain.model

data class PackingRule(
    val itemName: String,
    val condition: (Weather) -> Boolean
)