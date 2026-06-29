package com.example.trippack.ui.travellog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippack.domain.model.Trip
import com.example.trippack.domain.repository.TripRepository
import com.example.trippack.domain.usecase.CompleteTripUseCase
import com.example.trippack.domain.usecase.GetTravelLogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.exp

data class TravelLogUiState(
    val allTrips: List<Trip> = emptyList(),
    val expandedTripId: Int? = null
)

@HiltViewModel
class TravelLogViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val getTravelLogUseCase: GetTravelLogUseCase,
    private val completeTripUseCase: CompleteTripUseCase
) : ViewModel() {
    private val _expandedTripId = MutableStateFlow<Int?>(null)
    val uiState: StateFlow<TravelLogUiState> = combine(
        tripRepository.getAllTrips(),
        _expandedTripId
    ) { trips, expandedId ->
        TravelLogUiState(allTrips = trips, expandedTripId = expandedId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TravelLogUiState()
    )

    fun toggleExpanded(tripId: Int) {
        _expandedTripId.value = if (_expandedTripId.value == tripId) null else tripId
    }

    fun completeTrip(trip: Trip) {
        viewModelScope.launch {
            completeTripUseCase(trip)
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            tripRepository.deleteTrip(trip)
        }
    }
}