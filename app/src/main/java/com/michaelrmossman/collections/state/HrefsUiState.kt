package com.michaelrmossman.collections.state

sealed interface HrefsUiState {
    data object Downloading: HrefsUiState
    data object EmptyFilter: HrefsUiState
    data object Error      : HrefsUiState
    data object Forbidden  : HrefsUiState
    data object Init       : HrefsUiState
    data object Invalid    : HrefsUiState
    data object None       : HrefsUiState
    data object NotFound   : HrefsUiState
    data object Success    : HrefsUiState
}