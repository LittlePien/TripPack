package com.example.trippack.data.repository

import com.example.trippack.data.local.db.DestinationDao
import com.example.trippack.data.mapper.toDomain
import com.example.trippack.data.mapper.toEntity
import com.example.trippack.domain.model.Destination
import com.example.trippack.domain.repository.DestinationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DestinationRepositoryImpl @Inject constructor(
    private val dao: DestinationDao
) : DestinationRepository {
    override fun getAllDestinations(): Flow<List<Destination>> {
        return dao.getAllDestinations().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getDestinationsById(id: Int): Destination? {
        return dao.getDestinationById(id)?.toDomain()
    }

    override suspend fun saveDestination(destination: Destination) {
        dao.insertDestination(destination.toEntity())
    }

    override suspend fun deleteDestination(destination: Destination) {
        dao.deleteDestination(destination.toEntity())
    }
}