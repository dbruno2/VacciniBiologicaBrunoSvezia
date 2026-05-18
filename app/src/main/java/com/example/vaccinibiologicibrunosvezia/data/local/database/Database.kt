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
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    val vaccineDao = database.vaccineDao()
                    val ruleDao = database.ruleDao()

                    // --- 1. INSERIMENTO VACCINI (Dataset Esteso) ---

                    // Vaccini Inattivati (isLive = false)
                    val idInf = vaccineDao.insert(VaccineEntity(name = "Antinfluenzale", isLive = false))
                    val idPne = vaccineDao.insert(VaccineEntity(name = "Pneumococcico", isLive = false))
                    val idHpv = vaccineDao.insert(VaccineEntity(name = "HPV", isLive = false))
                    val idHepA = vaccineDao.insert(VaccineEntity(name = "Epatite A", isLive = false))
                    val idHepB = vaccineDao.insert(VaccineEntity(name = "Epatite B", isLive = false))
                    val idHz = vaccineDao.insert(VaccineEntity(name = "Herpes Zoster", isLive = false))
                    val idCovid = vaccineDao.insert(VaccineEntity(name = "COVID-19", isLive = false))
                    val idTet = vaccineDao.insert(VaccineEntity(name = "Tetano", isLive = false))
                    val idDif = vaccineDao.insert(VaccineEntity(name = "Difterite", isLive = false))
                    val idPer = vaccineDao.insert(VaccineEntity(name = "Pertosse", isLive = false))
                    val idMen = vaccineDao.insert(VaccineEntity(name = "Meningococco", isLive = false))
                    val idHib = vaccineDao.insert(VaccineEntity(name = "Hib", isLive = false))
                    val idRab = vaccineDao.insert(VaccineEntity(name = "Rabbia", isLive = false))
                    val idTif = vaccineDao.insert(VaccineEntity(name = "Tifo", isLive = false))
                    val idChol = vaccineDao.insert(VaccineEntity(name = "Colera", isLive = false))
                    val idTbe = vaccineDao.insert(VaccineEntity(name = "Encefalite da zecca", isLive = false))
                    val idPol = vaccineDao.insert(VaccineEntity(name = "Poliomielite", isLive = false))

                    // Vaccini Vivi Attenuati (isLive = true)
                    val idMpr = vaccineDao.insert(VaccineEntity(name = "MPR", isLive = true))
                    val idVar = vaccineDao.insert(VaccineEntity(name = "Varicella", isLive = true))
                    val idRot = vaccineDao.insert(VaccineEntity(name = "Rotavirus", isLive = true))
                    val idYf = vaccineDao.insert(VaccineEntity(name = "Febbre Gialla", isLive = true))


                    // --- 2. INSERIMENTO REGOLE CLINICHE (Combinazioni Realistiche) ---

                    // REGOLE PER CONDIZIONI CLINICHE

                    // BPCO o ASMA -> Antinfluenzale Raccomandato
                    ruleDao.insert(RuleEntity(vaccineId = idInf.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("BPCO"), result = RecommendationType.RACCOMANDATO))
                    ruleDao.insert(RuleEntity(vaccineId = idInf.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("ASMA"), result = RecommendationType.RACCOMANDATO))

                    // CARDIOPATIA o DIABETE -> Pneumococcico Raccomandato
                    ruleDao.insert(RuleEntity(vaccineId = idPne.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("CARDIOPATIA"), result = RecommendationType.RACCOMANDATO))
                    ruleDao.insert(RuleEntity(vaccineId = idPne.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("DIABETE"), result = RecommendationType.RACCOMANDATO))

                    // OBESITA o CARDIOPATIA -> COVID-19 Raccomandato
                    ruleDao.insert(RuleEntity(vaccineId = idCovid.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("OBESITA"), result = RecommendationType.RACCOMANDATO))
                    ruleDao.insert(RuleEntity(vaccineId = idCovid.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("CARDIOPATIA"), result = RecommendationType.RACCOMANDATO))

                    // EPATOPATIA -> Epatite A e B Raccomandati
                    ruleDao.insert(RuleEntity(vaccineId = idHepA.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("EPATOPATIA"), result = RecommendationType.RACCOMANDATO))
                    ruleDao.insert(RuleEntity(vaccineId = idHepB.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("EPATOPATIA"), result = RecommendationType.RACCOMANDATO))

                    // IMMUNODEPRESSIONE -> Meningococco e Hib Raccomandati
                    ruleDao.insert(RuleEntity(vaccineId = idMen.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("IMMUNODEPRESSIONE"), result = RecommendationType.RACCOMANDATO))
                    ruleDao.insert(RuleEntity(vaccineId = idHib.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("IMMUNODEPRESSIONE"), result = RecommendationType.RACCOMANDATO))

                    // MALATTIA RENALE -> Pneumococcico e COVID-19 Raccomandati
                    ruleDao.insert(RuleEntity(vaccineId = idPne.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("MALATTIA_RENALE"), result = RecommendationType.RACCOMANDATO))
                    ruleDao.insert(RuleEntity(vaccineId = idCovid.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("MALATTIA_RENALE"), result = RecommendationType.RACCOMANDATO))

                    // REGOLE PER ETA'

                    // Over 65 -> Pneumococcico e Herpes Zoster Raccomandati
                    ruleDao.insert(RuleEntity(vaccineId = idPne.toInt(), therapy = null, minAge = 65, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))
                    ruleDao.insert(RuleEntity(vaccineId = idHz.toInt(), therapy = null, minAge = 65, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))

                    // Under 45 + Immunodepressione -> HPV Possibile
                    ruleDao.insert(RuleEntity(vaccineId = idHpv.toInt(), therapy = null, minAge = null, maxAge = 45, requiredConditions = listOf("IMMUNODEPRESSIONE"), result = RecommendationType.POSSIBILE))

                    // Rotavirus (Solo Pediatrico < 5 anni)
                    ruleDao.insert(RuleEntity(vaccineId = idRot.toInt(), therapy = null, minAge = null, maxAge = 5, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))

                    // CONTROINDICAZIONI PER TERAPIA (Vaccini Vivi)
                    val terapieRischio = listOf("anti-TNF", "immunosoppressori", "anti-IL17", "anti-IL23")
                    val vacciniViviIds = listOf(idMpr, idVar, idRot, idYf)

                    for (vId in vacciniViviIds) {
                        for (terapia in terapieRischio) {
                            ruleDao.insert(RuleEntity(
                                vaccineId = vId.toInt(),
                                therapy = terapia,
                                minAge = null, maxAge = null,
                                requiredConditions = emptyList(),
                                result = RecommendationType.CONTROINDICATO
                            ))
                        }
                    }

                    // Febbre Gialla (Controindicato anche solo per Immunodepressione)
                    ruleDao.insert(RuleEntity(vaccineId = idYf.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("IMMUNODEPRESSIONE"), result = RecommendationType.CONTROINDICATO))

                    // REGOLE DI DEFAULT / POSSIBILI
                    val baseVaccines = listOf(idTet, idDif, idPer, idPol)
                    for (vId in baseVaccines) {
                        ruleDao.insert(RuleEntity(vaccineId = vId.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))
                    }
                    
                    // Altri vaccini (Possibili per rischi specifici)
                    ruleDao.insert(RuleEntity(vaccineId = idRab.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.POSSIBILE))
                    ruleDao.insert(RuleEntity(vaccineId = idTif.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.POSSIBILE))
                    ruleDao.insert(RuleEntity(vaccineId = idChol.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.POSSIBILE))
                    ruleDao.insert(RuleEntity(vaccineId = idTbe.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.POSSIBILE))
                }
            }
        }
    }
}
