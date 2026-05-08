package com.example.vaccinibiologicibrunosvezia.data.repository

import androidx.lifecycle.ViewModel
import com.example.vaccinibiologicibrunosvezia.model.PatientInput
import com.example.vaccinibiologicibrunosvezia.model.Recommendation
import com.example.vaccinibiologicibrunosvezia.model.RecommendationType
import kotlinx.coroutines.runBlocking

class VaccineViewModel(
    private val repository: VaccineRepository
) : ViewModel() {

    fun calculateRecommendations(input: PatientInput): List<Recommendation> {

        val rules = runBlocking { repository.getRules() }
        val vaccines = runBlocking { repository.getVaccines() }

        val result = mutableListOf<Recommendation>()

        for (rule in rules) {

            val therapyOk =
                rule.therapy == null || rule.therapy == input.terapiaBiologica

            val ageOk =
                (rule.minAge == null || input.eta >= rule.minAge) &&
                        (rule.maxAge == null || input.eta <= rule.maxAge)

            val conditionsOk =
                rule.requiredConditions.isEmpty() ||
                        rule.requiredConditions.all { it in input.condizioni }

            if (therapyOk && ageOk && conditionsOk) {

                val vaccine = vaccines.find { it.id == rule.vaccineId }

                if (vaccine != null) {

                    // 🔥 NUOVA LOGICA: già vaccinato
                    val alreadyDone = vaccine.id in input.vacciniEffettuati

                    val finalType =
                        if (alreadyDone) {
                            RecommendationType.POSSIBILE // oppure “GIÀ FATTO”
                        } else {
                            rule.result
                        }

                    result.add(
                        Recommendation(
                            vaccineName = vaccine.name,
                            type = finalType
                        )
                    )
                }
            }
        }

        return result
    }
}