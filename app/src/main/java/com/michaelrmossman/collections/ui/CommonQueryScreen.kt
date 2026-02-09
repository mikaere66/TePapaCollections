package com.michaelrmossman.collections.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.state.ResultsUiState
import com.michaelrmossman.collections.ui.common.EmptyFilterScreen
import com.michaelrmossman.collections.ui.common.EmptySearchScreen
import com.michaelrmossman.collections.ui.common.ErrorScreen
import com.michaelrmossman.collections.ui.common.ForbiddenScreen
import com.michaelrmossman.collections.ui.common.InitScreen
import com.michaelrmossman.collections.ui.common.InvalidScreen
import com.michaelrmossman.collections.ui.common.LoadingScreen
import com.michaelrmossman.collections.ui.common.NotFoundScreen
import com.michaelrmossman.collections.ui.components.DynamicActionMenu
import com.michaelrmossman.collections.ui.components.SearchBoxSuggestions
import com.michaelrmossman.collections.ui.components.SearchBoxWithButton
import com.michaelrmossman.collections.ui.components.TwoLineAppBar
import com.michaelrmossman.collections.util.DialogUtils.IconsLegendDialog
import com.michaelrmossman.collections.util.EmojiInputFilter
import com.michaelrmossman.collections.util.TextUtils.getAppSubtitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonQueryScreen(
    @DrawableRes drawableId: Int,
    entries: Iterable<Media>,
    onClickBackButton: () -> Unit,
    onClickNewSearch: () -> Unit,
    onClickSearchResult: (SearchResult) -> Unit,
    onLongClickSearchResult: (AMapMarker) -> Unit,
    searchType: SearchType,
    windowWidthSize: WindowWidthSizeClass,
    @StringRes stringId: Int
) {
    val sharedViewModel: SharedViewModel = viewModel(
        factory = SharedViewModel.Factory
    )
    /* Used to either populate, or tear down, history */
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    lifecycleOwner.lifecycle.addObserver(sharedViewModel)

    var filteredSuggestions by remember { mutableStateOf(emptyList<String>()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showIconsDialog by remember { mutableStateOf(false) }
    val viewState by sharedViewModel.resultsListState.collectAsState()

    var media by rememberSaveable { mutableStateOf(viewState.searchMedia) }
    val onClickSearchButton = { query: String ->
        keyboardController?.hide()
        filteredSuggestions = emptyList()
        sharedViewModel.searchAll(
            media = media,
            searchQuery = query,
            searchType = searchType,
            startFrom = when (
                media == viewState.searchMedia
                &&
                query == viewState.searchQuery
            ) {
                /* Same media, same query: just get more */
                true -> viewState.resultsList.size
                 /* Same media, diff query: start afresh */
                else -> when (media) {
                    viewState.searchMedia -> {
                        onClickNewSearch()
                        0
                    }
                    /* List filtered mid query: get more */
                    else -> viewState.resultsList.size
                }
            }
        )
    }
    var searchQuery by rememberSaveable { mutableStateOf(viewState.searchQuery) }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    /* DynamicActionMenu takes params : lists
                       of ENABLED, ACTIONS, and STRING IDs...
                       in this case, providing menu items for
                       Clear Search Results | Icons Legend */
                    DynamicActionMenu(
                        isEnabled = listOf(
                            (
                                viewState.interimList.isNotEmpty()
                                ||
                                viewState.resultsList.isNotEmpty()
                            ),
                            true /* showIconsDialog always avail */
                        ),
                        menuLabels = listOf(
                            R.string.menu_clear_list,
                            R.string.menu_icons_legend
                        ).map { stringId ->
                            stringResource(stringId)
                        },
                        onClickActions = listOf(
                            {
                                sharedViewModel.resetListState()
                                searchQuery = String()
                            },
                            {
                                showIconsDialog = true
                            }
                        )
                    )
                },
                onClickBackButton = onClickBackButton,
                stringId = R.string.app_name,
                subtitle = getAppSubtitle(
                    isInterim = viewState.listInterim,
                    listSize = viewState.resultsList.size,
                    media = media,
                    resultCount = viewState.resultCount,
                    stringId = stringId
                )
            )
        }
    ) { contentPadding ->

        if (showIconsDialog) {
            IconsLegendDialog(
                entries = entries, // MediaObject.entries,
                onClickConfirm = { showIconsDialog = false },
                title = stringResource(R.string.menu_icons_legend)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_vertical_small)
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = contentPadding.calculateTopPadding()
                )
        ) {
            SearchBoxWithButton(
                currentMedia = media,
                currentQueryType = viewState.queryOption,
                entries = entries,
                isEnabled = (
                    viewState.resultState != ResultsUiState.Downloading
                ),
                onClickClearButton = {
                    sharedViewModel.cancelDownloadState()
                    filteredSuggestions = emptyList()
                    searchQuery = String()
                },
                onClickMediaItem = { index ->
                    // android.util.Log.d("HEY",index.toString())
                    val element = when (index) {
                        -1   -> null /* search all, UNLESS we're in
                        Collections ... refer NetworkRepository */
                        else -> entries.elementAt(index)
                    }
                    media = element
                    sharedViewModel.manageSearch(media = element)
                },
                onClickQueryItem = { index ->
                    sharedViewModel.setQueryOption(index)
                },
                onClickSearchButton = onClickSearchButton,
                onTextChanged = { query ->
                    val filteredQuery = EmojiInputFilter().filter(
                        query,0,query.length,
                        null,0,0
                    )
                    searchQuery = filteredQuery
                    filteredSuggestions = when (filteredQuery.isBlank()) {
                        true -> emptyList()
                        else -> viewState.historyList.filter { historyItem ->
                            historyItem.contains(
                                filteredQuery, ignoreCase = true
                            )
                        }
                    }
                },
                searchQuery = searchQuery
            )

            Box(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.padding_small)
                )
            ) {
                /* Need to specify fillMaxSize each time, so that
                   preview screens in AndroidStudio fit better */
                when (viewState.resultState) {
                    is ResultsUiState.Downloading -> LoadingScreen(
                        modifier = Modifier.fillMaxSize(),
                        stringId = R.string.loading_anim
                    )
                    is ResultsUiState.EmptyFilter -> EmptyFilterScreen(
                        canDownload = viewState.canDownload,
                        mediaType = viewState.searchMedia,
                        modifier = Modifier.fillMaxSize(),
                        onClickDownloadMore = {
                            /* Use values from viewState (rather than
                               saveable(s) above) in case user reenters
                               this screen after leaving it briefly */
                            sharedViewModel.searchAll(
                                fromManageSearch = true,
                                media = viewState.searchMedia,
                                searchQuery = viewState.searchQuery,
                                searchType = searchType,
                                startFrom = maxOf(
                                    viewState.interimFrom,
                                    viewState.interimList.size
                                )
                            )
                        },
                        query = viewState.searchQuery
                    )
                    is ResultsUiState.Error -> ErrorScreen(
                        modifier = Modifier.fillMaxSize(),
                        retryAction = {
                            sharedViewModel.searchAll(
                                media = media,
                                searchQuery = viewState.searchQuery,
                                searchType = searchType
                            )
                        }
                    )
                    is ResultsUiState.Forbidden -> ForbiddenScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                    is ResultsUiState.Init -> InitScreen(
                        drawableId = drawableId,
                        stringId = when (searchType) {
                            SearchType.MediaSpecimen,
                            SearchType.MediaObject -> {
                                R.string.details_placeholder_collection
                            }
                            SearchType.MediaType -> {
                                R.string.details_placeholder_type
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        windowWidthSize = windowWidthSize
                    )
                    is ResultsUiState.Invalid -> InvalidScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                    is ResultsUiState.None -> EmptySearchScreen(
                        mediaType = viewState.searchMedia,
                        modifier = Modifier.fillMaxSize(),
                        onClickRemoveFilter = {
                            media = null
                            sharedViewModel.manageSearch(null)
                            sharedViewModel.searchAll(
                                media = null,
                                searchQuery = viewState.searchQuery,
                                searchType = searchType
                            )
                        },
                        query = viewState.searchQuery
                    )
                    is ResultsUiState.NotFound -> NotFoundScreen(
                        modifier = Modifier.fillMaxSize(),
                        query = viewState.searchQuery
                    )
                    is ResultsUiState.Success, ResultsUiState.GettingMore -> {
                        QueryResultsList(
                            contentPadding = contentPadding,
                            onClickDownloadMore = {
                                /* As note above re viewState values */
                                sharedViewModel.searchAll(
                                    media = viewState.searchMedia,
                                    searchQuery = viewState.searchQuery,
                                    searchType = searchType,
                                    startFrom = maxOf(
                                        viewState.interimFrom,
                                        viewState.resultsList.size
                                    )
                                )
                            },
                            onClickSearchResult = onClickSearchResult,
                            onLongClickSearchResult = onLongClickSearchResult,
                            searchType = searchType,
                            viewState = viewState
                        )

                    }
                }

                if (filteredSuggestions.isNotEmpty()) {
                    SearchBoxSuggestions(
                        filteredSuggestions = filteredSuggestions,
                        onClickSuggestion = { suggestion ->
                            onClickSearchButton(suggestion)
                            searchQuery = suggestion
                            /* Clear filtered suggestions
                               when suggestion clicked */
                            filteredSuggestions = emptyList()
                        }
                    )
                }
            }
        }
    }
}