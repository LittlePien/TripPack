package com.example.trippack.domain.usecase

import com.example.trippack.domain.model.Trip
import com.example.trippack.domain.repository.TripRepository
import javax.inject.Inject

class CompleteTripUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(trip: Trip){
        repository.updateTrip(trip.copy(isCompleted = true))
    }
}