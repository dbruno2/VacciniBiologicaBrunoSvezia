package com.example.vaccinibiologicibrunosvezia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vaccinibiologicibrunosvezia.data.local.entity.RuleEntity

@Dao
interface RuleDao {

    @Query("SELECT * FROM RuleEntity")
    suspend fun getAll(): List<RuleEntity>

    @Insert
    suspend fun insert(rule: RuleEntity)
}
