package com.example.trippack.ui.destination

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippack.domain.model.Destination
import com.example.trippack.domain.model.Weather
import com.example.trippack.domain.repository.DestinationRepository
import com.example.trippack.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DestinationDetailUiState(
    val destination: Destination? = null,
    val weather: Weather? = null,
    val isLoadingDestination: Boolean = true,
    val isLoadingWeather: Boolean = false,
    val weatherError: String? = null
)

@HiltViewModel
class DestinationDetailViewModel @Inject constructor(
    private val destinationRepository: DestinationRepository,
    private val getWeatherUseCase: GetWeatherUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val destinationId: Int = savedStateHandle.get<String>("destinationId")?.toIntOrNull() ?: 0
    private val _uiState = MutableStateFlow(DestinationDetailUiState())
    val uiState: StateFlow<DestinationDetailUiState> = _uiState
    init {
        loadDestination()
    }

    private fun loadDestination() {
        viewModelScope.launch {
            val destination = destinationRepository.getDestinationsById(destinationId)
            _uiState.value = _uiState.value.copy(destination = destination, isLoadingDestination = false)
            destination?.let {loadWeather(it.name)}
        }
    }

    private fun loadWeather(cityName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingWeather = true, weatherError = null)
            val result = getWeatherUseCase(cityName)
            result.onSuccess { weather ->
                _uiState.value = _uiState.value.copy(weather = weather, isLoadingWeather = false)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(isLoadingWeather = false, weatherError = e.message ?: "Gagal memuat cuaca")
            }
        }
    }

    fun refreshWeather() {
        val cityName = _uiState.value.destination?.name ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingWeather = true, weatherError = null)
            val result = getWeatherUseCase.refresh(cityName)
            result.onSuccess { weather ->
                _uiState.value = _uiState.value.copy(weather = weather, isLoadingWeather = false)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoadingWeather = false,
                    weatherError = e.message ?: "Gagal memuat cuaca"
                )
            }
        }
    }
}