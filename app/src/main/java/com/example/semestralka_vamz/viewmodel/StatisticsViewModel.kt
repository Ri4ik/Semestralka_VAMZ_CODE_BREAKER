package com.example.semestralka_vamz.viewmodel

// Importy pre ViewModel, coroutine flow a formátovanie dátumu/času
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semestralka_vamz.data.dao.GameStatsDao
import com.example.semestralka_vamz.data.model.*
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class StatisticsViewModel(
    private val dao: GameStatsDao // DAO pre prístup k herným štatistikám
) : ViewModel() {

    // Kombinovaný flow – vytvára objekt StatsOverview zo 4 tokov naraz:
    // počet hier, výhier, priemerné pokusy a čas
    val overview: StateFlow<StatsOverview> = combine(
        dao.getGamesPlayed(),                        // celkový počet hier
        dao.getGamesWon(),                           // počet výhier
        dao.getAvgAttempts().map { it },             // priemerný počet pokusov
        dao.getAvgDuration().map { it }              // priemerné trvanie v sekundách
    ) { played, won, attempts, duration ->
        StatsOverview(
            gamesPlayed = played,
            gamesWon = won,
            avgAttempts = attempts,
            avgTime = formatDuration(duration)       // formátovanie času do stringu
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),
        StatsOverview(0, 0, 0.0, "0s")                // default hodnota, ak tok ešte neemitoval dáta
    )

    // História všetkých hier – mapuje zoznam GameStatsEntity na GameHistoryEntry
    @RequiresApi(Build.VERSION_CODES.O)
    val history: StateFlow<List<GameHistoryEntry>> = dao.getAllStats()
        .map { entities ->
            entities.map {
                GameHistoryEntry(
                    date = formatDateTime(it.date),               // dátum hry vo formáte dd.MM.yyyy
                    duration = formatDuration(it.durationSeconds),// čas hry vo formáte Xm Ys
                    attempts = it.attempts,                       // počet pokusov
                    isWin = it.isWin                              // výhra/prehra
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Pomocná funkcia na formátovanie trvania z double (sekundy) na string typu "Xm Ys"
    private fun formatDuration(seconds: Double): String {
        val mins = seconds.toInt() / 60
        val secs = seconds.toInt() % 60
        return "${mins}m ${secs}s"
    }

    // Pomocná funkcia na formátovanie dátumu LocalDateTime na "dd.MM.yyyy"
    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateTime(date: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
        return date.format(formatter)
    }
}
