package com.example.semestralka_vamz.ui.screens

// Importy pre komponenty, layout, stav a spracovanie práce (WorkManager)
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.semestralka_vamz.BuildConfig
import com.example.semestralka_vamz.R
import com.example.semestralka_vamz.data.model.AppLanguage
import com.example.semestralka_vamz.data.model.AppTheme
import com.example.semestralka_vamz.store.LanguageStorage
import com.example.semestralka_vamz.worker.DailyChallengeWorker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onNewGame: () -> Unit = {},                 // Callback na začatie novej hry
    onDailyChallenge: () -> Unit = {},         // Callback na dennú výzvu
    onRules: () -> Unit = {},                  // Callback na zobrazenie pravidiel
    onStats: () -> Unit = {},                  // Callback na štatistiky
    theme: AppTheme,                           // Aktuálne zvolená téma
    onThemeChange: (AppTheme) -> Unit = {},    // Callback na zmenu témy
    language: AppLanguage,                     // Aktuálne zvolený jazyk
    onLanguageChange: (AppLanguage) -> Unit = {} // Callback na zmenu jazyka
) {
    var showSettings by remember { mutableStateOf(false) } // Otvorený spodný panel s nastaveniami
    val context = LocalContext.current

    // Hlavný kontajner obrazovky
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        val scrollState = rememberScrollState()

        // Vertikálne menu s tlačidlami
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Názov aplikácie
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            // Tlačidlo: Nová hra
            Button(onClick = onNewGame, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(id = R.string.menu_new_game))
            }

            // Tlačidlo: Denná výzva
            Button(onClick = onDailyChallenge, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(id = R.string.menu_daily_challenge))
            }

            // Tlačidlo: Pravidlá
            Button(onClick = onRules, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(id = R.string.menu_rules))
            }

            // Tlačidlo: Štatistiky
            Button(onClick = onStats, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(stringResource(id = R.string.menu_statistics))
            }

            // Tlačidlo len pre debug mód – testovacia notifikácia
//                Button(
//                    onClick = {
//                        val request = OneTimeWorkRequestBuilder<DailyChallengeWorker>().build()
//                        WorkManager.getInstance(context).enqueue(request)
//                    },
//                    modifier = Modifier.fillMaxWidth(0.8f)
//                ) {
//                    Text(stringResource(R.string.test_notification))
//                }
//            }
        }

        //  Tlačidlo na otvorenie nastavení (pravý dolný roh)
        IconButton(
            onClick = { showSettings = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Change settings")
        }

        //  Spodný panel s nastaveniami (téma + jazyk)
        if (showSettings) {
            ModalBottomSheet(onDismissRequest = { showSettings = false }) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Sekcia: Téma aplikácie
                    Text(context.getString(R.string.settings_theme), style = MaterialTheme.typography.titleMedium)
                    AppTheme.entries.forEach { t ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onThemeChange(t)  // zmena témy
                                    showSettings = false
                                }
                                .padding(8.dp)
                        ) {
                            RadioButton(selected = theme == t, onClick = null)
                            Text(t.name)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sekcia: Jazyk aplikácie
                    Text(context.getString(R.string.settings_language), style = MaterialTheme.typography.titleMedium)
                    AppLanguage.entries.forEach { lang ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    LanguageStorage.saveLanguage(context, lang) // uloženie do SharedPreferences
                                    onLanguageChange(lang)                      // aktualizácia v nastaveniach
                                    showSettings = false
                                }
                                .padding(8.dp)
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
