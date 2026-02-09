package com.michaelrmossman.collections.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.state.ResultsListState
import com.michaelrmossman.collections.state.ResultsUiState
import com.michaelrmossman.collections.ui.common.ListItemCard
import com.michaelrmossman.collections.ui.components.ButtonWithIcon
import com.michaelrmossman.collections.util.ModifierUtils.downloading
import com.michaelrmossman.collections.util.ResourceUtils.downloadDrawableIds
import com.michaelrmossman.collections.util.ResourceUtils.downloadStringIds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QueryResultsList(
    contentPadding: PaddingValues,
    onClickDownloadMore: () -> Unit,
    onClickSearchResult: (SearchResult) -> Unit,
    onLongClickSearchResult: (AMapMarker) -> Unit,
    searchType: SearchType,
    viewState: ResultsListState
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.spacing_vertical_small)
        ),
        state = lazyListState,
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                bottom = contentPadding.calculateBottomPadding()
            )
    ) {

        /* itemsIndexed is used, without the need for
           index, to ensure unique ids for lazyColumn */
        itemsIndexed(
            items = viewState.resultsList,
        ) { _, result ->

            ListItemCard(
                /* Modifier used by all [SearchResult]s */
                modifier = Modifier.fillMaxWidth(),
                onClickSearchResult = onClickSearchResult,
                onLongClickSearchResult = onLongClickSearchResult,
                result = result,
                searchType = searchType
            )
        }

        /* In case Manage Search used, and Search All selected */
        if (viewState.resultsList.isNotEmpty()) {
            val isDownloading =
                viewState.resultState is ResultsUiState.GettingMore
            item(
                key = -1
            ) {
                ButtonWithIcon(
                    drawableId = when (isDownloading) {
                        true -> downloadDrawableIds.first
                        else -> when (viewState.canDownload) {
                            true -> downloadDrawableIds.second
                            else -> downloadDrawableIds.third
                        }
                    },
                    isEnabled = (
                        viewState.canDownload
                        &&
                        !isDownloading
                    ),
                    /* Refer custom modifier in ModifierUtils */
                    modifier = Modifier.downloading(isDownloading),
                    onClickButton = onClickDownloadMore,
                    stringId = when (isDownloading) {
                        true -> downloadStringIds.first
                        else -> when (viewState.canDownload) {
                            true -> downloadStringIds.second
                            else -> downloadStringIds.third
                        }
                    }
                )
            }
        }
    }
}