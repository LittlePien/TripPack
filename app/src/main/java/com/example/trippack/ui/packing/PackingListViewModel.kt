package com.example.trippack.ui.packing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.trippack.domain.model.PackingItem
import com.example.trippack.domain.model.Trip
import com.example.trippack.domain.repository.PackingItemRepository
import com.example.trippack.domain.repository.TripRepository
import com.example.trippack.ui.navigation.PackingList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


data class PackingListUiState(
    val trip: Trip? = null,
    val items: List<PackingItem> = emptyList(),
    val newItemName: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class PackingListViewModel @Inject constructor(
    private val packingItemRepository: PackingItemRepository,
    private val tripRepository: TripRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tripId: Int = savedStateHandle.toRoute<PackingList>().tripId
    private val _newItemName = MutableStateFlow("")
    private val _trip = MutableStateFlow<Trip?>(null)

    val uiState: StateFlow<PackingListUiState> = combine(
        packingItemRepository.getPackingItemsByTripId(tripId),
        _newItemName,
        _trip
    ) { items, newItemName, trip ->
        PackingListUiState(
            trip = trip,
            items = items,
            newItemName = newItemName,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PackingListUiState()
    )

    init {
        viewModelScope.launch {
            _trip.value = tripRepository.getTripById(tripId)
        }
    }

    fun onNewItemNameChange(value: String) {
        _newItemName.value = value
    }

    fun addCustomItem() {
        val name = _newItemName.value.trim()
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

    fun formatDate(millis: Long): String =
        SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID")).format(Date(millis))
}