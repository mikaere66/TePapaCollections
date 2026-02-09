package com.michaelrmossman.collections.state

sealed interface ResultsUiState {
    data object Downloading: ResultsUiState
    data object EmptyFilter: ResultsUiState
    data object Error      : ResultsUiState
    data object Forbidden  : ResultsUiState
    data object GettingMore: ResultsUiState
    data object Init       : ResultsUiState
    data object Invalid    : ResultsUiState
    data object None       : ResultsUiState
    data object NotFound   : ResultsUiState
    data object Success    : ResultsUiState
}