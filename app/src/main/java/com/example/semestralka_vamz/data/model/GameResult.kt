package com.example.semestralka_vamz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val code: String,
    val guessesUsed: Int,
    val totalGuesses: Int,
    val durationMillis: Long,
    val won: Boolean
)
