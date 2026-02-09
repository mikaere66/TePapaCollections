package com.michaelrmossman.collections.ui.favourites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michaelrmossman.collections.CollectionsApplication
import com.michaelrmossman.collections.data.FaveEntity
import com.michaelrmossman.collections.data.NetworkRepository
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.network.SingleResponse
import com.michaelrmossman.collections.state.HrefSingleState
import com.michaelrmossman.collections.state.HrefsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class HrefSingleViewModel(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    fun getDownloadedCount(): Int =
        hrefsDownloaded.filterValues { true }.size

    /* In this instance, Int relates to fave.id */
    lateinit var hrefsDownloaded: HashMap<Int, Boolean>

    private val _hrefSingleState by lazy {
        MutableStateFlow(HrefSingleState())
    }
    val hrefSingleState: StateFlow<HrefSingleState>
        get() = _hrefSingleState

    private fun searchOne(
        href: String,
        faveId: Int
    ) {
        viewModelScope.launch {

            val callback: (SingleResponse) -> Unit = { response ->

                when (val resultCode = response.responseCode) {
                    200 -> when (
                        val result = response.searchResult
                    ) {
                        null -> updateEmptyHref(
                            faveId = faveId
                        )
                        else -> updateHrefsList(
                            faveId = faveId,
                            result = result
                        )
                    }
                    else -> updateUiState(
                        faveId = faveId,
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
                _hrefSingleState.update { currentState ->
                    currentState.copy(
                        hrefState = HrefsUiState.Error
                    )
                }
                Log.d(TAG,exception.toString())
            }
        }
    }

    fun setFavesList(favesList: List<FaveEntity>) {

        hrefsDownloaded = HashMap()

        /* Initialise/reinitialise upon
           entering HrefSingleScreen() */
        _hrefSingleState.update { currentState ->
            currentState.copy(
                doneCount = 0,
                favesList = favesList,
                hrefCount = favesList.size,
                hrefsList = emptyList()
            )
        }
    }

    fun setFaveItem(faveItem: FaveEntity) {
        if (
            hrefsDownloaded[faveItem.id] == null
            ||
            hrefsDownloaded[faveItem.id] == false
        ) {
            _hrefSingleState.update { currentState ->
                currentState.copy(
                    hrefState = HrefsUiState.Downloading
                )
            }
            searchOne(
                href = faveItem.href,
                faveId = faveItem.id
            )
        }
    }

    fun updateEmptyHref( // 200, but no result for query
        faveId: Int
    ) {
        hrefsDownloaded[faveId] = false

        _hrefSingleState.update { currentState ->
            currentState.copy(
                /* Note use of NotFound vs None */
                hrefState = HrefsUiState.NotFound
            )
        }
    }

    fun updateHrefsList( // 200 result code : success
        faveId: Int,
        result: SearchResult?
    ) {
        result?.let { searchResult ->
            hrefsDownloaded[faveId] = true

            _hrefSingleState.update { currentState ->
                currentState.copy(
                    doneCount = getDownloadedCount(),
                    hrefsList = currentState.hrefsList.plus(
                        result
                    ),
                    hrefState = HrefsUiState.Success
                )
            }
        }
    }

    fun updateUiState( // non-200 (error) : no joy
        faveId: Int,
        resultCode: Int
    ) {
        hrefsDownloaded[faveId] = false

        _hrefSingleState.update { currentState ->
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
                HrefSingleViewModel(networkRepository)
            }
        }
        const val TAG = "HrefsListViewModel"
    }
}