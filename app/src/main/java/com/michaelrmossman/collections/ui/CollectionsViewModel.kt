package com.michaelrmossman.collections.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michaelrmossman.collections.CollectionsApplication
import com.michaelrmossman.collections.data.FavouritesRepository
import com.michaelrmossman.collections.navigation.CurrentScreen

/* Used for Navigation3 app navigation */
class CollectionsViewModel(
    private val favesRepository: FavouritesRepository
) : ViewModel() {

    val backStack = mutableStateListOf<CurrentScreen>(
        CurrentScreen.MainScreen
    )

    val faveCount = favesRepository.faveCount.asLiveData()

    fun home() {
        backStack.forEach { screen ->
            backStack.removeIf { screen ->
                screen != CurrentScreen.MainScreen
            }
        }
    }

    fun pop() {
        backStack.removeLastOrNull()
    }

    fun put(currentScreen: CurrentScreen) {
        backStack.add(currentScreen)
    }

    fun removeIfNot(currentScreen: CurrentScreen) {
        backStack.removeIf {
            currentScreen != CurrentScreen.MainScreen
            &&
            currentScreen != CurrentScreen.ExplorerScreen
            &&
            currentScreen != CurrentScreen.ObjectsScreen
            &&
            currentScreen != CurrentScreen.SpecimensScreen
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CollectionsApplication)
                val favouritesRepository = application.container.favesRepository
                CollectionsViewModel(favouritesRepository)
            }
        }
        // const val TAG = "CollectionsViewModel"
    }
}