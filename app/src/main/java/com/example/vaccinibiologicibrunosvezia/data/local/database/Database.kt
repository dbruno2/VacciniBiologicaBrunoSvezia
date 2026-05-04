package com.example.vaccinibiologicibrunosvezia.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vaccinibiologicibrunosvezia.data.local.dao.RuleDao
import com.example.vaccinibiologicibrunosvezia.data.local.dao.VaccineDao
import com.example.vaccinibiologicibrunosvezia.data.local.entity.RuleEntity
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity

@Database(
    entities = [VaccineEntity::class, RuleEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vaccineDao(): VaccineDao
    abstract fun ruleDao(): RuleDao
}
