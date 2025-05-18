package com.example.semestralka_vamz.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.semestralka_vamz.notification.NotificationHelper

class DailyChallengeWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        NotificationHelper.showNotification(applicationContext)
        return Result.success()
    }
}
