package com.example.semestralka_vamz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import com.example.semestralka_vamz.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun OverviewCard(overview: com.example.semestralka_vamz.data.model.StatsOverview) {
    Text(stringResource(R.string.overview), style = MaterialTheme.typography.titleMedium)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.games_played) + ": ${overview.gamesPlayed}")
            Text(stringResource(R.string.games_won) + ": ${overview.gamesWon}")
            Text(stringResource(R.string.avg_attempts, overview.avgAttempts))
            Text(stringResource(R.string.avg_time) + ": ${overview.avgTime}")
        }
    }
}
