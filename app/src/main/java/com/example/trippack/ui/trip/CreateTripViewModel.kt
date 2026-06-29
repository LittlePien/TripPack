package com.example.trippack.ui.trip

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippack.domain.model.Destination
import com.example.trippack.domain.model.Trip
import com.example.trippack.domain.repository.DestinationRepository
import com.example.trippack.domain.repository.TripRepository
import com.example.trippack.domain.usecase.GeneratePackingListUseCase
import com.example.trippack.domain.usecase.GetWeatherUseCase
import com.example.trippack.domain.usecase.SaveTripUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateTripUiState(
    val destinations: List<Destination> = emptyList(),
    val selectedDestination: Destination? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val travelerCount: Int = 1,
    val estimatedBudget: String = "",
    val errorMessage: String? = null,
    val isSaving: Boolean = false,
    val createdTripId: Int? = null
)

@HiltViewModel
class CreateTripViewModel @Inject constructor(
    private val destinationRepository: DestinationRepository,
    private val saveTripUseCase: SaveTripUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val generatePackingListUseCase: GeneratePackingListUseCase,
    private val tripRepository: TripRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val editingTripId: Int? = savedStateHandle.get<String>("tripId")?.toIntOrNull()
    private val _uiState = MutableStateFlow(CreateTripUiState())
    val uiState: StateFlow<CreateTripUiState> = _uiState

    init {
        viewModelScope.launch {
            destinationRepository.getAllDestinations().collect { list ->
                _uiState.value = _uiState.value.copy(destinations = list)
            }
        }

        editingTripId?.let { id ->
            viewModelScope.launch {
                val existingTrip = tripRepository.getTripById(id)
                existingTrip?.let { trip ->
                    val destination = destinationRepository.getDestinationsById(trip.destinationId)
                    _uiState.value = _uiState.value.copy(
                        selectedDestination = destination,
                        startDate = trip.startDate,
                        endDate = trip.endDate
                    )
                }
            }
        }
    }

    fun onDestinationSelected(destination: Destination) {
        _uiState.value = _uiState.value.copy(selectedDestination = destination, errorMessage = null)
    }

    fun onStartDateChange(date: Long) {
        _uiState.value = _uiState.value.copy(startDate = date)
    }

    fun onEndDateChange(date: Long) {
        _uiState.value = _uiState.value.copy(endDate = date)
    }

    fun onTravelerCountChange(count: Int) {
        _uiState.value = _uiState.value.copy(travelerCount = count)
    }

    fun onBudgetChange(value: String) {
        _uiState.value = _uiState.value.copy(estimatedBudget = value)
    }

    fun formatDate(millis: Long?): String {
        if (millis == null) return "Pilih tanggal"
        val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.forLanguageTag("id-ID"))
        return sdf.format(java.util.Date(millis))
    }

    fun createTrip() {
        val state = _uiState.value
        val destination = state.selectedDestination

        if (destination == null) {
            _uiState.value = state.copy(errorMessage = "Pilih destinasi terlebih dahulu")
            return
        }
        if (state.startDate == null || state.endDate == null) {
            _uiState.value = state.copy(errorMessage = "Pilih tanggal berangkat dan pulang terlebih dahulu")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null)

            if (editingTripId != null) {
                val existingTrip = tripRepository.getTripById(editingTripId)
                existingTrip?.let {
                    val updated = it.copy(
                        destinationId = destination.id,
                        destinationName = destination.name,
                        startDate = state.startDate,
                        endDate = state.endDate
                    )
                    tripRepository.updateTrip(updated)
                }
                _uiState.value = _uiState.value.copy(isSaving = false, createdTripId = editingTripId)
            } else {
                val trip = Trip(
                    destinationId = destination.id,
                    destinationName = destination.name,
                    tripType = "Liburan",
                    startDate = state.startDate,
                    endDate = state.endDate
                )

                val tripId = saveTripUseCase(trip)
                val weatherResult = getWeatherUseCase(destination.name)
                weatherResult.onSuccess { weather ->
                    generatePackingListUseCase(tripId, weather)
                }
                _uiState.value = _uiState.value.copy(isSaving = false, createdTripId = tripId)
            }
        }
    }
}