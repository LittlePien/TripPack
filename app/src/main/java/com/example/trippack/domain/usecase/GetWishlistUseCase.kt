package com.example.trippack.domain.usecase

import com.example.trippack.domain.model.Destination
import com.example.trippack.domain.repository.DestinationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val repository: DestinationRepository
) {
    operator fun invoke(): Flow<List<Destination>> = repository.getAllDestinations()
}