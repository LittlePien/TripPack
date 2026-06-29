package com.example.trippack.ui.travellog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trippack.ui.components.ExpandableTripCard

@Composable
fun TravelLogScreen(
    onNavigateToPackingList: (Int) -> Unit,
    onNavigateToEditTrip: (Int) -> Unit,
    viewModel: TravelLogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Travel Log")

        if (uiState.allTrips.isEmpty()) {
            Text("Belum ada trip")
        } else {
            LazyColumn {
                items(uiState.allTrips, key = {it.id}) { trip ->
                    ExpandableTripCard(
                        trip = trip,
                        isExpanded = uiState.expandedTripId == trip.id,
                        onToggleExpand = {viewModel.toggleExpanded(trip.id)},
                        onCompleteTrip = {viewModel.completeTrip(trip)},
                        onDeleteTrip = {viewModel.deleteTrip(trip)},
                        onEditTrip = {onNavigateToEditTrip(trip.id)},
                        onViewPackingList = {onNavigateToPackingList(trip.id)}
                    )
                }
            }
        }
    }
}