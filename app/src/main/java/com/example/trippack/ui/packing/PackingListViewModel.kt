package com.example.trippack.ui.packing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippack.domain.model.PackingItem
import com.example.trippack.domain.repository.PackingItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PackingListUiState(
    val items: List<PackingItem> = emptyList(),
    val newItemName: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class PackingListViewModel @Inject constructor(
    private val packingItemRepository: PackingItemRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tripId: Int = savedStateHandle.get<String>("tripId")?.toIntOrNull() ?: 0
    private val _newItemName = MutableStateFlow("")
    val uiState: StateFlow<PackingListUiState> = combine(
        packingItemRepository.getPackingItemsByyTripId(tripId),
        _newItemName
    ) { items, newItemName ->
        PackingListUiState(
            items = items,
            newItemName = newItemName,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PackingListUiState()
    )

    fun onNewItemNameChange(value: String) {
        _newItemName.value = value
    }

    fun addCustomItem() {
        val name = _newItemName.value
        if (name.isBlank()) return

        viewModelScope.launch {
            packingItemRepository.saveItem(
                PackingItem(tripId = tripId, name = name, isAutoSuggested = false)
            )
            _newItemName.value = ""
        }
    }

    fun toggleChecked(item: PackingItem) {
        viewModelScope.launch {
            packingItemRepository.updateItem(item.copy(isChecked = !item.isChecked))
        }
    }

    fun deleteItem(item: PackingItem) {
        viewModelScope.launch {
            packingItemRepository.deleteItem(item)
        }
    }
}