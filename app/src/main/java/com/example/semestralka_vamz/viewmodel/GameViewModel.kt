package com.example.semestralka_vamz.viewmodel

// Importy pre ViewModel, databázu, notifikácie, dátum/čas a coroutine toky
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.semestralka_vamz.data.AppDatabase
import com.example.semestralka_vamz.data.model.GameStatsEntity
import com.example.semestralka_vamz.data.model.GuessState
import com.example.semestralka_vamz.notification.NotificationHelper
import com.example.semestralka_vamz.store.DailyChallengeStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val statsDao = AppDatabase.getInstance(application).gameStatsDao() // DAO pre štatistiky

    var isDailyChallenge = false // indikátor, či ide o dennú výzvu
        private set

    private var gameStarted = false
    val isGameStarted: Boolean
        get() = gameStarted

    // História všetkých pokusov
    private val _guessHistory = MutableStateFlow<List<GuessState>>(emptyList())
    val guessHistory: StateFlow<List<GuessState>> = _guessHistory.asStateFlow()

    // Príznak konca hry
    private val _gameFinished = MutableStateFlow(false)
    val gameFinished: StateFlow<Boolean> = _gameFinished.asStateFlow()

    val maxAttempts = 8
    var secretCode = listOf(1, 2, 3, 4) // tajný kód

    private val _attemptsUsed = MutableStateFlow(0)
    val attemptsUsed: StateFlow<Int> = _attemptsUsed.asStateFlow()

    private val _currentGuess = MutableStateFlow(List(4) { 0 })
    val currentGuess: StateFlow<List<Int>> = _currentGuess.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0) // uplynutý čas v sekundách
    val elapsedTime: StateFlow<Int> = _elapsedTime.asStateFlow()

    private var timerJob: Job? = null
    private var isTimerRunning = false

    // Spustenie dennej výzvy (so zafixovaným kódom)
    @RequiresApi(Build.VERSION_CODES.O)
    fun startDailyChallenge() {
        if (gameStarted) return
        val code = generateDailyCode()
        isDailyChallenge = true
        startNewGame(code)
    }

    // Interný timer (beží každú sekundu a zvyšuje čas)
    private fun startTimer() {
        if (isTimerRunning) return
        isTimerRunning = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _elapsedTime.value += 1
            }
        }
    }

    private fun stopTimer() {
        isTimerRunning = false
        timerJob?.cancel()
    }

    fun resumeTimerIfNeeded() {
        if (!_gameFinished.value && !isTimerRunning) {
            startTimer()
        }
    }

    // Formátovanie času do formátu MM:SS alebo HH:MM:SS
    fun formatElapsedTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return if (hours > 0)
            String.format("%02d:%02d:%02d", hours, minutes, secs)
        else
            String.format("%02d:%02d", minutes, secs)
    }

    // Nastavenie číslice na konkrétnom indexe v aktuálnom pokuse
    fun setGuessAt(index: Int, value: Int) {
        _currentGuess.value = _currentGuess.value.toMutableList().also {
            it[index] = value
        }
    }

    // Generovanie náhodného kódu pre bežnú hru
    private fun generateCode(): List<Int> {
        val random = Random(System.currentTimeMillis())
        return List(4) { random.nextInt(0, 10) }
    }

    // Generovanie deterministického kódu pre dennú výzvu (na základe dátumu)
    @RequiresApi(Build.VERSION_CODES.O)
    fun generateDailyCode(): List<Int> {
        val today = LocalDate.now()
        val seed = today.toEpochDay()
        val random = Random(seed)
        return List(4) { random.nextInt(0, 10) }
    }

    // Spustenie novej hry – volá restartGame
    fun startNewGame(secret: List<Int> = generateCode()) {
        if (gameStarted) return
        restartGame(secret)
    }

    // Resetuje všetky premenné a spúšťa hru odznova
    fun restartGame(secret: List<Int> = generateCode()) {
        gameStarted = true
        secretCode = secret
        _guessHistory.value = emptyList()
        _attemptsUsed.value = 0
        _gameFinished.value = false
        _elapsedTime.value = 0
        startTimer()
    }

    // Odoslanie aktuálneho pokusu a vyhodnotenie výsledku
    @RequiresApi(Build.VERSION_CODES.O)
    fun submitGuess() {
        if (_gameFinished.value || _currentGuess.value.size != secretCode.size) return

        val current = _currentGuess.value
        val result = evaluateGuess(secretCode, current)

        _guessHistory.value = _guessHistory.value + result
        _attemptsUsed.value = _guessHistory.value.size

        // Skončenie hry – výhra alebo prekročenie počtu pokusov
        if (result.isCorrect || _guessHistory.value.size >= maxAttempts) {
            _gameFinished.value = true
            stopTimer()

            if (isDailyChallenge) {
                // Uloženie výsledkov dennej výzvy
                DailyChallengeStorage.saveDailyProgress(
                    getApplication(),
                    result.isCorrect,
                    _attemptsUsed.value,
                    _elapsedTime.value
                )

                val context = getApplication<Application>().applicationContext
                if (result.isCorrect) {
                    NotificationHelper.showWinNotification(context, _attemptsUsed.value)
                } else {
                    NotificationHelper.showLoseNotification(context)
                }
            }

            // Uloženie do databázy ako GameStatsEntity
            val stats = GameStatsEntity(
                date = LocalDateTime.now(),
                durationSeconds = _elapsedTime.value.toDouble(),
                attempts = _attemptsUsed.value,
                isWin = result.isCorrect
            )
            viewModelScope.launch {
                statsDao.insertGameStats(stats)
            }
        }
    }

    // Porovnanie hádaného kódu s tajným kódom
    private fun evaluateGuess(secret: List<Int>, guess: List<Int>): GuessState {
        val exact = mutableListOf<Int>()
        val partial = mutableListOf<Int>()
        val used = BooleanArray(secret.size)
        val matched = BooleanArray(guess.size)

        // Hľadáme presné zhody
        for (i in secret.indices) {
            if (guess[i] == secret[i]) {
                exact += i
                used[i] = true
                matched[i] = true
            }
        }

        // Hľadáme čiastočné zhody
        for (i in guess.indices) {
            if (matched[i]) continue
            for (j in secret.indices) {
                if (!used[j] && guess[i] == secret[j]) {
                    partial += i
                    used[j] = true
                    break
                }
            }
        }

        return GuessState(
            guess = guess,
            isCorrect = exact.size == secret.size,
            exactIndices = exact,
            partialIndices = partial
        )
    }
}
