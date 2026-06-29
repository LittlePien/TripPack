package com.example.trippack.ui.packing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PackingListScreen(
    viewModel: PackingListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Packing List")

        Row {
            OutlinedTextField(
                value = uiState.newItemName,
                onValueChange = viewModel::onNewItemNameChange,
                label = {Text("Tambah item baru")},
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {viewModel.addCustomItem()}) {
                Text("Tambah")
            }
        }

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.items.isEmpty()) {
            Text("Belum ada item packing")
        } else {
            LazyColumn {
                items(uiState.items, key = {it.id}) { item ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Checkbox(
                            checked = item.isChecked,
                            onCheckedChange = {viewModel.toggleChecked(item)}
                        )
                        Text(
                            item.name,
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        )
                        if (item.isAutoSuggested) {
                            Text("(saran)", color = MaterialTheme.colorScheme.primary)
                        }
                        Button(onClick = {viewModel.deleteItem(item)}) {
                            Text("Hapus")
                        }
                    }
                }
            }
        }
    }
}