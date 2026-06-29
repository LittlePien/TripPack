package com.example.trippack.domain.usecase

import com.example.trippack.domain.model.Trip
import com.example.trippack.domain.repository.TripRepository
import javax.inject.Inject

class SaveTripUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(trip: Trip): Int {
        return repository.saveTrip(trip).toInt()
    }
}