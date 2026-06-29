package com.example.trippack.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(uiState.name)
        Text(uiState.email)

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text("${uiState.tripCount}")
                Text("Trips")
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("${uiState.destinationCount}")
                Text("Wishlist")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            viewModel.logout()
            onLogout()
        }) {
            Text("Keluar")
        }
    }
}