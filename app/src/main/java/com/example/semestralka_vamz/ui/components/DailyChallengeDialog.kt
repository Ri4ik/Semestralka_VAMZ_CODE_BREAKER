package com.example.semestralka_vamz.ui.components

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyChallengeResultDialog(
    isWin: Boolean,
    attempts: Int,
    durationSeconds: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val remainingSeconds = DailyChallengeStorage.secondsUntilMidnight()
    val hours = remainingSeconds / 3600
    val minutes = (remainingSeconds % 3600) / 60
    val seconds = remainingSeconds % 60

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "OK")
            }
        },
        title = {
            Text(
                text = if (isWin)
                    context.getString(R.string.daily_result_win)
                else
                    context.getString(R.string.daily_result_loss)
            )
        },
        text = {
            Column {
                Text(context.getString(R.string.daily_attempts, attempts))
                Spacer(modifier = Modifier.height(8.dp))
                Text(context.getString(R.string.daily_time, formatElapsedTime(durationSeconds)))
                Spacer(modifier = Modifier.height(8.dp))
                Text(context.getString(R.string.next_challenge_in_ui, "%02d:%02d:%02d".format(hours, minutes, seconds)))
            }
        }
    )
}

fun formatElapsedTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(minutes, secs)
}
