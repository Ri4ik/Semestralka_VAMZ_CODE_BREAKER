package com.example.semestralka_vamz.data.model

// Dátová trieda na prehľad štatistík – používa sa napríklad v hlavnom menu alebo v sekcii "štatistiky"
data class StatsOverview(
    val gamesPlayed: Int,
    // Celkový počet odohraných hier

    val gamesWon: Int,
    // Celkový počet vyhraných hier

    val avgAttempts: Double,
    // Priemerný počet pokusov na hru

    val avgTime: String
    // Priemerný čas trvania hry vo formáte reťazca (napr. "00:42")
)
