package com.michaelrmossman.collections.ui.favourites

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.michaelrmossman.collections.data.FaveEntity
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.state.HrefsUiState
import com.michaelrmossman.collections.ui.common.DetailsCard
import com.michaelrmossman.collections.ui.common.ErrorScreen
import com.michaelrmossman.collections.ui.common.ForbiddenScreen
import com.michaelrmossman.collections.ui.common.LoadingScreen
import com.michaelrmossman.collections.ui.common.NotFoundScreen
import com.michaelrmossman.collections.ui.components.TwoLineAppBar
import com.michaelrmossman.collections.util.formatWithComma

/* While this does page thru MULTIPLE favourites, it's called SINGLE because, for
   each page, it downloads a single href, similar to .single.HrefDetailsScreen */
@Composable
fun HrefSingleScreen(
    faveIndex: Int,
    favesList: List<FaveEntity>,
    isSearchVisible: Boolean,
    onClickBackButton: () -> Unit,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    onClickImages: (List<ImageObject>, String) -> Unit,
    onClickMapButton: (AMapMarker) -> Unit,
    searchType: SearchType,
    @StringRes stringId: Int,
    windowSize: WindowWidthSizeClass,
    /* Modifier used by all [SearchResult]s */
    modifier: Modifier = Modifier
) {
    val hrefViewModel: HrefSingleViewModel = viewModel(
        factory = HrefSingleViewModel.Factory
    )
    LaunchedEffect(key1 = Unit) {
        hrefViewModel.setFavesList(favesList)
    }

    val viewState by hrefViewModel.hrefSingleState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var scrollToIndex by rememberSaveable { mutableIntStateOf(-1) }
    var pageIndex by rememberSaveable {
        mutableIntStateOf(faveIndex)
    }
    val pagerState = rememberPagerState(
        initialPage = pageIndex,
        pageCount = { viewState.favesList.size }
    )
    /* innerPadding is equivalent to medium.minus(small).dp */
    val innerPadding = dimensionResource(R.dimen.details_inner_padding)

    LaunchedEffect(key1 = pagerState) {
        // Collect from the snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            hrefViewModel.setFaveItem(viewState.favesList[page])
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TwoLineAppBar(
                actions = { /* Not used */ },
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

        /* Row not reqd, but to mimic SearchBoxWithContent layout */
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = dimensionResource(
                    R.dimen.page_spacing
                ),
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (viewState.hrefState) {
                    HrefsUiState.Error -> ErrorScreen(
                        modifier = Modifier.fillMaxSize(),
                        retryAction = {
                            hrefViewModel.setFaveItem(
                                viewState.favesList[pageIndex]
                            )
                        }
                    )
                    HrefsUiState.Forbidden -> ForbiddenScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                    HrefsUiState.NotFound -> NotFoundScreen(
                        modifier = Modifier.fillMaxSize(),
                        query = favesList[page].title
                    )
                    HrefsUiState.Success -> {
                        viewState.hrefsList.find { href ->
                            href.href == favesList[page].href
                        }?.let { result ->
                            DetailsCard(
                                contentPadding = contentPadding,
                                isNestedContent = true,
                                isSearchVisible = isSearchVisible,
                                /* Modifier used by all items */
                                modifier = modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                onClickHrefItem = onClickHrefItem,
                                onClickImages = onClickImages,
                                onClickMapButton = onClickMapButton,
                                result = result,
                                searchType = searchType,
                                windowSize = windowSize
                            )
                        }
                    }
                    else -> LoadingScreen(
                        modifier = Modifier.fillMaxSize(),
                        stringId = R.string.loading_anim
                    )
                }
            }
        }
    }
}