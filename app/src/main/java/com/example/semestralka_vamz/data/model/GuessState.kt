package com.example.semestralka_vamz.data.model

// Trieda reprezentujúca stav jedného pokusu používateľa počas hry
data class GuessState(
    val guess: List<Int>,
    // Čísla zadané hráčom ako pokus (napr. [1, 4, 3, 2])

    val isCorrect: Boolean,
    // Označuje, či bol celý pokus správny (true = správny kód)

    val exactIndices: List<Int> = emptyList(),
    // Indexy, kde sú čísla úplne správne (správne číslo na správnom mieste)

    val partialIndices: List<Int> = emptyList()
    // Indexy, kde sa číslo nachádza v kóde, ale je na nesprávnom mieste
)
