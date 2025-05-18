package com.example.semestralka_vamz.data.model

// Údaje reprezentujúce jednu položku v histórii hier
data class GameHistoryEntry(
    val date: String,       // Dátum odohrania hry vo formáte reťazca
    val duration: String,   // Trvanie hry ako formátovaný reťazec (napr. "00:45")
    val attempts: Int,      // Počet pokusov v danej hre
    val isWin: Boolean      // Označuje, či bola hra vyhratá (true) alebo prehratá (false)
)
