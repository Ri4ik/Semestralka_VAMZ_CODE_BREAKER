package com.example.semestralka_vamz.ui.screens

// Importy základných komponentov Compose a ViewModel
import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.viewmodel.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.semestralka_vamz.R
import com.example.semestralka_vamz.data.model.AppTheme
import com.example.semestralka_vamz.store.DailyChallengeStorage
import com.example.semestralka_vamz.ui.components.*
import com.example.semestralka_vamz.viewmodel.GameViewModelFactory
import java.time.LocalDate

@SuppressLint("StringFormatInvalid")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GameScreen(
    navEntry: androidx.navigation.NavBackStackEntry,
    onBack: () -> Unit = {},
    isDailyChallenge: Boolean = false,
    theme: AppTheme
) {
    // Inicializácia ViewModelu
    val context = LocalContext.current
    val viewModel: GameViewModel = viewModel(navEntry, factory = GameViewModelFactory(context.applicationContext as Application))

    // Získavame stavy z ViewModelu
    val guessHistory by viewModel.guessHistory.collectAsState()
    val attemptsUsed by viewModel.attemptsUsed.collectAsState()
    val gameFinished by viewModel.gameFinished.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()

    // Príznaky pre dialógy
    var showResultDialogs by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var wasWin by remember { mutableStateOf(false) }
    var savedAttempts by remember { mutableStateOf(0) }
    var savedTime by remember { mutableStateOf(0) }

    // Načítanie a inicializácia hry
    LaunchedEffect(Unit) {
        if (viewModel.isGameStarted) {
            viewModel.resumeTimerIfNeeded()
            return@LaunchedEffect
        }

        val (date, solved, stats) = DailyChallengeStorage.loadDailyProgress(context)
        val today = LocalDate.now()

        if (isDailyChallenge) {
            if (date == today) {
                wasWin = solved
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

    // Zobrazenie výsledku dennej výzvy
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

    // Zobrazenie bežného výsledku po skončení hry
    LaunchedEffect(gameFinished) {
        if (gameFinished) {
            showResultDialogs = true
        }
    }
    if (showResultDialogs) {
        GameResultDialog(
            isDaylly = isDailyChallenge,
            isWin = guessHistory.lastOrNull()?.isCorrect == true,
            attempts = attemptsUsed,
            durationSeconds = elapsedTime,
            onPlayAgain = {
                viewModel.restartGame()
                showResultDialogs = false
            },
            onMainMenu = {
                showResultDialogs = false
                onBack()
            }
        )
    }

    // Detekcia orientácie obrazovky
    val configuration = LocalContext.current.resources.configuration
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    // Hlavný kontajner s pozadím podľa témy
    Box(modifier = Modifier.fillMaxSize().padding(0.dp)) {
        val backgroundRes = if (theme == AppTheme.Dark) R.drawable.background_dark else R.drawable.background_light
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Portrétna orientácia
        if (isPortrait) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Horný panel
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        if (!isDailyChallenge) {
                            IconButton(onClick = onBack) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                        Text(
                            text = stringResource(R.string.daily_time, viewModel.formatElapsedTime(elapsedTime)),
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

                // Zoznam pokusov
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(guessHistory) { guess ->
                        GuessRow(guess)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Aktuálny pokus – výber čísel cez NumberPicker
                val currentGuess by viewModel.currentGuess.collectAsState()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(4) { index ->
                        NumberPicker(
                            selected = currentGuess.getOrElse(index) { 0 },
                            onSelect = { viewModel.setGuessAt(index, it) },
                            modifier = Modifier.width(64.dp)
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
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                    Button(
                        onClick = { viewModel.submitGuess() },
                        enabled = !gameFinished
                    ) {
                        Text(stringResource(R.string.check_answer))
                    }
                }
            }

        } else {
            // Krajinná orientácia
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    if (!isDailyChallenge) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
                Text(
                    text = if (isDailyChallenge)
                        stringResource(R.string.menu_daily_challenge)
                    else
                        stringResource(R.string.game) + "   " +
                                stringResource(R.string.daily_time, viewModel.formatElapsedTime(elapsedTime)),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(text = "$attemptsUsed / ${viewModel.maxAttempts}")
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Hlavný obsah – rozdelený na 2 stĺpce
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Ľavý stĺpec: história pokusov
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(guessHistory) { guess ->
                        GuessRow(guess)
                    }
                }

                // Pravý stĺpec: NumberPicker + tlačidlá
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val currentGuess by viewModel.currentGuess.collectAsState()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(4) { index ->
                            NumberPicker(
                                selected = currentGuess.getOrElse(index) { 0 },
                                onSelect = { viewModel.setGuessAt(index, it) },
                                modifier = Modifier.width(64.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
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
        }
    }
}
