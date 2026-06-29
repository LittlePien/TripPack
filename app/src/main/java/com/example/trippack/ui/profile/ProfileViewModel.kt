package com.example.trippack.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippack.domain.repository.AuthRepository
import com.example.trippack.domain.repository.DestinationRepository
import com.example.trippack.domain.repository.TripRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val tripCount: Int = 0,
    val completedCount: Int = 0,
    val destinationCount: Int = 0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tripRepository: TripRepository,
    private val destinationRepository: DestinationRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        tripRepository.getAllTrips(),
        tripRepository.getCompletedTrips(),
        destinationRepository.getAllDestinations()
    ) { trips, completed, destinations ->
        ProfileUiState(
            name = firebaseAuth.currentUser?.displayName ?: "Traveler",
            email = firebaseAuth.currentUser?.email ?: "",
            tripCount = trips.size,
            completedCount = completed.size,
            destinationCount = destinations.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )

    fun logout() {
        authRepository.logout()
    }
}