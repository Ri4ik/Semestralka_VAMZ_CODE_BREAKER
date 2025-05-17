package com.example.semestralka_vamz.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semestralka_vamz.data.dao.GameStatsDao
import com.example.semestralka_vamz.data.model.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class StatisticsViewModel(
    private val dao: GameStatsDao
) : ViewModel() {

    val overview: StateFlow<StatsOverview> = combine(
        dao.getGamesPlayed(),
        dao.getGamesWon(),
        dao.getAvgAttempts().map { it ?: 0.0 },
        dao.getAvgDuration().map { it ?: 0.0 }
    ) { played, won, attempts, duration ->
        StatsOverview(
            gamesPlayed = played,
            gamesWon = won,
            avgAttempts = attempts,
            avgTime = formatDuration(duration)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsOverview(0, 0, 0.0, "0s"))

    @RequiresApi(Build.VERSION_CODES.O)
    val history: StateFlow<List<GameHistoryEntry>> = dao.getAllStats()
        .map { entities ->
            entities.map {
                GameHistoryEntry(
                    date = formatDateTime(it.date),
                    duration = formatDuration(it.durationSeconds),
                    attempts = it.attempts,
                    isWin = it.isWin
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun formatDuration(seconds: Double): String {
        val mins = seconds.toInt() / 60
        val secs = seconds.toInt() % 60
        return "${mins}m ${secs}s"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateTime(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
        return date.format(formatter)
    }
}
