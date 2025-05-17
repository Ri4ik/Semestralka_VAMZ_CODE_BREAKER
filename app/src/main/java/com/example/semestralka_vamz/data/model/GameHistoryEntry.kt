package com.example.semestralka_vamz.data.model

data class GameHistoryEntry(
    val date: String,
    val duration: String,
    val attempts: Int,
    val isWin: Boolean
)