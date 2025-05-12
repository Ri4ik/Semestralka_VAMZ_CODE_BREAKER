package com.example.semestralka_vamz.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DropdownMenuGuess(
    selected: Int,
    onSelect: (Int) -> Unit,
    options: List<Int> = (0..9).toList()
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = selected.toString(),
            modifier = Modifier
                .clickable { expanded = true }
                .padding(16.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
