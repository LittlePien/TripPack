package com.example.trippack.domain.usecase

import com.example.trippack.domain.model.PackingItem
import com.example.trippack.domain.model.Weather
import com.example.trippack.domain.repository.PackingItemRepository
import com.example.trippack.domain.rules.PackingRuleSet
import javax.inject.Inject

class GeneratePackingListUseCase @Inject constructor(
    private val packingItemRepository: PackingItemRepository
) {
    suspend operator fun invoke(tripId: Int, weather: Weather) {
        val suggestedItems = PackingRuleSet.rules.filter { it.condition(weather) }.map { rule ->
            PackingItem(
                tripId = tripId,
                name = rule.itemName,
                isChecked = false,
                isAutoSuggested = true
            )
        }
        packingItemRepository.saveAll(suggestedItems)
    }
}