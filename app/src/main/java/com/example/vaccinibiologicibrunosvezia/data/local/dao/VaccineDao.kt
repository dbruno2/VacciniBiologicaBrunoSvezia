package com.example.vaccinibiologicibrunosvezia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccineDao {

    @Query("SELECT * FROM VaccineEntity")
    fun getAll(): Flow<List<VaccineEntity>>

    @Insert
    suspend fun insert(vaccine: VaccineEntity): Long

    @Query("SELECT * FROM VaccineEntity WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): VaccineEntity?
}
