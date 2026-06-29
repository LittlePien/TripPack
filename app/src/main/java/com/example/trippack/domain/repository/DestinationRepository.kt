package com.example.trippack.domain.repository

import com.example.trippack.domain.model.Destination
import kotlinx.coroutines.flow.Flow

interface DestinationRepository {
    fun getAllDestinations(): Flow<List<Destination>>
    suspend fun getDestinationsById(id: Int): Destination?
    suspend fun saveDestination(destination: Destination)
    suspend fun deleteDestination(destination: Destination)
}