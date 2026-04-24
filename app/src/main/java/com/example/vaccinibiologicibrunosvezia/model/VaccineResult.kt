package com.example.vaccinibiologicibrunosvezia.model

data class VaccineResult (

    val vaccino: Vaccine,
    val stato: VaccineStatus,
    val reason: Int

)