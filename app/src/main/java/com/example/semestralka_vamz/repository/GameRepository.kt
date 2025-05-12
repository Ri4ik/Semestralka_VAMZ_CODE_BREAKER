package com.example.semestralka_vamz.repository

import com.example.semestralka_vamz.data.dao.GameResultDao
import com.example.semestralka_vamz.data.model.GameResult
import kotlinx.coroutines.flow.Flow

class GameRepository(private val dao: GameResultDao) {

    fun getAllResults(): Flow<List<GameResult>> = dao.getAllResults()

    suspend fun insertGameResult(result: GameResult) {
        dao.insertResult(result)
    }

    suspend fun getGamesPlayed(): Int = dao.getGamesPlayed()

    suspend fun getGamesWon(): Int = dao.getGamesWon()

    suspend fun getAvgGuesses(): Double = dao.getAvgGuesses()

    suspend fun getAvgTime(): Double = dao.getAvgTime()

    /**
     * Vyhodnotí porovnanie zadanej kombinácie s cieľovým kódom.
     * @return Pair<Int, Int> = (správne čísla na správnom mieste, správne čísla na nesprávnom mieste)
     */
    fun evaluateGuess(secret: List<Int>, guess: List<Int>): Pair<Int, Int> {
        val used = BooleanArray(secret.size)
        val matched = BooleanArray(guess.size)

        var exact = 0
        var partial = 0

        // Najprv zhodné pozície
        for (i in secret.indices) {
            if (guess[i] == secret[i]) {
                exact++
                used[i] = true
                matched[i] = true
            }
        }

        // Potom správne čísla na zlej pozícii
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
