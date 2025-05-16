package com.example.semestralka_vamz.viewmodel

import androidx.lifecycle.ViewModel
import com.example.semestralka_vamz.ui.screens.AppLanguage
import com.example.semestralka_vamz.ui.screens.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {
    private val _theme = MutableStateFlow(AppTheme.Dark)
    val theme: StateFlow<AppTheme> = _theme

    private val _language = MutableStateFlow(AppLanguage.EN)
    val language: StateFlow<AppLanguage> = _language

    fun setTheme(theme: AppTheme) {
        _theme.value = theme
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }
}
