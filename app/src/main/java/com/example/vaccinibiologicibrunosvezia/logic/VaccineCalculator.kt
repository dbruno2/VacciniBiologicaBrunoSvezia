package com.example.vaccinibiologicibrunosvezia.logic

import com.example.vaccinibiologicibrunosvezia.model.PatientInput
import com.example.vaccinibiologicibrunosvezia.model.VaccineResult

class VaccineCalculator(
    private val gestoreVaccineGuidelines: VaccineGuidelines
) {

    fun valuta(input: PatientInput): List<VaccineResult> {

        val risultati = mutableListOf<VaccineResult>()

        for (regola in gestoreVaccineGuidelines.ottieniVaccini()) {

            if (regola.condizione(input)) {

                risultati.add(
                    VaccineResult(
                        vaccino = regola.vaccino,
                        stato= regola.stato,
                        reason = regola.reasonResId
                    )
                )
            }
        }

        return risultati
    }
}