package com.example.trippack.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trippack.domain.model.Trip

@Composable
fun ExpandableTripCard(
    trip: Trip,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onCompleteTrip: () -> Unit,
    onDeleteTrip: () -> Unit,
    onEditTrip: () -> Unit,
    onViewPackingList: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text(trip.destinationName)
                Text(if (trip.isCompleted) "Selesai" else "Mendatang")
            }
            Button(onClick = onToggleExpand) {
                Text(if (isExpanded) "Tutup" else "Lihat")
            }
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onViewPackingList) {
                Text("Lihat Packing List")
            }
            if (!trip.isCompleted) {
                Button(onClick = onEditTrip) {
                    Text("Edit Trip")
                }
                Button(onClick = onCompleteTrip) {
                    Text("Selesaikan Perjalanan")
                }
            }
            Button(onClick = onDeleteTrip) {
                Text("Hapus")
            }
        }
    }
}