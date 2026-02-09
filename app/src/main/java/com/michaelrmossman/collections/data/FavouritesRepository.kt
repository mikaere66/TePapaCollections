package com.michaelrmossman.collections.data

import com.michaelrmossman.collections.database.FavesDao
import com.michaelrmossman.collections.database.SettingsDao
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.enum.SortFavesBy
import com.michaelrmossman.collections.util.SETTING_SAVE_FAVOURITES_DATE
import com.michaelrmossman.collections.util.SETTING_SORT_FAVOURITES_BY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FavouritesRepository(
    private val favouritesDao: FavesDao,
    private val settingsDao: SettingsDao
) {

    suspend fun deleteAllFavourites(): Int =
        favouritesDao.deleteAllFavourites()

    suspend fun deleteFave(fave: FaveEntity): Int =
        favouritesDao.deleteFave(fave = fave)

    suspend fun deleteFaveByIdAndType(
        itemId: Int, itemType: Media
    ) : Int = favouritesDao.deleteFaveByIdAndType(
        itemId = itemId, itemType = itemType.toString()
    )

    val faveCount: Flow<Int> = favouritesDao.getFaveCount()

    private val _favesSortedBy = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_SORT_FAVOURITES_BY
        )
    )
    val favesSortedBy: Flow<Int>
        get() = _favesSortedBy.value

    private val _favesTimestamp = MutableStateFlow(
        settingsDao.getSettingByIdFlow(
            settingId = SETTING_SAVE_FAVOURITES_DATE
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllFavourites(): Flow<List<FaveEntity>> =
        _favesSortedBy.flatMapLatest { sortedBy ->
            _favesTimestamp.flatMapLatest { timed ->

                val saveDateTime = timed.first()
                val sortFavesBy = SortFavesBy.entries[sortedBy.first()]

                favouritesDao.getFavesFlow().map { faves ->

                    faves.sortedBy { fave ->

                        when (sortFavesBy) {
                            SortFavesBy.Date -> when (saveDateTime) {
                                1 -> fave.added.toString()
                                else -> fave.id.toString()
                            }
                            SortFavesBy.Name -> fave.title
                            SortFavesBy.Type -> fave.media
                        }
                    }
                }
            }
        }

    suspend fun isFavourite(
        itemId: Int, itemType: Media
    ) : Boolean = favouritesDao.isFavourite(
        itemId = itemId, itemType = itemType.toString()
    )

//    suspend fun insertFave(fave: FaveEntity): Long =
//        favouritesDao.insertFave(fave = fave)

    suspend fun insertFave(
        collection: String,
        href: String,
        itemId: Int,
        itemType: Media,
        latitude: Double,
        locationTitle: String?,
        longitude: Double,
        searchType: SearchType,
        subtitle1: String,
        subtitle2: String,
        title: String,
        /* Only for use in HrefSingleViewModel */
        isFave: Boolean = true
    ) : Long {
        val saveDate = settingsDao.getSettingById(
            settingId = SETTING_SAVE_FAVOURITES_DATE
        )
        val faveEntity = FaveEntity(
            id = 0,
            added = when (saveDate == 1) {
                true -> System.currentTimeMillis()
                else -> 0L
            },
            collection = collection,
            href = href,
            isFave = isFave,
            itemId = itemId,
            latitude = latitude,
            locationTitle = locationTitle,
            longitude = longitude,
            media = itemType.toString(),
            searchType = searchType.toString(),
            subtitle1 = subtitle1,
            subtitle2 = subtitle2,
            title = title
        )
        return favouritesDao.insertFave(faveEntity)
    }

    suspend fun setFavesSortedBy(sortBy: SortFavesBy) {
        val settingEntity = SettingEntity(
            settingId = SETTING_SORT_FAVOURITES_BY,
            setting = sortBy.ordinal
        )
        if (settingsDao.updateSetting(settingEntity) > 0) {
            _favesSortedBy.value = flowOf(sortBy.ordinal)
        }
    }
}