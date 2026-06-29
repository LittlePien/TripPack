package com.example.trippack.domain.usecase

import com.example.trippack.domain.model.Destination
import com.example.trippack.domain.repository.DestinationRepository
import javax.inject.Inject

class DeleteDestinationUseCase @Inject constructor(
    private val repository: DestinationRepository
) {
    suspend operator fun invoke(destination: Destination) {
        repository.deleteDestination(destination)
    }
}