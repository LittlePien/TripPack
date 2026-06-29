package com.example.trippack.ui.packing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trippack.ui.components.AddPackingItemBar
import com.example.trippack.ui.components.PackingItemRow
import com.example.trippack.ui.components.PackingListEmptyState
import com.example.trippack.ui.components.PackingListHeader


@Composable
fun PackingListScreen(
    onNavigateBack: () -> Unit,
    viewModel: PackingListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val trip = uiState.trip

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AddPackingItemBar(
                value = uiState.newItemName,
                onValueChange = viewModel::onNewItemNameChange,
                onAdd = viewModel::addCustomItem
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            PackingListHeader(
                tripName = trip?.destinationName ?: "Packing List",
                dateRange = if (trip != null)
                    "${viewModel.formatDate(trip.startDate)} – ${viewModel.formatDate(trip.endDate)}"
                else "",
                checkedCount = uiState.items.count { it.isChecked },
                totalCount = uiState.items.size,
                onNavigateBack = onNavigateBack
            )

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(32.dp)
                    )
                }

                uiState.items.isEmpty() -> {
                    PackingListEmptyState()
                }

                else -> {
                    val unchecked = uiState.items.filter { !it.isChecked }
                    val checked = uiState.items.filter { it.isChecked }

                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (unchecked.isNotEmpty()) {
                            item {
                                SectionLabel(text = "Belum dikemas (${unchecked.size})")
                            }
                            items(unchecked, key = { it.id }) { item ->
                                PackingItemRow(
                                    item = item,
                                    onToggle = { viewModel.toggleChecked(item) },
                                    onDelete = { viewModel.deleteItem(item) }
                                )
                            }
                        }

                        if (checked.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(4.dp))
                                SectionLabel(text = "Sudah dikemas (${checked.size})")
                            }
                            items(checked, key = { it.id }) { item ->
                                PackingItemRow(
                                    item = item,
                                    onToggle = { viewModel.toggleChecked(item) },
                                    onDelete = { viewModel.deleteItem(item) }
                                )
                            }
                        }


                    }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
    )
}

