package com.example.semestralka_vamz.data

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun localDateTimeToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
