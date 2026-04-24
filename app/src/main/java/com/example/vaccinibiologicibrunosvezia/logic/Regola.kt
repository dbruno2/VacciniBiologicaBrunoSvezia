package com.example.vaccinibiologicibrunosvezia.logic

import com.example.vaccinibiologicibrunosvezia.model.PatientInput
import com.example.vaccinibiologicibrunosvezia.model.StatusVaccino
import com.example.vaccinibiologicibrunosvezia.model.Vaccino

data class Regola (

    val vaccino: Vaccino,
    val condizione: (PatientInput) -> Boolean,
    val stato: StatusVaccino,
    val ragioni: String

    )