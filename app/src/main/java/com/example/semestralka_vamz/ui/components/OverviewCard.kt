package com.example.semestralka_vamz.ui.components

// Importy pre layout, štýly a lokalizáciu
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

// Komponent, ktorý zobrazuje prehľad štatistík vo forme karty
@Composable
fun OverviewCard(overview: com.example.semestralka_vamz.data.model.StatsOverview) {
    // Nadpis "Prehľad"
    Text(stringResource(R.string.overview), style = MaterialTheme.typography.titleMedium)

    // Karta so štatistikami
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp) // zvislé rozostupy medzi riadkami
        ) {
            // Celkový počet hier
            Text(stringResource(R.string.games_played) + ": ${overview.gamesPlayed}")
            // Počet výhier
            Text(stringResource(R.string.games_won) + ": ${overview.gamesWon}")
            // Priemerný počet pokusov
            Text(stringResource(R.string.avg_attempts, overview.avgAttempts))
            // Priemerný čas
            Text(stringResource(R.string.avg_time) + ": ${overview.avgTime}")
        }
    }
}
