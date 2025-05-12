package com.example.semestralka_vamz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semestralka_vamz.data.dao.GameStatsDao
import com.example.semestralka_vamz.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StatisticsViewModel(
    private val dao: GameStatsDao
) : ViewModel() {

    val overview: StateFlow<StatsOverview> =
        combine(
            dao.getGamesPlayed(),
            dao.getGamesWon(),
            dao.getAvgAttempts().map { it ?: 0.0 },
            dao.getAvgDuration().map { it ?: 0.0 }
        ) { played, won, attempts, duration ->
            StatsOverview(
                gamesPlayed = played,
                gamesWon = won,
                avgAttempts = String.format("%.1f", attempts).toDouble(),
                avgTime = formatDuration(duration)
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsOverview(0, 0, 0.0, "0s"))

    val history: StateFlow<List<GameHistoryEntry>> = dao.getAllStats()
        .map { entities ->
            entities.map {
                GameHistoryEntry(
                    date = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it.date),
                    duration = formatDuration(it.durationSeconds),
                    attempts = it.attempts,
                    result = if (it.isWin) "Win" else "Loss"
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun formatDuration(seconds: Double): String {
        val mins = seconds.toInt() / 60
        val secs = seconds.toInt() % 60
        return "${mins}m ${secs}s"
    }
}
