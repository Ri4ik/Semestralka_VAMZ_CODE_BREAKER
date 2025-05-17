package com.example.semestralka_vamz.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.semestralka_vamz.data.model.AppLanguage
import com.example.semestralka_vamz.data.model.AppTheme
import com.example.semestralka_vamz.store.LanguageStorage
import com.example.semestralka_vamz.store.ThemeStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(private val appContext: Context) : ViewModel() {
    private val _theme = MutableStateFlow(ThemeStorage.loadTheme(appContext))
    val theme: StateFlow<AppTheme> = _theme

    private val _language = MutableStateFlow(LanguageStorage.loadLanguage(appContext))
    val language: StateFlow<AppLanguage> = _language

    fun setTheme(theme: AppTheme) {
        _theme.value = theme
        ThemeStorage.saveTheme(appContext, theme)
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
        LanguageStorage.saveLanguage(appContext, lang)
    }
}
