package com.example.semestralka_vamz.data

// Importy pre konverziu typov v Room a podporu LocalDateTime
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime

// Trieda obsahujúca konvertory pre neštandardné typy (napr. LocalDateTime), ktoré Room databáza natívne nepodporuje
class Converters {

    // Konvertuje reťazec (uložený v DB) späť na objekt LocalDateTime
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    // Konvertuje LocalDateTime na reťazec, aby ho bolo možné uložiť v Room databáze
    @TypeConverter
    fun localDateTimeToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
