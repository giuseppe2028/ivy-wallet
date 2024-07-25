package com.ivy.data.db.dao.read

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.ivy.data.db.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings LIMIT 1")
    suspend fun findFirst(): SettingsEntity

    @Query("SELECT timeZoneId FROM settings LIMIT 1")
    fun findLatestTimeZoneFlow(): Flow<String>

    @Query("SELECT * FROM settings LIMIT 1")
    suspend fun findFirstOrNull(): SettingsEntity?

    @Query("SELECT * FROM settings")
    suspend fun findAll(): List<SettingsEntity>

    @Query("SELECT * FROM settings WHERE id = :id")
    suspend fun findById(id: UUID): SettingsEntity?
}
