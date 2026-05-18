package com.example.vaccinibiologicibrunosvezia.data.repository

import com.example.vaccinibiologicibrunosvezia.data.local.dao.RuleDao
import com.example.vaccinibiologicibrunosvezia.data.local.dao.VaccineDao
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity
import kotlinx.coroutines.flow.Flow

class VaccineRepository(
    private val vaccineDao: VaccineDao,
    private val ruleDao: RuleDao
) {

    fun getVaccines(): Flow<List<VaccineEntity>> = vaccineDao.getAll()

    suspend fun getRules() = ruleDao.getAll()
}
