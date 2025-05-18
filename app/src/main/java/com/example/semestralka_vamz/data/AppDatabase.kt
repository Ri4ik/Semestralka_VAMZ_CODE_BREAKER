package com.example.semestralka_vamz.data

// Importy pre Room databázu a migráciu
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.semestralka_vamz.data.dao.GameStatsDao
import com.example.semestralka_vamz.data.model.GameStatsEntity

// Definícia Room databázy s entitou GameStatsEntity a verziou 2
@Database(entities = [GameStatsEntity::class], version = 2)
@TypeConverters(Converters::class) // Podpora pre vlastné typy, napr. LocalDateTime
abstract class AppDatabase : RoomDatabase() {

    // Prístup k DAO rozhraniu
    abstract fun gameStatsDao(): GameStatsDao

    companion object {
        // INSTANCE je singleton – zabezpečuje, že databáza sa vytvorí len raz
        @Volatile private var INSTANCE: AppDatabase? = null

        // Migrácia z verzie 1 na 2 (aktuálne bez úprav schémy)
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Žiadne zmeny – migrácia pridaná pre konzistenciu so zvýšením verzie
            }
        }

        // Vytvorenie alebo získanie inštancie databázy
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "codebreaker.db" // Názov databázového súboru
                )
                    .addMigrations(MIGRATION_1_2) // Pridanie migrácie
                    .build().also { INSTANCE = it }
            }
        }
    }
}
