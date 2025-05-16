package com.example.semestralka_vamz.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.semestralka_vamz.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.MAIN_MENU) {

        composable(Routes.MAIN_MENU) {
            MainMenuScreen(
                onNewGame = { navController.navigate(Routes.GAME) },
                onDailyChallenge = { navController.navigate(Routes.DAILY_CHALLENGE) },
                onRules = { navController.navigate(Routes.RULES) },
                onStats = { navController.navigate(Routes.STATS) },
                onToggleLanguage = { /* TODO */ }
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
