package com.michaelrmossman.collections.state

import com.michaelrmossman.collections.data.FaveEntity
import com.michaelrmossman.collections.model.SearchResult

data class HrefSingleState(
    val doneCount: Int = 0,
    val favesList: List<FaveEntity> = emptyList(),
    val hrefCount: Int = 0,
    val hrefsList: List<SearchResult> = emptyList(),
    val hrefState: HrefsUiState = HrefsUiState.Init
)