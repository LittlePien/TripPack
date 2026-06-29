package com.example.trippack.domain.usecase

import com.example.trippack.domain.model.Trip
import com.example.trippack.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTravelLogUseCase @Inject constructor(
    private val repository: TripRepository
) {
    operator fun invoke(): Flow<List<Trip>> = repository.getCompletedTrips()
}