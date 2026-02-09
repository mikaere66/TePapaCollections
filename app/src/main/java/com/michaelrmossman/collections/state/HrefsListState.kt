package com.michaelrmossman.collections.state

import com.michaelrmossman.collections.model.SearchResult

data class HrefsListState(
    val doneCount: Int = 0,
    val hrefCount: Int = 0,
    val hrefsList: List<SearchResult> = emptyList(),
    val hrefState: HrefsUiState = HrefsUiState.Init
)