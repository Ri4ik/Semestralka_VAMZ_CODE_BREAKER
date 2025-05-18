package com.example.semestralka_vamz.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.semestralka_vamz.data.AppDatabase
import com.example.semestralka_vamz.ui.screens.*
import com.example.semestralka_vamz.viewmodel.SettingsViewModel
import com.example.semestralka_vamz.viewmodel.StatisticsViewModel
import com.example.semestralka_vamz.viewmodel.StatisticsViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
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

        composable(Routes.GAME) { backStackEntry ->
            val theme by settingsViewModel.theme.collectAsState()
            GameScreen(
                navEntry = backStackEntry,
                onBack = { navController.popBackStack() },
                isDailyChallenge = false,
                theme = theme)
        }

        composable(Routes.DAILY_CHALLENGE) { backStackEntry ->
            val theme by settingsViewModel.theme.collectAsState()
            GameScreen(
                navEntry = backStackEntry,
                onBack = { navController.popBackStack() },
                isDailyChallenge = true,
                theme = theme)
        }

        composable(Routes.RULES) {
            val theme by settingsViewModel.theme.collectAsState()
            RulesScreen(onBack = { navController.popBackStack() }, theme = theme)
        }

        composable(Routes.STATS) {
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val dao = db.gameStatsDao()
            val factory = remember { StatisticsViewModelFactory(dao) }
            val viewModel: StatisticsViewModel = viewModel(factory = factory)

            StatisticsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
    }
}
