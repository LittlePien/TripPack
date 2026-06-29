package com.example.trippack.ui.trip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trippack.ui.components.FormErrorText
import com.example.trippack.ui.components.SelectableField
import com.example.trippack.ui.components.SimpleTopBar

@Composable
fun CreateTripScreen(
    onTripCreated: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CreateTripViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.createdTripId) {
        uiState.createdTripId?.let { onTripCreated(it) }
    }

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "Buat Trip Baru",
                onNavigateBack = onNavigateBack,
                backIcon = Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "Atur rencana perjalananmu, kami siapkan packing list otomatis sesuai cuaca tujuan.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Box {
                SelectableField(
                    label = "Destinasi",
                    value = uiState.selectedDestination?.name ?: "Pilih destinasi...",
                    onClick = { if (uiState.destinations.isNotEmpty()) expanded = true },
                    leadingIcon = Icons.Default.LocationOn
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    uiState.destinations.forEach { destination ->
                        DropdownMenuItem(
                            text = { Text(destination.name) },
                            onClick = {
                                viewModel.onDestinationSelected(destination)
                                expanded = false
                            }
                        )
                    }
                }
            }
            if (uiState.destinations.isEmpty()) {
                Text(
                    "Belum ada destinasi di wishlist, tambahkan dulu dari halaman Wishlist",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            SelectableField(
                label = "Tanggal Berangkat",
                value = viewModel.formatDate(uiState.startDate),
                onClick = { showStartDatePicker = true },
                leadingIcon = Icons.Default.DateRange
            )
            if (showStartDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showStartDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { viewModel.onStartDateChange(it) }
                            showStartDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showStartDatePicker = false }) { Text("Batal") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            SelectableField(
                label = "Tanggal Pulang",
                value = viewModel.formatDate(uiState.endDate),
                onClick = { showEndDatePicker = true },
                leadingIcon = Icons.Default.DateRange
            )
            if (showEndDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showEndDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { viewModel.onEndDateChange(it) }
                            showEndDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEndDatePicker = false }) { Text("Batal") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Column {
                Text(
                    "Jumlah Traveler",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    (1..5).forEach { count ->
                        val isSelected = uiState.travelerCount == count
                        Button(
                            onClick = { viewModel.onTravelerCountChange(count) },
                            colors = if (isSelected) {
                                ButtonDefaults.buttonColors()
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("$count")
                        }
                    }
                }
            }

            OutlinedTextField(
                value = uiState.estimatedBudget,
                onValueChange = viewModel::onBudgetChange,
                label = { Text("Estimasi Budget (opsional)") },
                prefix = { Text("Rp") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            uiState.errorMessage?.let { message ->
                FormErrorText(message)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { viewModel.createTrip() },
                enabled = !uiState.isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Buat Trip & Generate Packing", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}