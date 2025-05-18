package com.example.semestralka_vamz.store

// Importy: Context pre prístup k SharedPreferences a enum trieda AppTheme
import android.content.Context
import com.example.semestralka_vamz.data.model.AppTheme

// Objekt na ukladanie a načítanie preferovanej témy aplikácie
object ThemeStorage {
    private const val PREF_NAME = "app_settings"      // Názov súboru s preferenciami
    private const val KEY_THEME = "app_theme"         // Kľúč pre uloženú tému

    // Uloženie zvolenej témy ako reťazec (napr. "Light" alebo "Dark")
    fun saveTheme(context: Context, theme: AppTheme) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_THEME, theme.name)
            .apply()
    }

    // Načítanie uloženej témy; predvolená téma je Dark
    fun loadTheme(context: Context): AppTheme {
        val name = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_THEME, AppTheme.Dark.name)
        return AppTheme.valueOf(name!!) // Konverzia späť na enum hodnotu
    }
}
