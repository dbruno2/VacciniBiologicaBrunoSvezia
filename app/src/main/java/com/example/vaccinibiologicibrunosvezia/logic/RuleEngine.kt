package com.example.vaccinibiologicibrunosvezia.logic

import com.example.vaccinibiologicibrunosvezia.model.PatientInput
import com.example.vaccinibiologicibrunosvezia.model.VaccinoiRisultato
import com.example.vaccinibiologicibrunosvezia.model.Vaccino

class RuleEngine(
    private val gestoreRegole: Regole
) {

    fun valuta(input: PatientInput): List<VaccinoiRisultato> {

        val risultati = mutableListOf<VaccinoiRisultato>()

        for (regola in gestoreRegole.ottieniRegole()) {

            if (regola.condizione(input)) {

                risultati.add(
                    VaccinoiRisultato(
                        vaccino = regola.vaccino,
                        stato = regola.stato
                        //aggiungi la reason
                    )
                )
            }
        }

        return risultati
    }
}