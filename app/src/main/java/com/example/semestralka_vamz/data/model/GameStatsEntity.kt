package com.example.semestralka_vamz.data.model

// Importy potrebné pre Room databázu a LocalDateTime
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

// Táto trieda predstavuje entitu (tabuľku) v Room databáze s názvom "game_stats"
@Entity(tableName = "game_stats")
data class GameStatsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Primárny kľúč s automatickým generovaním ID

    val date: LocalDateTime,
    // Dátum a čas, kedy bola hra dokončená

    val durationSeconds: Double,
    // Trvanie hry v sekundách (môže obsahovať desatinnú časť)

    val attempts: Int,
    // Počet pokusov vykonaných počas hry

    val isWin: Boolean
    // Výsledok hry: true = výhra, false = prehra
)
