package com.michaelrmossman.collections.ui.single

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.michaelrmossman.collections.state.HrefsUiState
import com.michaelrmossman.collections.ui.common.DetailsCard
import com.michaelrmossman.collections.ui.components.DownloadStatus
import com.michaelrmossman.collections.ui.components.SearchBoxWithContent
import com.michaelrmossman.collections.ui.components.SearchButton
import com.michaelrmossman.collections.ui.components.SnackbarMessage
import com.michaelrmossman.collections.ui.components.TwoLineAppBar
import com.michaelrmossman.collections.util.formatWithComma
import com.michaelrmossman.collections.util.getResultsHashMap
import com.michaelrmossman.collections.util.getSearchResult

@Composable
fun HrefDetailsScreen(
    hrefIndex: Int,
    hrefsList: List<SearchResult>,
    isSearchVisible: Boolean,
    onClickBackButton: () -> Unit,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    onClickImages: (List<ImageObject>, String) -> Unit,
    onClickMapButton: (AMapMarker) -> Unit,
    onToggleSearch: () -> Unit,
    searchType: SearchType,
    @StringRes stringId: Int,
    windowSize: WindowWidthSizeClass,
    /* Modifier used by all [SearchResult]s */
    modifier: Modifier = Modifier
) {
    val hrefsViewModel: HrefsListViewModel = viewModel(
        factory = HrefsListViewModel.Factory
    )
    LaunchedEffect(key1 = Unit) {
        hrefsViewModel.setHrefsList(hrefsList)
    }

    val viewState by hrefsViewModel.hrefsListState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var scrollToIndex by rememberSaveable { mutableIntStateOf(-1) }
    val resultsHashMap = viewState.hrefsList.getResultsHashMap()
    var pageIndex by rememberSaveable {
        mutableIntStateOf(hrefIndex)
    }
    val pagerState = rememberPagerState(
        initialPage = pageIndex,
        pageCount = { viewState.hrefsList.size }
    )
    val onClickSearchItem: (Int) -> Unit = { itemId ->
        viewState.hrefsList.getSearchResult(
            itemId = itemId
        )?.let { result ->
            scrollToIndex = viewState.hrefsList.indexOf(result)
            /* animateScrollToPage before hiding
               visibility creates cool effect */
            onToggleSearch()
        }
    }
    /* innerPadding is equivalent to medium.minus(small).dp */
    val innerPadding = dimensionResource(R.dimen.details_inner_padding)
    val isNotFound = viewState.hrefState is HrefsUiState.NotFound
    val isForbidden = viewState.hrefState is HrefsUiState.Forbidden
    val isError = (
        viewState.hrefState is HrefsUiState.Error
        ||
        viewState.hrefState is HrefsUiState.None
    )
    val isDownloading = viewState.hrefState is HrefsUiState.Downloading

    LaunchedEffect(key1 = pagerState) {
        // Collect from the snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            hrefsViewModel.setHrefItem(viewState.hrefsList[page])
            /* Store current index as pageIndex, in case of
               activity restarted, i.e. screen rotation */
            pageIndex = page
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

    if (isForbidden) {
        SnackbarMessage(
            message = stringResource(R.string.forbidden_api_key_short),
            snackbarHostState = snackbarHostState
        )
    }

    if (isNotFound) {
        SnackbarMessage(
            message = stringResource(R.string.found_not_message_short),
            snackbarHostState = snackbarHostState
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TwoLineAppBar(
                actions = {
                    DownloadStatus(
                        isDownloading = isDownloading,
                        isError = isError,
                        isForbidden = isForbidden,
                        isNotFound = isNotFound,
                        onClickDownloadButton = {
                            hrefsViewModel.setHrefItem(
                                viewState.hrefsList[pageIndex]
                            )
                        }
                    )
                    SearchButton(
                        isSearchVisible = isSearchVisible,
                        onToggleSearch = onToggleSearch
                    )
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
                    stringResource(R.string.hrefs_subtitle),
                    viewState.doneCount.formatWithComma(),
                    viewState.hrefCount.formatWithComma()
                )
            )
        }
    ) { contentPadding ->

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
                    DetailsCard(
                        contentPadding = contentPadding,
                        isNestedContent = true,
                        isSearchVisible = isSearchVisible,
                        /* Modifier used by all [SearchResult]s */
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                        onClickHrefItem = onClickHrefItem,
                        onClickImages = onClickImages,
                        onClickMapButton = onClickMapButton,
                        result = viewState.hrefsList[page],
                        searchType = searchType,
                        windowSize = windowSize
                    )
                }
            }
        )
    }
}