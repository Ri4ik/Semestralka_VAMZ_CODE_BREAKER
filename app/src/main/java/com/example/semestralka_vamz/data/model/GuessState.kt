package com.example.semestralka_vamz.data.model

data class GuessState(
    val guess: List<Int>,
    val isCorrect: Boolean,
    val exactIndices: List<Int> = emptyList(),
    val partialIndices: List<Int> = emptyList()
)

