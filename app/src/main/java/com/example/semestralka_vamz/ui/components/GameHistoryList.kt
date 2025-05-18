package com.example.semestralka_vamz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.R
import androidx.compose.foundation.lazy.items

@Composable
fun GameHistoryList(history: List<com.example.semestralka_vamz.data.model.GameHistoryEntry>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(history) { entry ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(stringResource(R.string.date) + ": ${entry.date}")
                    Text(stringResource(R.string.duration) + ": ${entry.duration}")
                    Text(stringResource(R.string.attempts) + ": ${entry.attempts}")
                    Text(
                        text = stringResource(
                            if (entry.isWin) R.string.result_win else R.string.result_loss
                        ),
                        color = if (entry.isWin) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                }
            }
        }
    }
}

