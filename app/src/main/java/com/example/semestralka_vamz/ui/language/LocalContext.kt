package com.example.semestralka_vamz.ui.language

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun Context.createLocalizedContext(locale: Locale): Context {
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}
