package com.example.semestralka_vamz.store

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

object DailyChallengeStorage {

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveDailyProgress(context: Context, solved: Boolean, attempts: Int, seconds: Int) {
        val prefs = context.getSharedPreferences("daily_challenge", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("lastPlayedDate", LocalDate.now().toString())
            .putBoolean("wasSolved", solved)
            .putInt("attempts", attempts)
            .putInt("duration", seconds)
            .apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadDailyProgress(context: Context): Triple<LocalDate?, Boolean, Pair<Int, Int>> {
        val prefs = context.getSharedPreferences("daily_challenge", Context.MODE_PRIVATE)
        val date = prefs.getString("lastPlayedDate", null)?.let { LocalDate.parse(it) }
        val solved = prefs.getBoolean("wasSolved", false)
        val attempts = prefs.getInt("attempts", 0)
        val duration = prefs.getInt("duration", 0)
        return Triple(date, solved, attempts to duration)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun secondsUntilMidnight(): Long {
        val now = LocalDateTime.now()
        val midnight = now.toLocalDate().plusDays(1).atStartOfDay()
        return Duration.between(now, midnight).seconds
    }
}
