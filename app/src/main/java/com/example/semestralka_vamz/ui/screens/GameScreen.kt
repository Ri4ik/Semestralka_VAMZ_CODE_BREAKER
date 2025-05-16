package com.example.semestralka_vamz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.viewmodel.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.semestralka_vamz.R
import com.example.semestralka_vamz.ui.components.DropdownMenuGuess
import com.example.semestralka_vamz.ui.components.GuessRow

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel(),
    onBack: () -> Unit = {},
    isDailyChallenge: Boolean = false
) {
    val guessHistory by viewModel.guessHistory.collectAsState()
    val attemptsUsed by viewModel.attemptsUsed.collectAsState()
    val gameFinished by viewModel.gameFinished.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Top bar
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(text = if (isDailyChallenge) stringResource(R.string.menu_daily_challenge) else stringResource(
                R.string.game
            )
            )
            Text(text = "$attemptsUsed / ${viewModel.maxAttempts}")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn – pokusy
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(guessHistory) { guess ->
                GuessRow(guess)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Створюємо нову змінну — активний масив з ViewModel
        val currentGuess by viewModel.currentGuess.collectAsState()
        // Aktívny pokus – výber 4 čísel
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(4) { index ->
                DropdownMenuGuess(
                    selected = currentGuess.getOrElse(index) { 0 },
                    onSelect = { viewModel.setGuessAt(index, it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Ovládacie tlačidlá
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { viewModel.startNewGame() }) {
                Text(stringResource(R.string.restart))
            }
            Button(
                onClick = { viewModel.submitGuess() },
                enabled = !gameFinished
            ) {
                Text(stringResource(R.string.check_answer))
            }
        }
    }
}
