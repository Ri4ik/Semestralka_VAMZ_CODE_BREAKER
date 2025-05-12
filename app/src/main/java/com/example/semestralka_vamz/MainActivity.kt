package com.example.semestralka_vamz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.semestralka_vamz.ui.screens.GameScreen
import com.example.semestralka_vamz.ui.screens.MainMenuScreen
import com.example.semestralka_vamz.ui.screens.RulesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            GameScreen() // твій стартовий екран
            RulesScreen()
//            MainMenuScreen()
//                onNewGame = TODO(),
//                onDailyChallenge = TODO(),
//                onRules = TODO(),
//                onStats = TODO(),
//                onToggleLanguage = TODO()
//            )
        }
    }
}
