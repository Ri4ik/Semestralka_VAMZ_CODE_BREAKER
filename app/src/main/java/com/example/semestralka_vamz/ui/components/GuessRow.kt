package com.example.semestralka_vamz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.semestralka_vamz.data.model.GuessState

@Composable
fun GuessRow(guessState: GuessState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            guessState.guess.forEachIndexed { index, value ->
                val color = when (index) {
                    in guessState.exactIndices -> Color(0xFF4CAF50) // Зелений
                    in guessState.partialIndices -> Color(0xFFFFEB3B) // Жовтий
                    else -> Color(0xFF90CAF9) // Блакитний
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, shape = MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = value.toString(), color = Color.Black)
                }
            }
        }

        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Correct",
            tint = if (guessState.isCorrect) Color(0xFF4CAF50) else Color.Gray,
            modifier = Modifier
                .size(24.dp)
                .alpha(if (guessState.isCorrect) 1f else 0.3f)
        )
    }
}
