package com.michaelrmossman.collections.ui.maps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.maps.model.LatLng
import com.michaelrmossman.collections.CollectionsApplication
import com.michaelrmossman.collections.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

/* Common to ALL five map screens, including favourites */
class MapsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val satelliteView = settingsRepository.satelliteView.asLiveData()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CollectionsApplication)
                MapsViewModel(application.container.settingsRepository)
            }
        }
        const val TAG = "MapsViewModel"
    }
}