package com.example.semestralka_vamz.store

import android.content.Context
import com.example.semestralka_vamz.data.model.AppLanguage

object LanguageStorage {
    private const val PREF_NAME = "app_settings"
    private const val KEY_LANGUAGE = "app_language"

    fun saveLanguage(context: Context, lang: AppLanguage) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, lang.name)
            .apply()
    }

    fun loadLanguage(context: Context): AppLanguage {
        val name = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, AppLanguage.EN.name)
        return AppLanguage.valueOf(name!!)
    }
}