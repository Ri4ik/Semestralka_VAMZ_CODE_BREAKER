package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.viewmodel.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Top bar
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(text = if (isDailyChallenge) "Daily Challenge" else "Game")
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

        // Aktívny pokus – výber 4 čísel
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(4) { index ->
                var selected by remember { mutableStateOf(0) }
                DropdownMenuGuess(
                    selected = selected,
                    onSelect = {
                        selected = it
                        viewModel.setGuessAt(index, it)
                    }
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
                Text("Restart")
            }
            Button(
                onClick = { viewModel.submitGuess() },
                enabled = !gameFinished
            ) {
                Text("Check Answer")
            }
        }
    }
}
