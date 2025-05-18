package com.example.semestralka_vamz.worker

// Importy pre prácu s WorkManagerom a notifikáciami
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.semestralka_vamz.notification.NotificationHelper

// Worker trieda, ktorá sa spúšťa automaticky raz denne (naplánovaná cez WorkManager)
class DailyChallengeWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    // Hlavná logika práce, ktorá sa vykoná na pozadí
    override suspend fun doWork(): Result {
        // Zobrazenie notifikácie používateľovi
        NotificationHelper.showNotification(applicationContext)

        // Označenie úspešného dokončenia úlohy
        return Result.success()
    }
}
