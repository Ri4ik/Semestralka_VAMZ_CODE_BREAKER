package com.example.semestralka_vamz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.semestralka_vamz.data.AppDatabase
import com.example.semestralka_vamz.ui.screens.GameScreen
import com.example.semestralka_vamz.ui.screens.MainMenuScreen
import com.example.semestralka_vamz.ui.screens.RulesScreen
import com.example.semestralka_vamz.ui.screens.StatisticsScreen
import com.example.semestralka_vamz.viewmodel.StatisticsViewModel
import com.example.semestralka_vamz.viewmodel.StatisticsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val db = AppDatabase.getInstance(applicationContext) // реалізація Room DB
//        val dao = db.gameStatsDao()
//        val factory = StatisticsViewModelFactory(dao)

        setContent {
//            val viewModel: StatisticsViewModel = viewModel(factory = factory)
//            StatisticsScreen(viewModel = viewModel)
            GameScreen()
        }
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
////            GameScreen() // твій стартовий екран
////            RulesScreen()
//            StatisticsScreen()
////            MainMenuScreen()
//        }
//    }
}
