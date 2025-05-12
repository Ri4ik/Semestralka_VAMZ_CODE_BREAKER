package com.example.semestralka_vamz.data.model

data class GuessState(
    val guess: List<Int>,
    val exact: Int,
    val partial: Int,
    val isCorrect: Boolean
)
