package com.example.trippack.data.repository

import com.example.trippack.data.local.db.TripDao
import com.example.trippack.data.mapper.toDomain
import com.example.trippack.data.mapper.toEntity
import com.example.trippack.domain.model.Trip
import com.example.trippack.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val dao: TripDao
) : TripRepository {
    override fun getAllTrips(): Flow<List<Trip>> {
        return dao.getAllTrips().map { list -> list.map { it.toDomain() } }
    }

    override fun getUpcomingTrip(): Flow<Trip?> {
        return dao.getUpcomingTrip().map { it?.toDomain() }
    }

    override fun getCompletedTrips(): Flow<List<Trip>> {
        return dao.getCompletedTrips().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getTripById(id: Int): Trip? {
        return dao.getTripById(id)?.toDomain()
    }

    override suspend fun saveTrip(trip: Trip): Long {
        return dao.insertTrip(trip.toEntity())
    }

    override suspend fun updateTrip(trip: Trip) {
        dao.updateTrip(trip.toEntity())
    }

    override suspend fun deleteTrip(trip: Trip) {
        dao.deleteTrip(trip.toEntity())
    }
}