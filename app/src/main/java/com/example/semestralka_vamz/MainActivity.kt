package com.example.semestralka_vamz

import AppLanguageWrapper
import android.os.Build
import android.os.Bundle
import androidx.work.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.semestralka_vamz.language.LanguageStorage
import com.example.semestralka_vamz.navigation.AppNavGraph
import com.example.semestralka_vamz.ui.theme.AppThemeWrapper
import com.example.semestralka_vamz.viewmodel.SettingsViewModel
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
        val initialLanguage = LanguageStorage.loadLanguage(applicationContext)
        DailyChallengeScheduler.schedule(applicationContext)
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            AppLanguageWrapper(language = initialLanguage) {
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

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
////        val db = AppDatabase.getInstance(applicationContext) // реалізація Room DB
////        val dao = db.gameStatsDao()
////        val factory = StatisticsViewModelFactory(dao)
//
//        setContent {
////            val viewModel: StatisticsViewModel = viewModel(factory = factory)
////            StatisticsScreen(viewModel = viewModel)
//            GameScreen()
////            RulesScreen()
////            MainMenuScreen()
////            StatisticsScreen()
//        }
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
////            GameScreen() // твій стартовий екран
////            RulesScreen()
//            StatisticsScreen()
////            MainMenuScreen()
//        }
//    }
//}
