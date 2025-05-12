package com.example.semestralka_vamz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.semestralka_vamz.ui.MainMenuScreen
import ui.GameScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            GameScreen() // твій стартовий екран

            MainMenuScreen()
//                onNewGame = TODO(),
//                onDailyChallenge = TODO(),
//                onRules = TODO(),
//                onStats = TODO(),
//                onToggleLanguage = TODO()
//            )
        }
    }
}
