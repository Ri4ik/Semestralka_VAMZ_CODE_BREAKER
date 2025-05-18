package com.example.semestralka_vamz.ui.theme

// Importy pre tému, rozloženie a kompozíciu
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.semestralka_vamz.data.model.AppTheme

// Definícia základných farebných schém pre svetlú a tmavú tému
private val DarkColorScheme = darkColorScheme()
private val LightColorScheme = lightColorScheme()

@Composable
fun AppThemeWrapper(
    theme: AppTheme,                   // Aktuálne vybraná téma (tmavá alebo svetlá)
    content: @Composable () -> Unit   // Obsah aplikácie, ktorý sa má vykresliť s danou témou
) {
    // Výber farebnej schémy podľa zvolenej témy
    val colorScheme = when (theme) {
        AppTheme.Dark -> DarkColorScheme
        AppTheme.Light -> LightColorScheme
    }

    // Nastavenie témy aplikácie cez MaterialTheme
    MaterialTheme(
        colorScheme = colorScheme,           // Použitá farebná schéma
        typography = Typography(),           // Použité typografické štýly (default)
        content = {
            // Všetok obsah bude vykreslený na ploche Surface s pozadím z témy
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                content() // vykreslenie obsahu aplikácie
            }
        }
    )
}
