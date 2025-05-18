package com.example.semestralka_vamz.viewmodel

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val statsDao = AppDatabase.getInstance(application).gameStatsDao()

    var isDailyChallenge = false
        private set
    private var gameStarted = false
    val isGameStarted: Boolean
        get() = gameStarted

    private val _guessHistory = MutableStateFlow<List<GuessState>>(emptyList())
    val guessHistory: StateFlow<List<GuessState>> = _guessHistory.asStateFlow()

    private val _gameFinished = MutableStateFlow(false)
    val gameFinished: StateFlow<Boolean> = _gameFinished.asStateFlow()

    val maxAttempts = 8
    var secretCode = listOf(1, 2, 3, 4)
    private val _attemptsUsed = MutableStateFlow(0)
    val attemptsUsed: StateFlow<Int> = _attemptsUsed.asStateFlow()

    private val _currentGuess = MutableStateFlow(List(4) { 0 })
    val currentGuess: StateFlow<List<Int>> = _currentGuess.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0)
    val elapsedTime: StateFlow<Int> = _elapsedTime.asStateFlow()

    private var timerJob: Job? = null
    private var isTimerRunning = false

    @RequiresApi(Build.VERSION_CODES.O)
    fun startDailyChallenge() {
        if (gameStarted) return
        val code = generateDailyCode()
        isDailyChallenge = true
        startNewGame(code)
    }
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

    fun formatElapsedTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return if (hours > 0)
            String.format("%02d:%02d:%02d", hours, minutes, secs)
        else
            String.format("%02d:%02d", minutes, secs)
    }

    fun setGuessAt(index: Int, value: Int) {
        _currentGuess.value = _currentGuess.value.toMutableList().also {
            it[index] = value
        }
    }

    private fun generateCode(): List<Int> {
        val random = Random(System.currentTimeMillis())
        return List(4) { random.nextInt(0, 10) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateDailyCode(): List<Int> {
        val today = LocalDate.now()
        val seed = today.toEpochDay()
        val random = Random(seed)
        return List(4) { random.nextInt(0, 10) }
    }

    fun startNewGame(secret: List<Int> = generateCode()) {
        if (gameStarted) return
        gameStarted = true
        secretCode = secret
        _guessHistory.value = emptyList()
        _attemptsUsed.value = 0
        _gameFinished.value = false
        _currentGuess.value = List(secretCode.size) { 0 }
        _elapsedTime.value = 0
        startTimer()
    }

    fun restartGame(secret: List<Int> = generateCode()) {
        gameStarted = true
        secretCode = secret
        _guessHistory.value = emptyList()
        _attemptsUsed.value = 0
        _gameFinished.value = false
        _currentGuess.value = List(secretCode.size) { 0 }
        _elapsedTime.value = 0
        startTimer()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun submitGuess() {
        if (_gameFinished.value || _currentGuess.value.size != secretCode.size) return

        val current = _currentGuess.value
        val result = evaluateGuess(secretCode, current)

        _guessHistory.value = _guessHistory.value + result
        _attemptsUsed.value = _guessHistory.value.size

        if (result.isCorrect || _guessHistory.value.size >= maxAttempts) {
            _gameFinished.value = true
            stopTimer()
            if (isDailyChallenge) {
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
            // 💾 Сохраняем статистику
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

    private fun evaluateGuess(secret: List<Int>, guess: List<Int>): GuessState {
        val exact = mutableListOf<Int>()
        val partial = mutableListOf<Int>()
        val used = BooleanArray(secret.size)
        val matched = BooleanArray(guess.size)

        for (i in secret.indices) {
            if (guess[i] == secret[i]) {
                exact += i
                used[i] = true
                matched[i] = true
            }
        }

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
