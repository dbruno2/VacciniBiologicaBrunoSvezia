package com.example.vaccinibiologicibrunosvezia.data.local.database

import com.example.vaccinibiologicibrunosvezia.data.local.entity.RuleEntity
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity
import com.example.vaccinibiologicibrunosvezia.model.RecommendationType

object DatabaseSeeder {

    suspend fun seed(db: AppDatabase) {
        val vDao = db.vaccineDao()
        val rDao = db.ruleDao()

        // Verifichiamo se il database è già popolato controllando l'esistenza di un vaccino.
        if (vDao.getByName("vaccine_antinfluenzale") != null) return

        // Inserimento vaccini
        val idInf = vDao.insert(VaccineEntity(name = "vaccine_antinfluenzale", isLive = false))
        val idPne = vDao.insert(VaccineEntity(name = "vaccine_pneumococcico", isLive = false))
        val idHepA = vDao.insert(VaccineEntity(name = "vaccine_epatite_a", isLive = false))
        val idHepB = vDao.insert(VaccineEntity(name = "vaccine_epatite_b", isLive = false))
        val idHz = vDao.insert(VaccineEntity(name = "vaccine_herpes_zoster", isLive = false))
        val idTet = vDao.insert(VaccineEntity(name = "vaccine_tetano", isLive = false))
        val idDif = vDao.insert(VaccineEntity(name = "vaccine_difterite", isLive = false))
        val idPer = vDao.insert(VaccineEntity(name = "vaccine_pertosse", isLive = false))
        val idMen = vDao.insert(VaccineEntity(name = "vaccine_meningococco", isLive = false))
        val idPol = vDao.insert(VaccineEntity(name = "vaccine_polio", isLive = false))

        val idMpr = vDao.insert(VaccineEntity(name = "vaccine_mpr", isLive = true))
        val idVar = vDao.insert(VaccineEntity(name = "vaccine_varicella", isLive = true))
        val idRot = vDao.insert(VaccineEntity(name = "vaccine_rotavirus", isLive = true))
        val idYf = vDao.insert(VaccineEntity(name = "vaccine_febbre_gialla", isLive = true))

        // Inserimento regole cliniche
        rDao.insert(RuleEntity(vaccineId = idInf.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("BPCO"), result = RecommendationType.RACCOMANDATO))
        rDao.insert(RuleEntity(vaccineId = idInf.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("ASMA"), result = RecommendationType.RACCOMANDATO))
        rDao.insert(RuleEntity(vaccineId = idPne.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("CARDIOPATIA"), result = RecommendationType.RACCOMANDATO))
        rDao.insert(RuleEntity(vaccineId = idPne.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("DIABETE"), result = RecommendationType.RACCOMANDATO))
        rDao.insert(RuleEntity(vaccineId = idHepA.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("EPATOPATIA"), result = RecommendationType.RACCOMANDATO))
        rDao.insert(RuleEntity(vaccineId = idHepB.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("EPATOPATIA"), result = RecommendationType.RACCOMANDATO))
        rDao.insert(RuleEntity(vaccineId = idMen.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = listOf("IMMUNODEPRESSIONE"), result = RecommendationType.RACCOMANDATO))

        // Regole età
        rDao.insert(RuleEntity(vaccineId = idPne.toInt(), therapy = null, minAge = 65, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))
        rDao.insert(RuleEntity(vaccineId = idHz.toInt(), therapy = null, minAge = 65, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))
        rDao.insert(RuleEntity(vaccineId = idRot.toInt(), therapy = null, minAge = null, maxAge = 5, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))

        // Controindicazioni Vaccini Vivi e Terapie Biologiche
        val terapie = listOf("anti-TNF", "immunosoppressori", "anti-IL17", "anti-IL23")
        val vacciniVivi = listOf(idMpr, idVar, idRot, idYf)
        for (vId in vacciniVivi) {
            for (t in terapie) {
                rDao.insert(RuleEntity(vaccineId = vId.toInt(), therapy = t, minAge = null, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.CONTROINDICATO))
            }
        }

        // Vaccini raccomandati a tutti
        val base = listOf(idTet, idDif, idPer, idPol)
        for (vId in base) {
            rDao.insert(RuleEntity(vaccineId = vId.toInt(), therapy = null, minAge = null, maxAge = null, requiredConditions = emptyList(), result = RecommendationType.RACCOMANDATO))
        }
    }
}
