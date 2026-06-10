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

/**
 * AppDatabase: Classe principale per l'accesso ai dati con Room.
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
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vaccini_database"
                )
                .addCallback(DatabaseInitializer()) // Inizializza i dati alla prima apertura
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * DatabaseInitializer: Popola il database con vaccini e regole cliniche predefinite.
     */
    private class DatabaseInitializer : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Lanciamo un'operazione asincrona per non bloccare il caricamento iniziale
            CoroutineScope(Dispatchers.IO).launch {
                val database = INSTANCE ?: return@launch
                val vDao = database.vaccineDao()
                val rDao = database.ruleDao()

                // 1. Inserimento dei Vaccini (Inattivati e Vivi)
                val idInf = vDao.insert(VaccineEntity(name = "Antinfluenzale", isLive = false))
                val idPne = vDao.insert(VaccineEntity(name = "Pneumococcico", isLive = false))
                val idHpv = vDao.insert(VaccineEntity(name = "HPV", isLive = false))
                val idHepA = vDao.insert(VaccineEntity(name = "Epatite A", isLive = false))
                val idHepB = vDao.insert(VaccineEntity(name = "Epatite B", isLive = false))
                val idHz = vDao.insert(VaccineEntity(name = "Herpes Zoster", isLive = false))
                val idCovid = vDao.insert(VaccineEntity(name = "COVID-19", isLive = false))
                val idTet = vDao.insert(VaccineEntity(name = "Tetano", isLive = false))
                val idDif = vDao.insert(VaccineEntity(name = "Difterite", isLive = false))
                val idPer = vDao.insert(VaccineEntity(name = "Pertosse", isLive = false))
                val idMen = vDao.insert(VaccineEntity(name = "Meningococco", isLive = false))
                val idHib = vDao.insert(VaccineEntity(name = "Hib", isLive = false))
                val idRab = vDao.insert(VaccineEntity(name = "Rabbia", isLive = false))
                val idTif = vDao.insert(VaccineEntity(name = "Tifo", isLive = false))
                val idChol = vDao.insert(VaccineEntity(name = "Colera", isLive = false))
                val idTbe = vDao.insert(VaccineEntity(name = "Encefalite da zecca", isLive = false))
                val idPol = vDao.insert(VaccineEntity(name = "Poliomielite", isLive = false))

                val idMpr = vDao.insert(VaccineEntity(name = "MPR", isLive = true))
                val idVar = vDao.insert(VaccineEntity(name = "Varicella", isLive = true))
                val idRot = vDao.insert(VaccineEntity(name = "Rotavirus", isLive = true))
                val idYf = vDao.insert(VaccineEntity(name = "Febbre Gialla", isLive = true))

                // 2. Inserimento delle Regole Cliniche (Esempi)
                rDao.insert(RuleEntity(vaccineId = idInf.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("BPCO"), result = RecommendationType.RACCOMANDATO))
                rDao.insert(RuleEntity(vaccineId = idInf.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("ASMA"), result = RecommendationType.RACCOMANDATO))
                rDao.insert(RuleEntity(vaccineId = idPne.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("CARDIOPATIA"), result = RecommendationType.RACCOMANDATO))
                rDao.insert(RuleEntity(vaccineId = idPne.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("DIABETE"), result = RecommendationType.RACCOMANDATO))
                rDao.insert(RuleEntity(vaccineId = idHepA.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("EPATOPATIA"), result = RecommendationType.RACCOMANDATO))
                rDao.insert(RuleEntity(vaccineId = idHepB.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("EPATOPATIA"), result = RecommendationType.RACCOMANDATO))
                rDao.insert(RuleEntity(vaccineId = idMen.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("IMMUNODEPRESSIONE"), result = RecommendationType.RACCOMANDATO))

                // Regole per Età
                rDao.insert(RuleEntity(vaccineId = idPne.toInt(), therapy = null, minAge = 65, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))
                rDao.insert(RuleEntity(vaccineId = idHz.toInt(), therapy = null, minAge = 65, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))
                rDao.insert(RuleEntity(vaccineId = idRot.toInt(), therapy = null, minAge = null, maxAge = 5, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))

                // Controindicazioni Vaccini Vivi + Terapie Biologiche
                val terapie = listOf("anti-TNF", "immunosoppressori", "anti-IL17", "anti-IL23")
                val vacciniVivi = listOf(idMpr, idVar, idRot, idYf)
                for (vId in vacciniVivi) {
                    for (t in terapie) {
                        rDao.insert(RuleEntity(vaccineId = vId.toInt(), therapy = t, minAge = null, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.CONTROINDICATO))
                    }
                }

                // Vaccini di base raccomandati a tutti
                val base = listOf(idTet, idDif, idPer, idPol)
                for (vId in base) {
                    rDao.insert(RuleEntity(vaccineId = vId.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))
                }
            }
        }
    }
}
