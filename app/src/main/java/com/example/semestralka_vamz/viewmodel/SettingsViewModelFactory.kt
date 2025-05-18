package com.example.semestralka_vamz.viewmodel

// Importy pre ViewModel a ViewModelProvider
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Factory trieda na vytváranie inštancií SettingsViewModel s kontextom
class SettingsViewModelFactory(
    private val context: Context // Kontext aplikácie
) : ViewModelProvider.Factory {

    // Metóda, ktorá vytvorí SettingsViewModel s daným kontextom
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(context) as T
    }
}
