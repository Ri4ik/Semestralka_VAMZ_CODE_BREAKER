package com.example.semestralka_vamz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.semestralka_vamz.data.dao.GameStatsDao

class StatisticsViewModelFactory(
    private val dao: GameStatsDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            return StatisticsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
