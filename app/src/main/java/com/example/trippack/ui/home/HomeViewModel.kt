package com.example.trippack.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippack.domain.model.Destination
import com.example.trippack.domain.model.Trip
import com.example.trippack.domain.repository.DestinationRepository
import com.example.trippack.domain.repository.TripRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class HomeUiState(
    val userName: String = "",
    val upcomingTrip: Trip? = null,
    val daysUntilTrip: Long? = null,
    val wishlistCount: Int = 0,
    val tripCount: Int = 0,
    val completedCount: Int = 0,
    val previewDestinations: List<Destination> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val destinationRepository: DestinationRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = combine(
        tripRepository.getUpcomingTrip(),
        tripRepository.getAllTrips(),
        tripRepository.getCompletedTrips(),
        destinationRepository.getAllDestinations()
    ) { upcomingTrip, allTrips, completedTrips, destinations ->
        val days = upcomingTrip?.let {
            val diff = it.startDate - System.currentTimeMillis()
            (diff / (1000 * 60 * 60 * 24)).coerceAtLeast(0)
        }
        HomeUiState(
            userName = firebaseAuth.currentUser?.displayName ?: "Traveler",
            upcomingTrip = upcomingTrip,
            daysUntilTrip = days,
            wishlistCount = destinations.size,
            tripCount = allTrips.size,
            completedCount = completedTrips.size,
            previewDestinations = destinations
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )
}