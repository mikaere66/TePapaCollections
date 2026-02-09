package com.michaelrmossman.collections.ui

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michaelrmossman.collections.CollectionsApplication
import com.michaelrmossman.collections.CollectionsApplication.Companion.instance
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.data.HistoryRepository
import com.michaelrmossman.collections.data.NetworkRepository
import com.michaelrmossman.collections.data.SettingsRepository
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.network.SearchResponse
import com.michaelrmossman.collections.state.ResultsListState
import com.michaelrmossman.collections.state.ResultsListState.Companion.START_FROM
import com.michaelrmossman.collections.state.ResultsUiState
import com.michaelrmossman.collections.util.DEBUG_SHOW_ADDITIONAL_MESSAGES
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class SharedViewModel(
    // private val favesRepository: FavouritesRepository,
    private val historyRepository : HistoryRepository,
    private val networkRepository : NetworkRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel(), DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        /* Perform actions when associated UI component resumes.
           Refer to *ListScreen[s] for actual lifecycleOwner. In
           this case, either populate, or tear down, history */
        setHistory(media = resultsListState.value.searchMedia)
    }

    fun cancelDownloadState() {
        if (
            resultsListState.value.resultState
            ==
            ResultsUiState.Downloading
        ) {
            _resultsListState.update { currentState ->
                currentState.copy(
                    resultState = ResultsUiState.Init,
                    searchQuery = String()
                )
            }
        }
    }

    private fun getResultsSize(numSetting: Int): Int {
        val resultsArray = instance.resources.getIntArray(
            R.array.settings_num_results
        )
        return resultsArray[numSetting]
    }

//    suspend fun isFavourite(
//        itemId: Int,
//        itemType: Media
//    ) : Boolean = favesRepository.isFavourite(
//        itemId = itemId,
//        itemType = itemType
//    )

    /* Called from [CommonQueryScreen] on changing media,
       or upon removing filter from [EmptySearchScreen],
       however if NO actual results are in play, then the
       entire "when (media)" block is largely superfluous */
    fun manageSearch(media: Media?) {
        // Log.d("HEY","'$media'")

        when (media) {
            null -> _resultsListState.update { currentState ->
                currentState.copy(
                    /* Clear out temp list to save memory */
                    interimList = emptyList(),
                    listInterim = false,
                    /* Restore temp list to the full list */
                    resultsList = currentState.interimList,
                    searchMedia = null
                )
            }

            /* Filter the "full" list, to show JUST selected media */
            else -> {
                val isInterim: Boolean
                val workingList: List<SearchResult>
                with (resultsListState.value) {
                    isInterim = (
                        interimList.isNotEmpty()
                        || /* Only true if results exist */
                        resultsList.isNotEmpty()
                    )
                    workingList = when (listInterim) {
                        /* Filter temp list if jumping
                           from one type to another */
                        true -> interimList.filter { result ->
                            result.media.toString() == media.toString()
                        }
                        /* else, filter "full" list */
                        else -> resultsList.filter { result ->
                            result.media.toString() == media.toString()
                        }
                    }
                }
                _resultsListState.update { currentState ->
                    currentState.copy(
                        interimList = when (currentState.listInterim) {
                            /* Save full list as a temporary
                               list (note false is first) */
                            false -> currentState.resultsList
                            /* If already filtered, maintain
                               our backup of "full" list */
                            else -> currentState.interimList
                        },
                        listInterim = isInterim,
                        resultsList = workingList,
                        resultState = when (workingList.isNotEmpty()) {
                            true -> ResultsUiState.Success
                            else -> when (isInterim) {
                                true -> ResultsUiState.EmptyFilter
                                else -> ResultsUiState.Init
                            }
                        },
                        searchMedia = media
                    )
                }
            }
        }

        if (DEBUG_SHOW_ADDITIONAL_MESSAGES) {
            Log.d(
                TAG,
                (media?.toString() ?: "null")
                + "," +
                resultsListState.value.interimList.size.toString()
                + "," +
                resultsListState.value.resultsList.size.toString()
                + "," +
                resultsListState.value.listInterim.toString()
                + "," +
                resultsListState.value.resultState.toString()
            )
        }

        /* Auto complete suggestions, based on selection */
        setHistory(media)
    }

    fun resetListState(media: Media? = null) {
        _resultsListState.update { currentState ->
            currentState.copy(
                canDownload = false,
                historyList = emptyList(),
                interimFrom = 0,
                interimList = emptyList(),
                listInterim = false,
                queryOption = 0,
                resultCount = 0,
                resultsList = emptyList(),
                resultState = ResultsUiState.Init,
                searchMedia = media,
                searchQuery = String()
            )
        }
    }

    private val _resultsListState by lazy {
        MutableStateFlow(ResultsListState())
    }
    val resultsListState: StateFlow<ResultsListState>
        get() = _resultsListState

    @Suppress("KotlinConstantConditions") /* START_FROM */
    fun searchAll(
        media: Media?,
        searchType: SearchType,
        searchQuery: String,
        startFrom: Int = START_FROM,
        /* Must come AFTER startFrom */
        fromManageSearch: Boolean = false
    ) {
        viewModelScope.launch {
            val query = searchQuery.trim()
            val sizeSetting = settingsRepository.resultsSize.first()
            /* Refer to companion object, at End of File */
            val resultsSize = getResultsSize(sizeSetting)

            if (
                (resultsListState.value.resultCount
                == /* i.e. zero, a new search */
                startFrom)
                ||
                START_FROM != 0 /* Debug json */
            ) {
                _resultsListState.update { currentState ->
                    currentState.copy(
                        resultState = ResultsUiState.Downloading,
                        searchMedia = media,
                        searchQuery = query
                    )
                }
                if (DEBUG_SHOW_ADDITIONAL_MESSAGES) Log.d(TAG,"Here1")

            } else if (
                media == resultsListState.value.searchMedia
                && /* Get next X num results in existing search */
                query == resultsListState.value.searchQuery
                &&
                (resultsListState.value.resultsList.size
                < /* assuming we haven't reached end of results */
                resultsListState.value.resultCount)
            ) {
                _resultsListState.update { currentState ->
                    currentState.copy(
                        resultState = when (startFrom) {
                            /* List was filtered, but is empty */
                            0    -> ResultsUiState.Downloading
                            else -> ResultsUiState.GettingMore
                        }
                    )
                }
                if (DEBUG_SHOW_ADDITIONAL_MESSAGES) Log.d(TAG,"Here2")

            } else { /* Reset state if initiating a different
                        search "on top of" an existing one */
                resetListState(
                    media = media
                )
                searchAll(
                    media = media,
                    searchQuery = query,
                    searchType = searchType
                )
                if (DEBUG_SHOW_ADDITIONAL_MESSAGES) Log.d(TAG,"Here3")
            }

            val callback: (SearchResponse) -> Unit = { response ->
                when (response.responseCode) {
                    200 -> {
                        response.searchResults?.let { searchResults ->
                            when (
                                searchResults._metadata.resultset.size
                            ) {
                                0    -> updateEmptyList()
                                else -> updateResultsList(
                                    count = searchResults._metadata
                                        .resultset.count,
                                    fromManageSearch = fromManageSearch,
                                    results = searchResults.results,
                                    startFrom = startFrom
                                )
                            }
                        }
                    }
                    else -> updateUiState(
                        resultCode = response.responseCode
                    )
                }
            }
            try {
                networkRepository.getSearchResults(
                    callback = callback,
                    media = resultsListState.value.searchMedia,
                    queryType = resultsListState.value.queryOption,
                    searchQuery = query,
                    searchType = searchType,
                    size = resultsSize,
                    startFrom = maxOf(
                        startFrom,
                        START_FROM,
                    )
                )

            } catch (exception: IOException) {
                _resultsListState.update { currentState ->
                    currentState.copy(
                        resultsList = emptyList(),
                        resultState = ResultsUiState.Error
                    )
                }
                Log.d(TAG,exception.toString())
            }
        }
    }

    /* Also called from manageSearch() on change media */
    fun setHistory(media: Media?) {
        viewModelScope.launch {
            _resultsListState.update { currentState ->
                currentState.copy(
                    historyList = when (
                        settingsRepository.saveHistory.first()
                    ) {
                        0 -> emptyList()
                        else -> historyRepository.getHistoryItemsByMediaType(
                            media = media
                        )
                    }
                )
            }
        }
    }

    fun setQueryOption(index: Int) {
        _resultsListState.update { currentState ->
            currentState.copy(
                queryOption = index
            )
        }
    }

    fun updateEmptyList() { // 200, no results from search query
        _resultsListState.update { currentState ->
            currentState.copy(
                resultsList = emptyList(),
                resultState = when (currentState.listInterim) {
                    true -> ResultsUiState.EmptyFilter
                    else -> ResultsUiState.None
                }
            )
        }
    }

    fun updateResultsList( // 200 result code : success w/results
        count: Int,
        fromManageSearch: Boolean,
        results: List<SearchResult>,
        startFrom: Int
    ) {
        _resultsListState.update { currentState ->
            currentState.copy(
                interimFrom = when (fromManageSearch) {
                    true -> currentState.interimFrom.plus(startFrom)
                    else -> 0
                },
                interimList = when (fromManageSearch) {
                    true -> currentState.interimList
                    else -> emptyList()
                },
                listInterim = fromManageSearch,
                resultsList = when (startFrom) {
                    0 -> results
                    else -> currentState.resultsList.plus(results)
                },
                resultCount = count,
                resultState = ResultsUiState.Success
            )
        }
        /* Now that "list" & "count" are updated, update "canDownload" */
        _resultsListState.update { currentState ->
            currentState.copy(
                canDownload = (currentState.resultCount
                > /* Debug json search */
                currentState.resultsList.size.plus(START_FROM)),
            )
        }

        viewModelScope.launch {
            /* Save query to database, if user has enabled history option
               & the query (together with mediaType) not already in DB */
            if (settingsRepository.saveHistory.first() == 1) {
                historyRepository.insertHistoryItem(
                    item = resultsListState.value.searchQuery,
                    media = resultsListState.value.searchMedia
                )
                if (!resultsListState.value.historyList.contains(
                    _resultsListState.value.searchQuery
                )) {
                    _resultsListState.update { currentState ->
                        currentState.copy(
                            historyList = currentState.historyList.plus(
                                currentState.searchQuery
                            )
                        )
                    }
                }
            }
        }
    }

    fun updateUiState(resultCode: Int) { // non-200 (error) code
        _resultsListState.update { currentState ->
            currentState.copy(
                resultsList = emptyList(),
                resultState = when (resultCode) {
                    401 -> ResultsUiState.Forbidden
                    404 -> ResultsUiState.NotFound
                    else -> ResultsUiState.Error
                }
            )
        }
    }

    val zoomFullImage: LiveData<Int> = settingsRepository.zoomFullImage.asLiveData()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CollectionsApplication)
                // val favouritesRepository = application.container.favesRepository
                val historyRepository = application.container.historyRepository
                val networkRepository = application.container.networkRepository
                val settingsRepository = application.container.settingsRepository
                SharedViewModel(
                    // favouritesRepository,
                    historyRepository,
                    networkRepository,
                    settingsRepository
                )
            }
        }
        const val TAG = "SharedViewModel"
    }
}