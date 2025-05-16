package com.example.semestralka_vamz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.semestralka_vamz.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavGraph(navController)
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
