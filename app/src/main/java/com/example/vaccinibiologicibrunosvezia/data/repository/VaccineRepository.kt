package com.example.vaccinibiologicibrunosvezia.data.repository

import com.example.vaccinibiologicibrunosvezia.data.local.dao.RuleDao
import com.example.vaccinibiologicibrunosvezia.data.local.dao.VaccineDao

class VaccineRepository(
    private val vaccineDao: VaccineDao,
    private val ruleDao: RuleDao
) {

    suspend fun getVaccines() = vaccineDao.getAll()

    suspend fun getRules() = ruleDao.getAll()
}
