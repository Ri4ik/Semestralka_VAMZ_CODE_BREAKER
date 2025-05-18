package com.example.semestralka_vamz.worker


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration

object DailyChallengeScheduler {
    @RequiresApi(Build.VERSION_CODES.O)
    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<DailyChallengeWorker>(
            Duration.ofDays(1) // ⏰ change to Duration.ofDays(1) for daily mode
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_challenge_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}