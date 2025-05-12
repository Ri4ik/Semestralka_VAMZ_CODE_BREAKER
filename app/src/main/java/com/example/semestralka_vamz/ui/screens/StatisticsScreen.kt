package com.example.semestralka_vamz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.semestralka_vamz.viewmodel.StatisticsViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val overview by viewModel.overview.collectAsState()
    val history by viewModel.history.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("STATISTICS") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("OVERVIEW", style = MaterialTheme.typography.titleMedium)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Games played: ${overview.gamesPlayed}")
                    Text("Games won: ${overview.gamesWon}")
                    Text("Avg. attempts: ${overview.avgAttempts}")
                    Text("Avg. time: ${overview.avgTime}")
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(history) { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2A2A2A)
                        )
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Date: ${entry.date}")
                            Text("Duration: ${entry.duration}")
                            Text("Attempts: ${entry.attempts}")
                            Text(
                                text = "Result: ${entry.result}",
                                color = if (entry.result == "Win") Color(0xFF4CAF50) else Color(0xFFF44336)
                            )
                        }
                    }
                }
            }
        }
    }
}
