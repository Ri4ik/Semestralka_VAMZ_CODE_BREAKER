package com.example.semestralka_vamz.viewmodel

// Importy pre ViewModel, kontext a dátové modely
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.semestralka_vamz.data.model.AppLanguage
import com.example.semestralka_vamz.data.model.AppTheme
import com.example.semestralka_vamz.store.LanguageStorage
import com.example.semestralka_vamz.store.ThemeStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel pre nastavenia aplikácie: téma a jazyk
class SettingsViewModel(private val appContext: Context) : ViewModel() {

    // Vnútorný stav pre tému aplikácie
    private val _theme = MutableStateFlow(ThemeStorage.loadTheme(appContext))
    val theme: StateFlow<AppTheme> = _theme // Verejný prístup k aktuálnej téme

    // Vnútorný stav pre jazyk aplikácie
    private val _language = MutableStateFlow(LanguageStorage.loadLanguage(appContext))
    val language: StateFlow<AppLanguage> = _language // Verejný prístup k aktuálnemu jazyku

    // Zmena témy: aktualizuje stav aj uloží do SharedPreferences
    fun setTheme(theme: AppTheme) {
        _theme.value = theme
        ThemeStorage.saveTheme(appContext, theme)
    }

    // Zmena jazyka: aktualizuje stav aj uloží do SharedPreferences
    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
        LanguageStorage.saveLanguage(appContext, lang)
    }
}
