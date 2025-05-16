package com.example.semestralka_vamz

import AppLanguageWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.semestralka_vamz.navigation.AppNavGraph
import com.example.semestralka_vamz.ui.theme.AppThemeWrapper
import com.example.semestralka_vamz.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val theme by settingsViewModel.theme.collectAsState()
            val language by settingsViewModel.language.collectAsState()
            AppLanguageWrapper(language = language) {
                AppThemeWrapper(theme = theme) {
                    val navController = rememberNavController()
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
