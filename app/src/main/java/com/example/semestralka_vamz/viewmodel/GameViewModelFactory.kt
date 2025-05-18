package com.example.semestralka_vamz.viewmodel

// Importy pre ViewModel, kontext a ViewModelProvider
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Vlastná továreň (factory) na vytvorenie GameViewModel s prístupom ku kontextu
class GameViewModelFactory(
    private val context: Context // Kontext aplikácie (napr. z LocalContext.current)
) : ViewModelProvider.Factory {

    // Generická metóda na vytvorenie ViewModelu
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Vytvorenie inštancie GameViewModel, odovzdávame application context
        return GameViewModel(context.applicationContext as Application) as T
    }
}
