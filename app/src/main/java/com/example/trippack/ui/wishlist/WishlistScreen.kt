package com.example.trippack.ui.wishlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trippack.ui.components.DestinationButton
import com.example.trippack.ui.components.WishlistDestinationCard
import com.example.trippack.ui.components.WishlistEmptyState
import com.example.trippack.ui.components.WishlistHeader
import com.example.trippack.ui.components.WishlistLoadingState
import com.example.trippack.ui.components.WishlistNoResults

@Composable
fun WishlistScreen(
    onNavigateToAddDestination: () -> Unit,
    onNavigateToDestinationDetail: (Int) -> Unit,
    viewModel: WishlistViewModel = hiltViewModel()
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
            WishlistHeader(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange
            )
            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(top = 16.dp)
                    ) {
                        WishlistLoadingState()
                    }
                }
                uiState.destinations.isEmpty() && uiState.searchQuery.isBlank() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(top = 16.dp)
                    ) {
                        WishlistEmptyState(onAddClick = onNavigateToAddDestination)
                    }
                }
                uiState.destinations.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(top = 16.dp)
                    ){
                        WishlistNoResults(query = uiState.searchQuery)
                    }
                }

                else -> {
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 96.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Text(
                                text = "${uiState.destinations.size} destinasi ditemukan",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                        items(
                            items = uiState.destinations,
                            key = { it.id }
                        ) { destination ->
                            WishlistDestinationCard(
                                destination = destination,
                                onClick = { onNavigateToDestinationDetail(destination.id) },
                                onDelete = { viewModel.deleteDestination(destination) }
                            )
                        }
                        item {
                            DestinationButton(
                                onClick = onNavigateToAddDestination,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}