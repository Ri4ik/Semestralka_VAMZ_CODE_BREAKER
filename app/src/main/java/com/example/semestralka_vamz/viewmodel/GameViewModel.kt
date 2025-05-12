package com.example.semestralka_vamz.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GuessState(
    val guess: List<Int>,
    val exact: Int,
    val partial: Int,
    val isCorrect: Boolean
)

class GameViewModel : ViewModel() {

    private val _guessHistory = MutableStateFlow<List<GuessState>>(emptyList())
    val guessHistory: StateFlow<List<GuessState>> = _guessHistory.asStateFlow()

    private val _gameFinished = MutableStateFlow(false)
    val gameFinished: StateFlow<Boolean> = _gameFinished.asStateFlow()

    val maxAttempts = 8
    val secretCode = listOf(1, 2, 3, 4) // временно хардкодим
    private val _attemptsUsed = MutableStateFlow(0)
    val attemptsUsed: StateFlow<Int> = _attemptsUsed.asStateFlow()

    val _currentGuess = MutableStateFlow(List(4) { 0 })
    val currentGuess: StateFlow<List<Int>> = _currentGuess.asStateFlow()

    fun setGuessAt(index: Int, value: Int) {
        _currentGuess.value = _currentGuess.value.toMutableList().also {
            it[index] = value
        }
    }

    fun submitGuess() {
        if (_gameFinished.value || _currentGuess.value.size != secretCode.size) return
        val current = _currentGuess.value
        val (exact, partial) = evaluateGuess(secretCode, current)
        val result = GuessState(
            guess = current,
            exact = exact,
            partial = partial,
            isCorrect = exact == secretCode.size
        )

        _guessHistory.value = _guessHistory.value + result
        _attemptsUsed.value = _guessHistory.value.size

        if (result.isCorrect || _guessHistory.value.size >= maxAttempts) {
            _gameFinished.value = true
        }

        // Скидаємо currentGuess назад у 0000
//        _currentGuess.value = List(secretCode.size) { 0 }
    }
    fun updateGuess(index: Int, value: Int) {
        _currentGuess.value = _currentGuess.value.toMutableList().also {
            if (index in it.indices) it[index] = value
        }
    }
    fun startNewGame() {
        _guessHistory.value = emptyList()
        _attemptsUsed.value = 0
        _gameFinished.value = false
        _currentGuess.value = List(secretCode.size) { 0 }
        // TODO: regenerate secretCode if needed
    }

    private fun evaluateGuess(secret: List<Int>, guess: List<Int>): Pair<Int, Int> {
        var exact = 0
        var partial = 0
        val used = BooleanArray(secret.size)
        val matched = BooleanArray(guess.size)

        for (i in secret.indices) {
            if (guess[i] == secret[i]) {
                exact++
                used[i] = true
                matched[i] = true
            }
        }

        for (i in guess.indices) {
            if (matched[i]) continue
            for (j in secret.indices) {
                if (!used[j] && guess[i] == secret[j]) {
                    partial++
                    used[j] = true
                    break
                }
            }
        }

        return exact to partial
    }
}
