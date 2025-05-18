package com.example.semestralka_vamz.ui.components

// Importy pre layout, štýl a kontext
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.R

// Dialóg, ktorý zobrazuje výsledok hry (klasickej alebo dennej výzvy)
@Composable
fun GameResultDialog(
    isDaylly: Boolean,           // true = denná výzva, false = bežná hra
    isWin: Boolean,              // true = výhra, false = prehra
    attempts: Int,               // počet pokusov
    durationSeconds: Int,        // trvanie hry v sekundách
    onPlayAgain: () -> Unit,     // callback pre nové spustenie hry
    onMainMenu: () -> Unit       // callback pre návrat do hlavného menu
) {
    val context = LocalContext.current

    // Nastavenie nadpisu podľa výsledku
    val title = if (isWin) {
        context.getString(R.string.result_win_end)
    } else {
        context.getString(R.string.result_loss_end)
    }

    // Formátovaný čas v MM:SS
    val time = formatElapsedTime(durationSeconds)

    AlertDialog(
        onDismissRequest = {}, // zakázané zatvorenie kliknutím mimo
        title = {
            Text(title) // výhra / prehra
        },
        text = {
            Column {
                Text(context.getString(R.string.daily_attempts, attempts)) // počet pokusov
                Spacer(modifier = Modifier.height(8.dp))
                Text(context.getString(R.string.daily_time, time)) // čas
            }
        },
        confirmButton = {
            if (!isDaylly) {
                // Tlačidlo "Skúsiť znova" sa zobrazuje len v klasickom móde
                Button(onClick = onPlayAgain) {
                    Text(context.getString(R.string.try_againe))
                }
            }
        },
        dismissButton = {
            // Tlačidlo pre návrat do hlavného menu
            TextButton(onClick = onMainMenu) {
                Text(context.getString(R.string.menu_main))
            }
        }
    )
}
