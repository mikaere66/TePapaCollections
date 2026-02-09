package com.michaelrmossman.collections.ui.variables

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michaelrmossman.collections.CollectionsApplication
import com.michaelrmossman.collections.data.HistoryRepository
import com.michaelrmossman.collections.data.SettingsRepository
import com.michaelrmossman.collections.state.SettingsUiState
import com.michaelrmossman.collections.util.DEBUG_SHOW_ADDITIONAL_MESSAGES
import com.michaelrmossman.collections.util.SETTING_COMMON_NUM_RESULTS
import com.michaelrmossman.collections.util.SETTING_COMMON_SAVE_HISTORY
import com.michaelrmossman.collections.util.SETTING_MAP_SATELLITE_VIEW
import com.michaelrmossman.collections.util.SETTING_SAVE_FAVOURITES_DATE
import com.michaelrmossman.collections.util.SETTING_ZOOM_IMAGE_FULL_SCRN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val historyRepository : HistoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _settingsUiState by lazy { MutableStateFlow(SettingsUiState()) }
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState

    /* Live Data */

    val resultsSize: LiveData<Int> = settingsRepository.resultsSize.asLiveData()

    val qtyHistory: LiveData<Int> = historyRepository.getHistoryItemsCount().asLiveData()

    val satelliteView: LiveData<Int> = settingsRepository.satelliteView.asLiveData()

    val saveFavDate: LiveData<Int> = settingsRepository.saveFavDate.asLiveData()

    val saveHistory: LiveData<Int> = settingsRepository.saveHistory.asLiveData()

    val zoomFullImage: LiveData<Int> = settingsRepository.zoomFullImage.asLiveData()

    /* Save Settings */

    fun saveSetting(settingId: String, setting: Int) {
        viewModelScope.launch {
            val result = settingsRepository.saveSetting(
                settingId = settingId,
                setting   = setting
            )
            if (DEBUG_SHOW_ADDITIONAL_MESSAGES) {
                Log.d(TAG,"$settingId: $setting ($result)")
            }
        }
    }

    fun setResultsSize(index: Int) {
        saveSetting(SETTING_COMMON_NUM_RESULTS, index)
        settingsRepository.setResultsSize(index)
    }

    fun setSatelliteView(satellite: Int) {
        saveSetting(SETTING_MAP_SATELLITE_VIEW, satellite)
        settingsRepository.setSatelliteView(satellite)
    }

    fun setSaveFavDate(saveDate: Int) {
        saveSetting(SETTING_SAVE_FAVOURITES_DATE, saveDate)
        settingsRepository.setSaveFavDate(saveDate)
    }

    fun setSaveHistory(save: Int) {
        saveSetting(SETTING_COMMON_SAVE_HISTORY, save)
        settingsRepository.setSaveHistory(save)
    }

    fun setZoomFullImage(zoomImage: Int) {
        saveSetting(SETTING_ZOOM_IMAGE_FULL_SCRN, zoomImage)
        settingsRepository.setZoomFullImage(zoomImage)
    }

    /* Restore Defaults */

    fun clearHistoryItems() {
        viewModelScope.launch {
            val deleted = historyRepository.deleteAllHistoryItems()
            _settingsUiState.update { currentState ->
                currentState.copy(
                    historyItemsDeleted = deleted
                )
            }
        }
    }

    fun resetAllSettings() {
        viewModelScope.launch {
            val result = settingsRepository.resetAllSettings()
            if (DEBUG_SHOW_ADDITIONAL_MESSAGES) {
                Log.d(TAG,"All settings ($result)")
            }
        }
    }

    fun resetHistoryState() {
        _settingsUiState.update { currentState ->
            currentState.copy(
                historyItemsDeleted = 0
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CollectionsApplication)
                val historyRepository = application.container.historyRepository
                val settingsRepository = application.container.settingsRepository
                SettingsViewModel(historyRepository, settingsRepository)
            }
        }
        private const val TAG = "SettingsViewModel"
    }
}