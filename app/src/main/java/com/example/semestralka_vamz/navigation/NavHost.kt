package com.example.semestralka_vamz.navigation

// Importy pre Compose navigáciu, ViewModel, a obrazovky
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
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

// Navigačný graf aplikácie – definuje všetky obrazovky a ich trasy
@RequiresApi(Build.VERSION_CODES.O) // niektoré obrazovky používajú LocalDateTime (od Android 8.0)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel // zdieľaný ViewModel pre tému a jazyk
) {
    // Definícia navigačného stromu, s počiatočnou obrazovkou MAIN_MENU
    NavHost(navController = navController, startDestination = Routes.MAIN_MENU) {

        // Hlavné menu
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

        // Klasická hra
        composable(Routes.GAME) { backStackEntry ->
            val theme by settingsViewModel.theme.collectAsState()
            GameScreen(
                navEntry = backStackEntry,
                onBack = { navController.popBackStack() },
                isDailyChallenge = false,
                theme = theme
            )
        }

        // Denná výzva
        composable(Routes.DAILY_CHALLENGE) { backStackEntry ->
            val theme by settingsViewModel.theme.collectAsState()
            GameScreen(
                navEntry = backStackEntry,
                onBack = { navController.popBackStack() },
                isDailyChallenge = true,
                theme = theme
            )
        }

        // Obrazovka s pravidlami
        composable(Routes.RULES) {
            val theme by settingsViewModel.theme.collectAsState()
            RulesScreen(onBack = { navController.popBackStack() }, theme = theme)
        }

        // Obrazovka so štatistikami
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
