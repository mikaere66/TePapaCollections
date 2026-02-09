package com.michaelrmossman.collections.data

import com.michaelrmossman.collections.database.SettingsDao
import com.michaelrmossman.collections.util.SETTING_COMMON_NUM_RESULTS
import com.michaelrmossman.collections.util.SETTING_COMMON_SAVE_HISTORY
import com.michaelrmossman.collections.util.SETTING_MAP_SATELLITE_VIEW
import com.michaelrmossman.collections.util.SETTING_SAVE_FAVOURITES_DATE
import com.michaelrmossman.collections.util.SETTING_ZOOM_IMAGE_FULL_SCRN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class SettingsRepository(
    private val settingsDao: SettingsDao
) {

    suspend fun resetAllSettings(): Int {
        val settings = SettingEntity.getSettings()
        val result = settingsDao.updateSettings(settings)

        setResultsSize(index = 0)
        setSaveHistory(save = 0)

        return result
    }

    private val _resultsSize = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_COMMON_NUM_RESULTS
        )
    )
    val resultsSize: Flow<Int>
        get() = _resultsSize.value
    fun setResultsSize(index: Int) {
        _resultsSize.value = flowOf(index)
    }

    private val _satelliteView = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_MAP_SATELLITE_VIEW
        )
    )
    val satelliteView: Flow<Int>
        get() = _satelliteView.value
    fun setSatelliteView(satellite: Int) {
        _satelliteView.value = flowOf(satellite)
    }

    private val _saveFavDate = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_SAVE_FAVOURITES_DATE
        )
    )
    val saveFavDate: Flow<Int>
        get() = _saveFavDate.value
    fun setSaveFavDate(saveDate: Int) {
        _saveFavDate.value = flowOf(saveDate)
    }

    private val _saveHistory = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_COMMON_SAVE_HISTORY
        )
    )
    val saveHistory: Flow<Int>
        get() = _saveHistory.value
    fun setSaveHistory(save: Int) {
        _saveHistory.value = flowOf(save)
    }

    suspend fun saveSetting(
        settingId: String, setting: Int
    ) : Int {
        val settingEntity = SettingEntity(
            settingId = settingId,
            setting = setting
        )
        return settingsDao.updateSetting(settingEntity)
    }

    private val _zoomFullImage = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_ZOOM_IMAGE_FULL_SCRN
        )
    )
    val zoomFullImage: Flow<Int>
        get() = _zoomFullImage.value
    fun setZoomFullImage(zoomImage: Int) {
        _zoomFullImage.value = flowOf(zoomImage)
    }
}