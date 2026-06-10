package com.example.vaccinibiologicibrunosvezia.data.repository

import com.example.vaccinibiologicibrunosvezia.data.local.dao.RuleDao
import com.example.vaccinibiologicibrunosvezia.data.local.dao.VaccineDao
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity
import kotlinx.coroutines.flow.Flow

/**
 * VaccineRepository: Gestisce l'accesso ai dati, fungendo da intermediario tra il Database e il ViewModel.
 */
class VaccineRepository(
    private val vaccineDao: VaccineDao,
    private val ruleDao: RuleDao
) {

    // Restituisce un Flow di vaccini (si aggiorna automaticamente se i dati cambiano)
    fun getVaccines(): Flow<List<VaccineEntity>> = vaccineDao.getAll()

    // Recupera la lista delle regole cliniche
    suspend fun getRules() = ruleDao.getAll()
}
