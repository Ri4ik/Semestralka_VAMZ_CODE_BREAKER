package com.example.semestralka_vamz.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.semestralka_vamz.data.model.GameResult

@Dao
interface GameResultDao {

    @Insert
    suspend fun insertResult(result: GameResult)

    @Query("SELECT * FROM GameResult ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<GameResult>>

    @Query("SELECT COUNT(*) FROM GameResult")
    suspend fun getGamesPlayed(): Int

    @Query("SELECT COUNT(*) FROM GameResult WHERE won = 1")
    suspend fun getGamesWon(): Int

    @Query("SELECT AVG(guessesUsed) FROM GameResult")
    suspend fun getAvgGuesses(): Double

    @Query("SELECT AVG(durationMillis) FROM GameResult")
    suspend fun getAvgTime(): Double
}
