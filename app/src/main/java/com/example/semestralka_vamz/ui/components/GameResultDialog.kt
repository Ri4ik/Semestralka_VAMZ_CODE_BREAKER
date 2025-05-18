package com.example.semestralka_vamz.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.R

@Composable
fun GameResultDialog(
    isDaylly: Boolean,
    isWin: Boolean,
    attempts: Int,
    durationSeconds: Int,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {
    val context = LocalContext.current

    val title = if (isWin) {
        context.getString(R.string.result_win)
    } else {
        context.getString(R.string.result_loss)
    }

    val time = formatElapsedTime(durationSeconds)

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(title)
        },
        text = {
            Column {
                Text(context.getString(R.string.daily_attempts, attempts))
                Spacer(modifier = Modifier.height(8.dp))
                Text(context.getString(R.string.daily_time, time))
            }
        },
        confirmButton = {
            if (!isDaylly) {
                Button(onClick = onPlayAgain) {
                    Text(context.getString(R.string.try_againe))
                }
            }
        },

        dismissButton = {
            TextButton(onClick = onMainMenu) {
                Text(context.getString(R.string.menu_main))
            }
        }
    )
}
