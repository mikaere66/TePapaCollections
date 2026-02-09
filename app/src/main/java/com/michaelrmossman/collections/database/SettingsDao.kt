package com.michaelrmossman.collections.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.michaelrmossman.collections.data.SettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Query("""
        SELECT setting FROM $TABLE_NAME_SETTING
        WHERE settingId = :settingId
    """)
    suspend fun getSettingById(settingId: String): Int

    @Query("""
        SELECT setting FROM $TABLE_NAME_SETTING
        WHERE settingId = :settingId
    """)
    fun getSettingByIdFlow(settingId: String): Flow<Int>

    @Insert // For new database creation
    suspend fun insertSettings(settings: List<SettingEntity>)

    @Update // For "restore app defaults"
    suspend fun updateSettings(settings: List<SettingEntity>): Int

    @Update // For single Setting update
    suspend fun updateSetting(setting: SettingEntity): Int
}