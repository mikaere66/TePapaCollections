package com.michaelrmossman.collections.state

import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.model.SearchResult

data class ResultsListState(
    val canDownload: Boolean = false,
    val historyList: List<String> = emptyList(),
    val interimFrom: Int = 0,
    val interimList: List<SearchResult> = emptyList(),
    val listInterim: Boolean = false,
    val queryOption: Int = 0,
    val resultCount: Int = 0,
    val resultsList: List<SearchResult> = emptyList(),
    val resultState: ResultsUiState = ResultsUiState.Init,
    val searchMedia: Media? = null,
    val searchQuery: String = String()
) {
    companion object {
        const val START_FROM = 0
    }
}