package com.example.semestralka_vamz.worker


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.util.Calendar

object DailyChallengeScheduler {
    @RequiresApi(Build.VERSION_CODES.O)
    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<DailyChallengeWorker>(
            Duration.ofMinutes(15) // ⏰ change to Duration.ofDays(1) for daily mode
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_challenge_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}
//fun scheduleDailyChallenge(context: Context) {
//    val dailyRequest = PeriodicWorkRequestBuilder<DailyChallengeWorker>(
//        1, TimeUnit.DAYS
//    )
//        .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
//        .build()
//
//    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//        "daily_challenge_work",
//        ExistingPeriodicWorkPolicy.UPDATE,
//        dailyRequest
//    )
//}

fun calculateInitialDelay(): Long {
    val now = Calendar.getInstance()
    val nextRun = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 4)
        set(Calendar.MINUTE, 40)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        add(Calendar.DAY_OF_YEAR, 1)
    }
    return nextRun.timeInMillis - now.timeInMillis
}