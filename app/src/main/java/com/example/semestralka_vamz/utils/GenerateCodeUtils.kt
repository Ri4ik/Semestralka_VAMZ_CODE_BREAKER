package com.example.semestralka_vamz.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
fun generateDailyCode(): List<Int> {
    val today = LocalDate.now()
    val seed = today.toEpochDay()
    val random = Random(seed)
    return List(4) { random.nextInt(0, 10) }
}
