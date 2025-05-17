package com.example.semestralka_vamz.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.semestralka_vamz.data.AppDatabase
import com.example.semestralka_vamz.data.model.GameStatsEntity
import com.example.semestralka_vamz.data.model.GuessState
import com.example.semestralka_vamz.store.DailyChallengeStorage
import com.example.semestralka_vamz.utils.generateDailyCode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val statsDao = AppDatabase.getInstance(application).gameStatsDao()

    var isDailyChallenge = false
        private set

    private val _guessHistory = MutableStateFlow<List<GuessState>>(emptyList())
    val guessHistory: StateFlow<List<GuessState>> = _guessHistory.asStateFlow()

    private val _gameFinished = MutableStateFlow(false)
    val gameFinished: StateFlow<Boolean> = _gameFinished.asStateFlow()

    val maxAttempts = 8
    var secretCode = listOf(1, 2, 3, 4) // временно хардкодим
    private val _attemptsUsed = MutableStateFlow(0)
    val attemptsUsed: StateFlow<Int> = _attemptsUsed.asStateFlow()

    private val _currentGuess = MutableStateFlow(List(4) { 0 })
    val currentGuess: StateFlow<List<Int>> = _currentGuess.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0)
    val elapsedTime: StateFlow<Int> = _elapsedTime.asStateFlow()

    private var timerJob: Job? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun startDailyChallenge() {
        val code = generateDailyCode()
        isDailyChallenge = true
        startNewGame(code)
    }
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _elapsedTime.value += 1
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
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

    fun updateGuess(index: Int, value: Int) {
        _currentGuess.value = _currentGuess.value.toMutableList().also {
            if (index in it.indices) it[index] = value
        }
    }

    fun startNewGame(secret: List<Int> = List(4) { 0 }) {
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
