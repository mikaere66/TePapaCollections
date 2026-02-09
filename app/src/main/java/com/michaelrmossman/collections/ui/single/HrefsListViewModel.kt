package com.michaelrmossman.collections.ui.single

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michaelrmossman.collections.CollectionsApplication
import com.michaelrmossman.collections.data.NetworkRepository
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.network.SingleResponse
import com.michaelrmossman.collections.state.HrefsListState
import com.michaelrmossman.collections.state.HrefsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class HrefsListViewModel(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    fun getDownloadedCount(): Int =
        hrefsDownloaded.filterValues { true }.size

    val hrefsDownloaded: HashMap<Int, Boolean> = HashMap()

    private val _hrefsListState by lazy {
        MutableStateFlow(HrefsListState())
    }
    val hrefsListState: StateFlow<HrefsListState>
        get() = _hrefsListState

    private fun searchOne(
        href: String,
        hrefId: Int
    ) {
        viewModelScope.launch {

            val callback: (SingleResponse) -> Unit = { response ->

                when (val resultCode = response.responseCode) {
                    200 -> when (
                        val result = response.searchResult
                    ) {
                        null -> updateEmptyHref(
                            hrefId = hrefId
                        )
                        else -> updateHrefsList(
                            result = result
                        )
                    }
                    else -> updateUiState(
                        hrefId = hrefId,
                        resultCode = resultCode
                    )
                }
            }

            try {
                networkRepository.getSingleResult(
                    callback = callback,
                    href = href
                )

            } catch (exception: IOException) {
                _hrefsListState.update { currentState ->
                    currentState.copy(
                        hrefState = HrefsUiState.Error
                    )
                }
                Log.d(TAG,exception.toString())
            }
        }
    }

    fun setHrefItem(hrefItem: SearchResult) {
        if (
            hrefsDownloaded[hrefItem.id] == null
            ||
            hrefsDownloaded[hrefItem.id] == false
        ) {
            _hrefsListState.update { currentState ->
                currentState.copy(
                    hrefState = HrefsUiState.Downloading
                )
            }
            searchOne(
                href = hrefItem.href,
                hrefId = hrefItem.id
            )
        }
    }

    fun setHrefsList(hrefsList: List<SearchResult>) {

        /* Initialise/reinitialise upon
           entering HrefDetailsScreen() */
        _hrefsListState.update { currentState ->
            currentState.copy(
                doneCount = 0,
                hrefCount = hrefsList.size,
                hrefsList = hrefsList
            )
        }
    }

    fun updateEmptyHref( // 200, but no result for query
        hrefId: Int
    ) {
        hrefsDownloaded[hrefId] = false

        _hrefsListState.update { currentState ->
            currentState.copy(
                hrefState = HrefsUiState.None
            )
        }
    }

    fun updateHrefsList( // 200 result code : success
        result: SearchResult?
    ) {
        result?.let { searchResult ->
            hrefsDownloaded[searchResult.id] = true

            _hrefsListState.update { currentState ->
                currentState.copy(
                    doneCount = getDownloadedCount(),
                    hrefsList = currentState.hrefsList.map { href ->
                        when (searchResult.id) {
                            href.id -> searchResult
                            else -> href
                        }
                    },
                    hrefState = HrefsUiState.Success
                )
            }
        }
    }

    fun updateUiState( // non-200 (error) : no joy
        hrefId: Int,
        resultCode: Int
    ) {
        hrefsDownloaded[hrefId] = false

        _hrefsListState.update { currentState ->
            currentState.copy(
                hrefState = when (resultCode) {
                    401 -> HrefsUiState.Forbidden
                    404 -> HrefsUiState.NotFound
                    else -> HrefsUiState.Error
                }
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CollectionsApplication)
                val networkRepository = application.container.networkRepository
                HrefsListViewModel(networkRepository)
            }
        }
        const val TAG = "HrefsListViewModel"
    }
}