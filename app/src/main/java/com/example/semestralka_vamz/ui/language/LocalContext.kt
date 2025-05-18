package com.example.semestralka_vamz.ui.language

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

// Rozšírenie triedy Context – vytvorí nový Context s danou lokalizáciou
fun Context.createLocalizedContext(locale: Locale): Context {
    val config = Configuration(resources.configuration) // získa aktuálnu konfiguráciu
    config.setLocale(locale)                            // nastaví požadovanú Locale
    return createConfigurationContext(config)           // vráti nový lokalizovaný Context
}
