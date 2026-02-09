package com.michaelrmossman.collections.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michaelrmossman.collections.database.COLUMN_NAME_SETTING
import com.michaelrmossman.collections.database.COLUMN_NAME_SETTING_ID
import com.michaelrmossman.collections.database.TABLE_NAME_SETTING
import com.michaelrmossman.collections.util.SETTING_COMMON_NUM_RESULTS
import com.michaelrmossman.collections.util.SETTING_COMMON_SAVE_HISTORY
import com.michaelrmossman.collections.util.SETTING_SAVE_FAVOURITES_DATE
import com.michaelrmossman.collections.util.SETTING_SORT_FAVOURITES_BY
import com.michaelrmossman.collections.util.SETTING_ZOOM_IMAGE_FULL_SCRN

@Entity(tableName = TABLE_NAME_SETTING) // 6
data class SettingEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COLUMN_NAME_SETTING_ID)
    val settingId: String,

    @ColumnInfo(name = COLUMN_NAME_SETTING)
    val setting: Int
) {

    companion object {
        fun getSettings(): List<SettingEntity> {
            val settingIds = listOf(
                SETTING_COMMON_NUM_RESULTS,
                SETTING_SAVE_FAVOURITES_DATE,
                SETTING_COMMON_SAVE_HISTORY,
                // SETTING_MAP_SATELLITE_VIEW,
                SETTING_SORT_FAVOURITES_BY,
                SETTING_ZOOM_IMAGE_FULL_SCRN
            )
            return settingIds.map { settingId ->
                SettingEntity(
                    settingId = settingId,
                    setting   = 0
                )
            }
        }
    }
}