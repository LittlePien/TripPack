package com.example.trippack.ui.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippack.domain.model.Destination
import com.example.trippack.domain.usecase.SaveDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddDestinationUiState(
    val name: String = "",
    val notes: String = "",
    val estimatedBudget: String = "",
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)

@HiltViewModel
class AddDestinationViewModel @Inject constructor(
    private val saveDestinationUseCase: SaveDestinationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddDestinationUiState())
    val uiState: StateFlow<AddDestinationUiState> = _uiState

    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(name = value, errorMessage = null)
    }

    fun onNotesChange(value: String) {
        _uiState.value = _uiState.value.copy(notes = value)
    }

    fun onBudgetChange(value: String) {
        _uiState.value = _uiState.value.copy(estimatedBudget = value)
    }

    fun saveDestination() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Nama destinasi tidak boleh kosong")
            return
        }
        val budget = if (state.estimatedBudget.isBlank()) {
            0.0
        } else {
            state.estimatedBudget.toDoubleOrNull()
        }
        if (budget == null) {
            _uiState.value = state.copy(errorMessage = "Budget harus berupa angka yang valid")
            return
        }
        viewModelScope.launch {
            val destination = Destination(
                name = state.name,
                notes = state.notes,
                estimatedBudget = state.estimatedBudget.toDoubleOrNull() ?: 0.0
            )
            saveDestinationUseCase(destination)
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }
}