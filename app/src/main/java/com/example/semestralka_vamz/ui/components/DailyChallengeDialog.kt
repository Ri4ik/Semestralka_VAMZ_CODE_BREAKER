package com.example.semestralka_vamz.ui.components

// Importy pre Compose UI, prácu s časom a lokalizáciu
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.R
import com.example.semestralka_vamz.store.DailyChallengeStorage

// Dialóg zobrazujúci výsledok dennej výzvy – výhra alebo prehra + štatistiky
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyChallengeResultDialog(
    isWin: Boolean,             // Označuje, či hráč vyhral
    attempts: Int,              // Počet pokusov
    durationSeconds: Int,       // Dĺžka hry v sekundách
    onDismiss: () -> Unit       // Callback pri zatvorení dialógu
) {
    val context = LocalContext.current
    val remainingSeconds = DailyChallengeStorage.secondsUntilMidnight()
    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60

    // Zobrazenie dialógu
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "OK") // Potvrdenie (zatvorenie)
            }
        },
        title = {
            Text(
                text = if (isWin)
                    context.getString(R.string.daily_result_win) // Výhra
                else
                    context.getString(R.string.daily_result_loss) // Prehra
            )
        },
        text = {
            Column {
                Text(context.getString(R.string.daily_attempts, attempts)) // Zobrazenie počtu pokusov
                Spacer(modifier = Modifier.height(8.dp))
                Text(context.getString(R.string.daily_time, formatElapsedTime(durationSeconds))) // Dĺžka hry
                Spacer(modifier = Modifier.height(8.dp))
                Text(context.getString(R.string.next_challenge_in_ui, "%02d:%02d:%02d".format(hours, minutes, seconds))) // Čas do novej výzvy
            }
        }
    )
}

// Pomocná funkcia na formátovanie času zo sekúnd do formátu MM:SS
fun formatElapsedTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(minutes, secs)
}
