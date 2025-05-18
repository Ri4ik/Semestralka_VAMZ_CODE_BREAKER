package com.example.semestralka_vamz.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.semestralka_vamz.viewmodel.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.semestralka_vamz.R
import com.example.semestralka_vamz.store.DailyChallengeStorage
import com.example.semestralka_vamz.ui.components.DropdownMenuGuess
import com.example.semestralka_vamz.ui.components.GuessRow
import java.time.LocalDate
import com.example.semestralka_vamz.ui.components.DailyChallengeResultDialog
import com.example.semestralka_vamz.viewmodel.GameViewModelFactory

@SuppressLint("StringFormatInvalid")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GameScreen(
    navEntry: androidx.navigation.NavBackStackEntry,
//    viewModel: GameViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as Application)),
    onBack: () -> Unit = {},
    isDailyChallenge: Boolean = false
) {
    val context = LocalContext.current
    val viewModel: GameViewModel = viewModel(navEntry, factory = GameViewModelFactory(context.applicationContext as Application))
    val guessHistory by viewModel.guessHistory.collectAsState()
    val attemptsUsed by viewModel.attemptsUsed.collectAsState()
    val gameFinished by viewModel.gameFinished.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()

    var showResultDialog by remember { mutableStateOf(false) }
    var wasWin by remember { mutableStateOf(false) }
    var savedAttempts by remember { mutableStateOf(0) }
    var savedTime by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {

        if (viewModel.isGameStarted) {
            viewModel.resumeTimerIfNeeded()
            return@LaunchedEffect
        }
        val (date, solved, stats) = DailyChallengeStorage.loadDailyProgress(context)
        val today = LocalDate.now()

        if (isDailyChallenge) {
            if (date == today && solved) {
                wasWin = true
                savedAttempts = stats.first
                savedTime = stats.second
                showResultDialog = true
            } else {
                viewModel.startDailyChallenge()
            }
        } else {
            viewModel.startNewGame()
        }
    }

    // Повноекранне повідомлення, якщо денне завдання вже виконано
    if (isDailyChallenge && showResultDialog) {
        DailyChallengeResultDialog(
            isWin = wasWin,
            attempts = savedAttempts,
            durationSeconds = savedTime
        ) {
            showResultDialog = false
            onBack()
        }
        return
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Top bar
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Time: ${viewModel.formatElapsedTime(elapsedTime)}",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                text = if (isDailyChallenge)
                    stringResource(R.string.menu_daily_challenge)
                else
                    stringResource(R.string.game)
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
            if (!isDailyChallenge) {
                Button(onClick = { viewModel.restartGame() }) {
                    Text(stringResource(R.string.restart))
                }
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
