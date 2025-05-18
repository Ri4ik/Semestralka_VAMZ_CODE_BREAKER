package com.example.semestralka_vamz

import AppLanguageWrapper
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.semestralka_vamz.navigation.AppNavGraph
import com.example.semestralka_vamz.ui.theme.AppThemeWrapper
import com.example.semestralka_vamz.viewmodel.SettingsViewModel
import com.example.semestralka_vamz.viewmodel.SettingsViewModelFactory
import com.example.semestralka_vamz.worker.DailyChallengeScheduler
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Požiadanie o povolenie na notifikácie od Android 13 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)
        }

        // Nastavenie režimu na celú obrazovku – skrytie systémových panelov
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
        }

        // Naplánovanie dennej výzvy len raz pri prvom spustení
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val hasScheduled = prefs.getBoolean("has_scheduled", false)
        if (!hasScheduled) {
            DailyChallengeScheduler.schedule(applicationContext)
            prefs.edit { putBoolean("has_scheduled", true) }
        }

        // Určenie počiatočnej obrazovky na základe intentu (napr. po kliknutí na notifikáciu)
        val startDestination = when (intent?.getStringExtra("navigateTo")) {
            "daily_challenge" -> "daily_challenge"
            else -> "main_menu"
        }

        // Spustenie UI s Jetpack Compose
        setContent {
            val settingsViewModel: SettingsViewModel =
                viewModel(factory = SettingsViewModelFactory(applicationContext))

            val language by settingsViewModel.language.collectAsState()

            // Aplikovanie jazykového kontextu
            AppLanguageWrapper(language = language) {

                val theme by settingsViewModel.theme.collectAsState()

                // Aplikovanie vizuálnej témy
                AppThemeWrapper(theme = theme) {

                    val navController = rememberNavController()

                    // Navigácia na počiatočnú obrazovku ak je iná než main_menu
                    LaunchedEffect(Unit) {
                        if (startDestination != "main_menu") {
                            navController.navigate(startDestination)
                        }
                    }

                    // Spustenie navigačného grafu
                    AppNavGraph(
                        navController = navController,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}
