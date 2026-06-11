package com.example.vaccinibiologicibrunosvezia.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vaccinibiologicibrunosvezia.data.local.dao.RuleDao
import com.example.vaccinibiologicibrunosvezia.data.local.dao.VaccineDao
import com.example.vaccinibiologicibrunosvezia.data.local.entity.RuleEntity
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity

/**
 * AppDatabase: Classe principale per l'accesso ai dati con Room.
 * Gestisce la persistenza in modo minimale, senza logica di popolamento interna.
 */
@Database(entities = [VaccineEntity::class, RuleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vaccineDao(): VaccineDao
    abstract fun ruleDao(): RuleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * getDatabase: Pattern Singleton per garantire un'unica istanza del DB.
         */
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vaccini_database"
                ).build()
            }
            return INSTANCE!!
        }
    }
}
