package com.example.semestralka_vamz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "game_stats")
data class GameStatsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDateTime,
    val durationSeconds: Double,
    val attempts: Int,
    val isWin: Boolean
)