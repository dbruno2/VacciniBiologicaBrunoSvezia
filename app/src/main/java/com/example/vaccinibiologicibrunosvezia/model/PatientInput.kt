package com.example.vaccinibiologicibrunosvezia.model

data class PatientInput(
    val terapiaBiologica: String,
    val eta: Int,
    val condizioni: List<String>,
    val storiaVaccinale: List<Vaccino>
)
