package com.example.semestralka_vamz.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.semestralka_vamz.notification.NotificationHelper

class DailyChallengeWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // TODO: Тут додай логіку для створення денної гри
        NotificationHelper.showNotification(applicationContext)
        return Result.success()
    }
}
