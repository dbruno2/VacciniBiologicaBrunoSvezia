package com.example.vaccinibiologicibrunosvezia.model

import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity

data class Recommendation(
    val vaccine: VaccineEntity,
    val type: RecommendationType
)
