package com.example.semestralka_vamz.store
import android.content.Context
import com.example.semestralka_vamz.data.model.AppTheme

object ThemeStorage {
    private const val PREF_NAME = "app_settings"
    private const val KEY_THEME = "app_theme"

    fun saveTheme(context: Context, theme: AppTheme) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_THEME, theme.name)
            .apply()
    }

    fun loadTheme(context: Context): AppTheme {
        val name = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_THEME, AppTheme.Dark.name)
        return AppTheme.valueOf(name!!)
    }
}
