package com.michaelrmossman.collections.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michaelrmossman.collections.CollectionsApplication
import com.michaelrmossman.collections.data.FaveEntity
import com.michaelrmossman.collections.data.FavouritesRepository
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.enum.SortFavesBy
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavesViewModel(
    private val favesRepository: FavouritesRepository
) : ViewModel() {

    fun deleteAllFavourites() {
        viewModelScope.launch {
            favesRepository.deleteAllFavourites()
        }
    }

    fun deleteFave(fave: FaveEntity) {
        viewModelScope.launch {
            favesRepository.deleteFave(fave = fave)
        }
    }

    val favesSortedBy: LiveData<Int> =
        favesRepository.favesSortedBy.asLiveData()

    val favourites: LiveData<List<FaveEntity>> =
        favesRepository.getAllFavourites().asLiveData()

    suspend fun isFavourite(
        itemId: Int,
        itemType: Media
    ) : Boolean = favesRepository.isFavourite(
        itemId = itemId,
        itemType = itemType
    )

    fun setFavesSortedBy(sortBy: SortFavesBy) {
        viewModelScope.launch {
            favesRepository.setFavesSortedBy(sortBy)
        }
    }

    suspend fun toggleFavourite(
        collection: String,
        href: String,
        itemId: Int,
        itemType: Media,
        searchType: SearchType,
        subtitle1: String,
        subtitle2: String,
        title: String,
        /* These three only for MediaType.Place */
        latitude: Double = 0.0,
        locationTitle: String? = null,
        longitude: Double = 0.0
    ) : Int {
        val result = viewModelScope.async {
            val isFavourite = favesRepository.isFavourite(
                itemId = itemId, itemType = itemType
            )
            when (isFavourite) {
                true -> favesRepository.deleteFaveByIdAndType(
                    itemId = itemId,
                    itemType = itemType
                )
                else -> favesRepository.insertFave(
                    collection = collection,
                    href = href,
                    itemId = itemId,
                    itemType = itemType,
                    latitude = latitude,
                    locationTitle = locationTitle,
                    longitude = longitude,
                    searchType = searchType,
                    subtitle1 = subtitle1,
                    subtitle2 = subtitle2,
                    title = title
                ).toInt()
            }
        }
        return result.await()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CollectionsApplication)
                val favouritesRepository = application.container.favesRepository
                FavesViewModel(favouritesRepository)
            }
        }
        const val TAG = "FavesViewModel"
    }
}