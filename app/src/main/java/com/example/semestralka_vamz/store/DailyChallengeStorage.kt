package com.example.semestralka_vamz.store

// Importy pre prácu s časom a uloženými dátami
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

// Objekt zodpovedný za ukladanie a načítanie údajov o dennej výzve pomocou SharedPreferences
object DailyChallengeStorage {

    // Uloženie výsledku dennej výzvy: dátum, či bola vyriešená, počet pokusov a dĺžka trvania
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveDailyProgress(context: Context, solved: Boolean, attempts: Int, seconds: Int) {
        val prefs = context.getSharedPreferences("daily_challenge", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("lastPlayedDate", LocalDate.now().toString()) // dnešný dátum
            .putBoolean("wasSolved", solved)                          // true = vyriešená
            .putInt("attempts", attempts)                             // počet pokusov
            .putInt("duration", seconds)                              // čas v sekundách
            .apply()
    }

    // Načítanie uložených údajov o poslednej dennej výzve
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDailyProgress(context: Context): Triple<LocalDate?, Boolean, Pair<Int, Int>> {
        val prefs = context.getSharedPreferences("daily_challenge", Context.MODE_PRIVATE)
        val date = prefs.getString("lastPlayedDate", null)?.let { LocalDate.parse(it) }
        val solved = prefs.getBoolean("wasSolved", false)
        val attempts = prefs.getInt("attempts", 0)
        val duration = prefs.getInt("duration", 0)
        return Triple(date, solved, attempts to duration)
        // Výstup: dátum, vyriešené?, (pokusy, trvanie)
    }

    // Výpočet zostávajúcich sekúnd do polnoci (na odpočet novej výzvy)
    @RequiresApi(Build.VERSION_CODES.O)
    fun secondsUntilMidnight(): Long {
        val now = LocalDateTime.now()
        val midnight = now.toLocalDate().plusDays(1).atStartOfDay()
        return Duration.between(now, midnight).seconds
    }
}
