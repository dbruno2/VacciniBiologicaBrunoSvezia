package com.example.vaccinibiologicibrunosvezia.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vaccinibiologicibrunosvezia.data.local.dao.RuleDao
import com.example.vaccinibiologicibrunosvezia.data.local.dao.VaccineDao
import com.example.vaccinibiologicibrunosvezia.data.local.entity.RuleEntity
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity
import com.example.vaccinibiologicibrunosvezia.model.RecommendationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 1. Definizione del database con le entità e i convertitori
@Database(entities = [VaccineEntity::class, RuleEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vaccineDao(): VaccineDao
    abstract fun ruleDao(): RuleDao

    companion object {
        // Singleton semplice per evitare aperture multiple del database
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vaccini_database"
                )
                    // 2. Aggiunta della callback per il popolamento iniziale
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // 3. Classe interna per gestire la creazione del database
    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Lanciamo una coroutine per l'inserimento asincrono dei dati
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    val vaccineDao = database.vaccineDao()
                    val ruleDao = database.ruleDao()

                    // 4. Inserimento dei VACCINI e recupero dei loro ID
                    val idInf = vaccineDao.insert(VaccineEntity(name = "Antinfluenzale", isLive = false))
                    val idPne = vaccineDao.insert(VaccineEntity(name = "Pneumococcico", isLive = false))
                    val idHpv = vaccineDao.insert(VaccineEntity(name = "HPV", isLive = false))
                    val idMpr = vaccineDao.insert(VaccineEntity(name = "MPR", isLive = true))
                    val idVar = vaccineDao.insert(VaccineEntity(name = "Varicella", isLive = true))

                    // 5. Inserimento delle REGOLE associate ai vaccini tramite ID

                    ruleDao.insert(
                        RuleEntity(
                            vaccineId = idInf.toInt(),
                            therapy = null,
                            minAge = null,
                            maxAge = null,
                            requiredConditions = listOf(
                                "ASMA",
                                "BPCO",
                                "OBESITA",
                                "CARDIOPATIA",
                                "DIABETE"
                            ),
                            result = RecommendationType.RACCOMANDATO
                        )
                    )

                    // -----------------------------------
                    // PNEUMOCOCCICO
                    // Raccomandato:
                    // - età >= 65
                    // - patologie respiratorie/cardiache
                    // -----------------------------------

                    ruleDao.insert(
                        RuleEntity(
                            vaccineId = idPne.toInt(),
                            therapy = null,
                            minAge = 65,
                            maxAge = null,
                            requiredConditions = listOf(
                                "BPCO",
                                "CARDIOPATIA",
                                "ASMA",
                                "MALATTIA_RENALE",
                                "DIABETE"
                            ),
                            result = RecommendationType.RACCOMANDATO
                        )
                    )

                    // -----------------------------------
                    // HPV
                    // Possibile in pazienti giovani
                    // e immunodepressi
                    // -----------------------------------

                    ruleDao.insert(
                        RuleEntity(
                            vaccineId = idHpv.toInt(),
                            therapy = null,
                            minAge = null,
                            maxAge = 45,
                            requiredConditions = listOf(
                                "IMMUNODEPRESSIONE"
                            ),
                            result = RecommendationType.POSSIBILE
                        )
                    )

                    // -----------------------------------
                    // MPR
                    // Controindicato:
                    // - vaccino vivo
                    // - terapia anti-TNF
                    // - immunodepressione
                    // -----------------------------------

                    ruleDao.insert(
                        RuleEntity(
                            vaccineId = idMpr.toInt(),
                            therapy = "anti-TNF",
                            minAge = null,
                            maxAge = null,
                            requiredConditions = listOf(
                                "IMMUNODEPRESSIONE"
                            ),
                            result = RecommendationType.CONTROINDICATO
                        )
                    )

                    // -----------------------------------
                    // VARICELLA
                    // Controindicato:
                    // - immunosoppressori
                    // - immunodepressione
                    // -----------------------------------

                    ruleDao.insert(
                        RuleEntity(
                            vaccineId = idVar.toInt(),
                            therapy = "immunosoppressori",
                            minAge = null,
                            maxAge = null,
                            requiredConditions = listOf(
                                "IMMUNODEPRESSIONE"
                            ),
                            result = RecommendationType.CONTROINDICATO
                        ))
                   //condizioni per seba
                    //"DIABETE"
                    //"BPCO"
                    //"CARDIOPATIA"
                    //"IMMUNODEPRESSIONE"
                    //"MALATTIA_RENALE"
                    //"OBESITA"
                    //"ASMA"
                    //"EPATOPATIA"
                }
            }
        }
    }
}
