package com.example.vaccinibiologicibrunosvezia.model

data class VaccinoiRisultato (

    val vaccino: Vaccino,
    val stato: StatusVaccino,
    val reason: String? =null

)