package com.example.vaccinibiologicibrunosvezia.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)])
data class VaccineEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val isLive: Boolean
)
