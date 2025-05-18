package com.example.semestralka_vamz.data.dao

// Importy potrebných anotácií a tried z Room a Flow API
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.semestralka_vamz.data.model.GameStatsEntity
import kotlinx.coroutines.flow.Flow

// Označenie rozhrania ako DAO (Data Access Object) pre Room databázu
@Dao
interface GameStatsDao {

    // Vloženie jednej inštancie štatistiky hry do databázy
    @Insert
    suspend fun insertGameStats(stats: GameStatsEntity)

    // Výber všetkých záznamov zo štatistiky hier, zoradených podľa dátumu zostupne
    @Query("SELECT * FROM game_stats ORDER BY date DESC")
    fun getAllStats(): Flow<List<GameStatsEntity>>

    // Počet všetkých odohraných hier
    @Query("SELECT COUNT(*) FROM game_stats")
    fun getGamesPlayed(): Flow<Int>

    // Počet víťazných hier (isWin = 1)
    @Query("SELECT COUNT(*) FROM game_stats WHERE isWin = 1")
    fun getGamesWon(): Flow<Int>

    // Priemerný počet pokusov vo všetkých hrách
    @Query("SELECT AVG(attempts) FROM game_stats")
    fun getAvgAttempts(): Flow<Double>

    // Priemerný čas trvania hry v sekundách
    @Query("SELECT AVG(durationSeconds) FROM game_stats")
    fun getAvgDuration(): Flow<Double>
}
