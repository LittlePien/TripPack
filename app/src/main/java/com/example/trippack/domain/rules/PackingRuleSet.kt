package com.example.trippack.domain.rules

import com.example.trippack.domain.model.PackingRule

object PackingRuleSet {
    val rules = listOf(
        PackingRule("Jaket") {it.temperature < 20},
        PackingRule("Sweater") {it.temperature < 15},
        PackingRule("Payung") {it.condition.contains("Rain", ignoreCase = true)},
        PackingRule("Jas hujan") {it.condition.contains("Rain", ignoreCase = true) && it.windSpeed > 20},
        PackingRule("Sunscreen SPF 50") {it.temperature > 30},
        PackingRule("Topi") {it.temperature > 30},
        PackingRule("Kacamata hitam") {it.temperature > 30},
        PackingRule("Baju ganti ekstra") {it.humidity > 80},
        PackingRule("Sepatu waterproof") {it.condition.contains("Rain", ignoreCase = true) || it.humidity > 85},
        PackingRule("Boots") {it.temperature < 10},
        PackingRule("Sarung tangan") {it.temperature < 5},
        PackingRule("Charger HP") {true},
        PackingRule("Obat-obatan pribadi") {true}
    )
}