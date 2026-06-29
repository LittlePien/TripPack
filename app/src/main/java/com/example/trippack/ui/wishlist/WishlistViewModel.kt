package com.example.trippack.ui.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trippack.domain.model.Destination
import com.example.trippack.domain.usecase.GetWishlistUseCase
import com.example.trippack.domain.usecase.DeleteDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WishlistUiState(
    val destinations: List<Destination> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
)

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val deleteDestinationUseCase: DeleteDestinationUseCase
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<WishlistUiState> = combine(
        getWishlistUseCase(),
        _searchQuery
    ) { destinations, query ->
        val filtered = if (query.isBlank()) {
            destinations
        } else {
            destinations.filter { it.name.contains(query, ignoreCase = true) }
        }
        WishlistUiState(
            destinations = filtered,
            searchQuery = query,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = WishlistUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun deleteDestination(destination: Destination) {
        viewModelScope.launch {
            deleteDestinationUseCase(destination)
        }
    }
}