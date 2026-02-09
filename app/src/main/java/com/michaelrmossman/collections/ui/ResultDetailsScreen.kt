package com.michaelrmossman.collections.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.state.ResultsUiState
import com.michaelrmossman.collections.ui.common.DetailsCard
import com.michaelrmossman.collections.ui.common.ToggleFaveBottomSheet
import com.michaelrmossman.collections.ui.components.DownloadButton
import com.michaelrmossman.collections.ui.components.SearchBoxWithContent
import com.michaelrmossman.collections.ui.components.SearchButton
import com.michaelrmossman.collections.ui.components.SingleActionMenu
import com.michaelrmossman.collections.ui.components.TwoLineAppBar
import com.michaelrmossman.collections.util.formatWithComma
import com.michaelrmossman.collections.util.getResultsHashMap
import com.michaelrmossman.collections.util.getSearchResult

@Composable
fun ResultDetailsScreen(
    isSearchVisible: Boolean,
    onClickBackButton: () -> Unit,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    onClickImages: (List<ImageObject>, String) -> Unit,
    onClickMapButton: (AMapMarker) -> Unit,
    onToggleSearch: () -> Unit,
    searchResult: SearchResult,
    searchType: SearchType,
    @StringRes stringId: Int,
    windowSize: WindowWidthSizeClass,
    /* Modifier used by all [SearchResult]s */
    modifier: Modifier = Modifier
) {
    val sharedViewModel: SharedViewModel = viewModel(
        factory = SharedViewModel.Factory
    )

    val viewState by sharedViewModel.resultsListState.collectAsState()
    val initialIndex = viewState.resultsList.indexOf(searchResult)
    val isDownloading = viewState.resultState is ResultsUiState.GettingMore
    val isLastPage by remember { /* Must be remember */
        derivedStateOf { viewState.resultsList.size.minus(1) }
    }

    /* innerPadding is equivalent to medium.minus(small).dp */
    val innerPadding = dimensionResource(R.dimen.details_inner_padding)
    var isOnLastPage by rememberSaveable { mutableStateOf(false) }
    var pageIndex by rememberSaveable {
        mutableIntStateOf(initialIndex)
    }
    val pagerState = rememberPagerState(
        initialPage = pageIndex,
        pageCount = { viewState.resultsList.size }
    )
    val resultsHashMap = viewState.resultsList.getResultsHashMap()
    var scrollToIndex by rememberSaveable { mutableIntStateOf(-1) }
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = pagerState) {
        // Collect from the snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            /* Store current index as pageIndex, in case of
               activity restarted, i.e. screen rotation */
            pageIndex = page
            /* Enable the Download More icon on last page */
            isOnLastPage = (page == isLastPage)
        }
    }

    LaunchedEffect(key1 = scrollToIndex) {
        if (scrollToIndex != -1) {
            pagerState.animateScrollToPage(
                scrollToIndex
            )
            scrollToIndex = -1
        }
    }

    if (showBottomSheet) {
        ToggleFaveBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            result = viewState.resultsList[pageIndex],
            searchType = searchType
        )
    }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    if (windowSize == WindowWidthSizeClass.Compact) {
                        DownloadButton(
                            canDownloadMore = viewState.canDownload,
                            isDownloading = isDownloading,
                            isEnabled = (
                                viewState.canDownload
                                &&
                                !isDownloading
                                &&
                                isOnLastPage
                            ),
                            onClickDownloadButton = {
                                sharedViewModel.searchAll(
                                    media = viewState.searchMedia,
                                    searchType = searchType,
                                    searchQuery = viewState.searchQuery,
                                    startFrom = viewState.resultsList.size
                                )
                                isOnLastPage = false
                            }
                        )
                        SearchButton(
                            isSearchVisible = isSearchVisible,
                            onToggleSearch = onToggleSearch
                        )
                        SingleActionMenu(
                            itemStringId = R.string.menu_toggle_fave,
                            onSingleItemClick = {
                                showBottomSheet = true
                            }
                        )
                    }
                },
                onClickBackButton = onClickBackButton,
                stringId = when (windowSize) {
                    WindowWidthSizeClass.Compact -> {
                        stringId
                    }
                    /* Pass zero as titleId for larger screens,
                       to indicate NO navigation or title text */
                    else -> 0
                },
                subtitle = stringResource(
                    R.string.app_subtitle,
                    stringResource(R.string.results_subtitle),
                    viewState.resultsList.size.formatWithComma(),
                    viewState.resultCount.formatWithComma()
                )
            )
        }
    ) { contentPadding ->

        val content: (@Composable (SearchResult) -> Unit) = { result ->
            DetailsCard(
                contentPadding = contentPadding,
                isNestedContent = false,
                isSearchVisible = isSearchVisible,
                /* Modifier used by all [SearchResult]s */
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                onClickHrefItem = onClickHrefItem,
                onClickImages = onClickImages,
                onClickMapButton = onClickMapButton,
                result = result,
                searchType = searchType,
                windowSize = windowSize
            )
        }
        when (windowSize == WindowWidthSizeClass.Compact) {
            true -> {
                val onClickSearchItem: (Int) -> Unit = { itemId ->
                    viewState.resultsList.getSearchResult(
                        itemId = itemId
                    )?.let { result ->
                        scrollToIndex = viewState.resultsList.indexOf(result)
                        /* animateScrollToPage before hiding
                           visibility creates cool effect */
                        onToggleSearch()
                    }
                }

                SearchBoxWithContent(
                    contentPadding = contentPadding,
                    hashMap = resultsHashMap,
                    isSearchVisible = isSearchVisible,
                    onClickSearchItem = onClickSearchItem,
                    content = {
                        HorizontalPager(
                            state = pagerState,
                            pageSpacing = dimensionResource(
                                R.dimen.page_spacing
                            ),
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            content(viewState.resultsList[page])
                        }
                    }
                )
            }
            /* If new search carried out while content showing */
            else -> if (initialIndex != -1) {
                content(viewState.resultsList[initialIndex])
            }
        }
    }
}