package com.example.vaccinibiologicibrunosvezia.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vaccinibiologicibrunosvezia.R
import com.example.vaccinibiologicibrunosvezia.data.local.dao.RuleDao
import com.example.vaccinibiologicibrunosvezia.data.local.dao.VaccineDao
import com.example.vaccinibiologicibrunosvezia.data.local.entity.RuleEntity
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity
import com.example.vaccinibiologicibrunosvezia.model.RecommendationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [VaccineEntity::class, RuleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vaccineDao(): VaccineDao
    abstract fun ruleDao(): RuleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vaccini_database"
                )
                    .addCallback(AppDatabaseCallback(context, scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val context: Context,
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    val vaccineDao = database.vaccineDao()
                    val ruleDao = database.ruleDao()

                    val idInf = vaccineDao.insert(VaccineEntity(name = "Antinfluenzale", isLive = false))
                    val idPne = vaccineDao.insert(VaccineEntity(name = "Pneumococcico", isLive = false))
                    val idHpv = vaccineDao.insert(VaccineEntity(name = "HPV", isLive = false))
                    val idMpr = vaccineDao.insert(VaccineEntity(name = "MPR", isLive = true))
                    val idVar = vaccineDao.insert(VaccineEntity(name = "Varicella", isLive = true))

                    val condizioni = context.resources.getStringArray(R.array.condizioni)
                    val terapie = context.resources.getStringArray(R.array.terapie)

                    // Helper per trovare le stringhe corrette (mappe per sicurezza)
                    val condMap = condizioni.associateBy { it.uppercase() }
                    val terMap = terapie.associateBy { it.lowercase() }

                    // Antinfluenzale
                    ruleDao.insert(RuleEntity(
                        vaccineId = idInf.toInt(),
                        therapy = null,
                        minAge = null,
                        maxAge = null,
                        requiredConditions = listOfNotNull(condMap["ASMA"], condMap["BPCO"], condMap["OBESITÀ"], condMap["CARDIOPATIA"], condMap["DIABETE"]),
                        result = RecommendationType.RACCOMANDATO
                    ))

                    // Pneumococcico
                    ruleDao.insert(RuleEntity(
                        vaccineId = idPne.toInt(),
                        therapy = null,
                        minAge = 65,
                        maxAge = null,
                        requiredConditions = listOfNotNull(condMap["BPCO"], condMap["CARDIOPATIA"], condMap["ASMA"], condMap["MALATTIA RENALE"], condMap["DIABETE"]),
                        result = RecommendationType.RACCOMANDATO
                    ))

                    // HPV
                    ruleDao.insert(RuleEntity(
                        vaccineId = idHpv.toInt(),
                        therapy = null,
                        minAge = null,
                        maxAge = 45,
                        requiredConditions = listOfNotNull(condMap["IMMUNODEPRESSIONE"]),
                        result = RecommendationType.POSSIBILE
                    ))

                    // MPR
                    ruleDao.insert(RuleEntity(
                        vaccineId = idMpr.toInt(),
                        therapy = terapie.find { it.contains("anti-TNF", ignoreCase = true) },
                        minAge = null,
                        maxAge = null,
                        requiredConditions = listOfNotNull(condMap["IMMUNODEPRESSIONE"]),
                        result = RecommendationType.CONTROINDICATO
                    ))

                    // Varicella
                    ruleDao.insert(RuleEntity(
                        vaccineId = idVar.toInt(),
                        therapy = terapie.find { it.contains("immunosoppressori", ignoreCase = true) },
                        minAge = null,
                        maxAge = null,
                        requiredConditions = listOfNotNull(condMap["IMMUNODEPRESSIONE"]),
                        result = RecommendationType.CONTROINDICATO
                    ))
                }
            }
        }
    }
}
