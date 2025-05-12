package com.example.semestralka_vamz.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.semestralka_vamz.data.model.GameStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameStatsDao {
    @Insert
    suspend fun insertGameStats(stats: GameStatsEntity)

    @Query("SELECT * FROM game_stats ORDER BY date DESC")
    fun getAllStats(): Flow<List<GameStatsEntity>>

    @Query("SELECT COUNT(*) FROM game_stats")
    fun getGamesPlayed(): Flow<Int>

    @Query("SELECT COUNT(*) FROM game_stats WHERE isWin = 1")
    fun getGamesWon(): Flow<Int>

    @Query("SELECT AVG(attempts) FROM game_stats")
    fun getAvgAttempts(): Flow<Double>

    @Query("SELECT AVG(durationSeconds) FROM game_stats")
    fun getAvgDuration(): Flow<Double>
}
