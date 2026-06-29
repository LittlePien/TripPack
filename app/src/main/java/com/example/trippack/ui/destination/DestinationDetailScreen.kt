package com.example.trippack.ui.destination

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trippack.ui.components.DestinationDetailHeader
import com.example.trippack.ui.components.DestinationDetailLoadingState
import com.example.trippack.ui.components.DestinationNotFound
import com.example.trippack.ui.components.DestinationNotesCard
import com.example.trippack.ui.components.WeatherCard

@Composable
fun DestinationDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: DestinationDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when {
                uiState.isLoadingDestination -> {
                    DestinationDetailLoadingState()
                }

                uiState.destination == null -> {
                    DestinationNotFound()
                }

                else -> {
                    val destination = uiState.destination ?: return@Scaffold

                    DestinationDetailHeader(
                        name = destination.name,
                        onNavigateBack = onNavigateBack
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (destination.notes.isNotBlank()) {
                            DestinationNotesCard(notes = destination.notes)
                        }

                        WeatherCard(
                            weather = uiState.weather,
                            isLoading = uiState.isLoadingWeather,
                            error = uiState.weatherError,
                            onRefresh = viewModel::refreshWeather
                        )
                    }
                }
            }
        }
    }
}