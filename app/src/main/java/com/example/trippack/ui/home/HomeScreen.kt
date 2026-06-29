package com.example.trippack.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardTravel
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trippack.ui.components.DestinationPreviewCard
import com.example.trippack.ui.components.StatDivider
import com.example.trippack.ui.components.StatItem
import com.example.trippack.ui.components.UpcomingTripCard

@Composable
fun HomeScreen(
    onNavigateToWishlist: () -> Unit,
    onNavigateToPackingList: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(Modifier.weight(1f), uiState.wishlistCount, "Wishlist", Icons.Outlined.FavoriteBorder)
                StatDivider()
                StatItem(Modifier.weight(1f), uiState.tripCount, "Trips", Icons.Outlined.CardTravel)
                StatDivider()
                StatItem(Modifier.weight(1f), uiState.completedCount, "Selesai", Icons.Outlined.TaskAlt)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            uiState.upcomingTrip?.let { trip ->
                UpcomingTripCard(
                    destinationName = trip.destinationName,
                    daysUntilTrip = uiState.daysUntilTrip,
                    onClick = { onNavigateToPackingList(trip.id) }
                )
            } ?: Text(
                "Belum ada trip mendatang",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(28.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Destinasi Pilihan", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(
                    "Lihat semua",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onNavigateToWishlist() }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.previewDestinations.isEmpty()) {
                Text(
                    "Belum ada destinasi di wishlist, yuk tambahkan impianmu",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.previewDestinations, key = { it.id }) { destination ->
                        DestinationPreviewCard(destination)
                    }
                }
            }
        }
    }
}