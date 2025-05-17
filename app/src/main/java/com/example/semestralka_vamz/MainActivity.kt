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
import com.example.semestralka_vamz.store.LanguageStorage
import com.example.semestralka_vamz.navigation.AppNavGraph
import com.example.semestralka_vamz.ui.theme.AppThemeWrapper
import com.example.semestralka_vamz.viewmodel.SettingsViewModel
import com.example.semestralka_vamz.viewmodel.SettingsViewModelFactory
import com.example.semestralka_vamz.worker.DailyChallengeScheduler

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)
        }
        val startDestination = when (intent?.getStringExtra("navigateTo")) {
            "daily_challenge" -> "daily_challenge"
            else -> "main_menu"
        }
//        val initialLanguage = LanguageStorage.loadLanguage(applicationContext)
        DailyChallengeScheduler.schedule(applicationContext)
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel( factory = SettingsViewModelFactory(applicationContext))
            val language by settingsViewModel.language.collectAsState()
            AppLanguageWrapper(language = language) {
                val theme by settingsViewModel.theme.collectAsState()
                AppThemeWrapper(theme = theme) {
                    val navController = rememberNavController()
                    LaunchedEffect(Unit) {
                        if (startDestination != "main_menu") {
                            navController.navigate(startDestination)
                        }
                    }
                    AppNavGraph(
                        navController = navController,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }

    }

}
