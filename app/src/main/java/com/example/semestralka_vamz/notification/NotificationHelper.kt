package com.example.semestralka_vamz.notification

// Importy potrebné pre notifikácie, lokalizáciu a UI
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.semestralka_vamz.MainActivity
import com.example.semestralka_vamz.R
import com.example.semestralka_vamz.data.model.AppLanguage
import com.example.semestralka_vamz.store.LanguageStorage
import com.example.semestralka_vamz.ui.language.createLocalizedContext
import java.util.Locale

// Pomocný objekt na zobrazovanie notifikácií používateľovi
object NotificationHelper {

    // ID pre notifikačný kanál
    private const val CHANNEL_ID = "daily_challenge_channel"

    // ID bežnej notifikácie
    private const val NOTIFICATION_ID = 1001

    // ID notifikácie s výsledkom hry
    private const val RESULT_NOTIFICATION_ID = 1002

    // Základná denná pripomienka (napr. na nový denný challenge)
    fun showNotification(context: Context) {
        showBaseNotification(
            context = context,
            titleRes = R.string.notification_title,
            textRes = R.string.notification_text
        )
    }

    // Notifikácia v prípade výhry v dennej výzve – zahŕňa počet pokusov
    fun showWinNotification(context: Context, attempts: Int) {
        val appLanguage = LanguageStorage.loadLanguage(context)
        val locale = when (appLanguage) {
            AppLanguage.EN -> Locale.ENGLISH
            AppLanguage.SK -> Locale("sk")
        }
        val localizedContext = context.createLocalizedContext(locale)

        val text = localizedContext.getString(R.string.win_notification_text, attempts)

        showBaseNotification(
            context = context,
            titleRes = R.string.result_win_end,
            text = text,
            notificationId = RESULT_NOTIFICATION_ID
        )
    }

    // Notifikácia v prípade prehry v dennej výzve
    fun showLoseNotification(context: Context) {
        showBaseNotification(
            context = context,
            titleRes = R.string.result_loss_daylly,
            textRes = R.string.lose_notification_text,
            notificationId = RESULT_NOTIFICATION_ID
        )
    }

    // Vnútorná metóda pre tvorbu notifikácie s možnosťou použiť text z resource alebo priamo string
    private fun showBaseNotification(
        context: Context,
        titleRes: Int,
        textRes: Int? = null,
        text: String? = null,
        notificationId: Int = NOTIFICATION_ID
    ) {
        val localizedContext = context.createLocalizedContextWithStorage()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Intent na otvorenie aplikácie po kliknutí na notifikáciu
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("navigateTo", "daily_challenge") // môže sa využiť na navigáciu v aplikácii
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Vytvorenie notifikačného kanálu pre Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                localizedContext.getString(R.string.notification_title),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        // Skladanie obsahu notifikácie
        val notificationText = text ?: localizedContext.getString(textRes!!)
        val notification = NotificationCompat.Builder(localizedContext, CHANNEL_ID)
            .setContentTitle(localizedContext.getString(titleRes))
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Zobrazenie notifikácie
        manager.notify(notificationId, notification)
    }

    // Pomocná funkcia na vytvorenie lokalizovaného kontextu podľa uloženého jazyka
    private fun Context.createLocalizedContextWithStorage(): Context {
        val lang = LanguageStorage.loadLanguage(this)
        val locale = when (lang) {
            AppLanguage.EN -> Locale.ENGLISH
            AppLanguage.SK -> Locale("sk")
        }
        return createLocalizedContext(locale)
    }
}
