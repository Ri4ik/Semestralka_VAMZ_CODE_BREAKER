package com.example.semestralka_vamz.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.semestralka_vamz.R

@Composable
fun MainMenuScreen(
    onNewGame: () -> Unit = {},
    onDailyChallenge: () -> Unit = {},
    onRules: () -> Unit = {},
    onStats: () -> Unit = {},
    onToggleLanguage: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "CodeBreaker",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onNewGame,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("New Game")
            }

            Button(
                onClick = onDailyChallenge,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Daily Challenge")
            }

            Button(
                onClick = onRules,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Rules")
            }

            Button(
                onClick = onStats,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Stats")
            }
        }

        IconButton(
            onClick = onToggleLanguage,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Change language")
        }
    }
}
