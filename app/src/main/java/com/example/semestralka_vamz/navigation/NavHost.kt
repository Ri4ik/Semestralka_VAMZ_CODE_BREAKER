package com.example.semestralka_vamz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.semestralka_vamz.ui.screens.*
import com.example.semestralka_vamz.viewmodel.SettingsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    NavHost(navController = navController, startDestination = Routes.MAIN_MENU) {

        composable(Routes.MAIN_MENU) {
            val theme by settingsViewModel.theme.collectAsState()
            val language by settingsViewModel.language.collectAsState()
            MainMenuScreen(
                theme = theme,
                language = language,
                onNewGame = { navController.navigate(Routes.GAME) },
                onDailyChallenge = { navController.navigate(Routes.DAILY_CHALLENGE) },
                onRules = { navController.navigate(Routes.RULES) },
                onStats = { navController.navigate(Routes.STATS) },
                onThemeChange = settingsViewModel::setTheme,
                onLanguageChange = settingsViewModel::setLanguage
            )
        }

        composable(Routes.GAME) {
            GameScreen(onBack = { navController.popBackStack() }, isDailyChallenge = false)
        }

        composable(Routes.DAILY_CHALLENGE) {
            GameScreen(onBack = { navController.popBackStack() }, isDailyChallenge = true)
        }

        composable(Routes.RULES) {
            RulesScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.STATS) {
            StatisticsScreen(onBack = { navController.popBackStack() })
        }
    }
}
