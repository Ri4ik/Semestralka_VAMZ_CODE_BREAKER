package com.example.semestralka_vamz.worker

// Importy pre prácu so schedulovanými úlohami a časom
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration

// Objekt zodpovedný za naplánovanie dennej úlohy (daily challenge)
object DailyChallengeScheduler {

    // Funkcia, ktorá naplánuje úlohu na pozadí, ktorá sa spúšťa raz denne
    @RequiresApi(Build.VERSION_CODES.O)
    fun schedule(context: Context) {
        // Vytvorenie požiadavky: úloha sa spustí každých 24 hodín
        val request = PeriodicWorkRequestBuilder<DailyChallengeWorker>(
            Duration.ofDays(1)
        ).build()

        // Naplánovanie jedinečnej periodickej práce (zabraňuje duplikátom)
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_challenge_work",                 // názov úlohy
            ExistingPeriodicWorkPolicy.UPDATE,      // ak už existuje, prepíše sa
            request                                 // samotná požiadavka
        )
    }
}
