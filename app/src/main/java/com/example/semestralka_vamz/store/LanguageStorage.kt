package com.example.semestralka_vamz.store

// Import triedy Context a enum triedy AppLanguage
import android.content.Context
import com.example.semestralka_vamz.data.model.AppLanguage

// Objekt pre ukladanie a načítavanie nastaveného jazyka aplikácie pomocou SharedPreferences
object LanguageStorage {
    private const val PREF_NAME = "app_settings"     // Názov súboru s preferenciami
    private const val KEY_LANGUAGE = "app_language"  // Kľúč pre uložený jazyk

    // Uloženie jazykovej preferencie ako String (napr. "EN" alebo "SK")
    fun saveLanguage(context: Context, lang: AppLanguage) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, lang.name)
            .apply()
    }

    // Načítanie uloženého jazyka; predvolený jazyk je EN
    fun loadLanguage(context: Context): AppLanguage {
        val name = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, AppLanguage.EN.name)
        return AppLanguage.valueOf(name!!) // prevedie string späť na enum hodnotu
    }
}
