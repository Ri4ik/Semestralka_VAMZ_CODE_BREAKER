package com.example.semestralka_vamz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.semestralka_vamz.data.dao.GameResultDao
import com.example.semestralka_vamz.data.dao.GameStatsDao
import com.example.semestralka_vamz.data.model.GameResult
import com.example.semestralka_vamz.data.model.GameStatsEntity

@Database(entities = [GameResult::class, GameStatsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameStatsDao(): GameStatsDao
    abstract fun gameResultDao(): GameResultDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "codebreaker.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
