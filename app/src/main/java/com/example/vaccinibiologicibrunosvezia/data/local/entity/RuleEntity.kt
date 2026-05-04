package com.example.vaccinibiologicibrunosvezia.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.vaccinibiologicibrunosvezia.model.RecommendationType

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = VaccineEntity::class,
            parentColumns = ["id"],
            childColumns = ["vaccineId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val vaccineId: Int,

    val therapy: String?,
    val minAge: Int?,
    val maxAge: Int?,

    val requiredConditions: List<String>,

    val result: RecommendationType
)
