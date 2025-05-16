package com.example.semestralka_vamz.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
enum class AppTheme { Light, Dark }
enum class AppLanguage { EN, SK }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onNewGame: () -> Unit = {},
    onDailyChallenge: () -> Unit = {},
    onRules: () -> Unit = {},
    onStats: () -> Unit = {},
    theme: AppTheme,
    onThemeChange: (AppTheme) -> Unit = {},
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit = {}
) {
    var showSettings by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "CodeBreaker",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Button(onClick = onNewGame, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("New Game")
            }

            Button(onClick = onDailyChallenge, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Daily Challenge")
            }

            Button(onClick = onRules, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Rules")
            }

            Button(onClick = onStats, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Stats")
            }
        }

        IconButton(
            onClick = { showSettings = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Change settings")
        }

        if (showSettings) {
            ModalBottomSheet(onDismissRequest = { showSettings = false }) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Theme", style = MaterialTheme.typography.titleMedium)
                    AppTheme.entries.forEach { t ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().clickable {
                                onThemeChange(t)
                                showSettings = false
                            }.padding(8.dp)
                        ) {
                            RadioButton(selected = theme == t, onClick = null)
                            Text(t.name)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Language", style = MaterialTheme.typography.titleMedium)
                    AppLanguage.entries.forEach { lang ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().clickable {
                                onLanguageChange(lang)
                                showSettings = false
                            }.padding(8.dp)
                        ) {
                            RadioButton(selected = language == lang, onClick = null)
                            Text(lang.name)
                        }
                    }
                }
            }
        }
    }
}
