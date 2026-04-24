package com.example.vaccinibiologicibrunosvezia.logic

import com.example.vaccinibiologicibrunosvezia.model.PatientInput
import com.example.vaccinibiologicibrunosvezia.model.VaccineStatus
import com.example.vaccinibiologicibrunosvezia.model.Vaccine

data class VaccineCheck (

    val vaccino: Vaccine,
    val condizione: (PatientInput) -> Boolean,
    val stato: VaccineStatus,
    val reasonResId: Int

    )