package com.example.semestralka_vamz.viewmodel

// Importy pre ViewModel a ViewModelProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semestralka_vamz.data.dao.GameStatsDao

// Factory trieda na vytváranie StatisticsViewModel s DAO ako závislosťou
class StatisticsViewModelFactory(
    private val dao: GameStatsDao // DAO poskytuje prístup k štatistickým dátam z databázy
) : ViewModelProvider.Factory {

    // Vytvorenie inštancie požadovaného ViewModelu
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Overenie, že požadovaný typ je StatisticsViewModel
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            // Vytvorenie a vrátenie inštancie StatisticsViewModel
            return StatisticsViewModel(dao) as T
        }
        // V prípade neznámeho typu vyhodenie výnimky
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
