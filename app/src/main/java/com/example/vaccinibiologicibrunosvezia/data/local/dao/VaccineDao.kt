package com.example.vaccinibiologicibrunosvezia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity

@Dao
interface VaccineDao {

    @Query("SELECT * FROM VaccineEntity")
    suspend fun getAll(): List<VaccineEntity>

    @Insert
    suspend fun insert(vaccine: VaccineEntity)
}
