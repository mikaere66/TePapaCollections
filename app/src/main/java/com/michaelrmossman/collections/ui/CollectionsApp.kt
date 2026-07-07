package com.michaelrmossman.collections.ui

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.MediaObject
import com.michaelrmossman.collections.enum.MediaSpecimen
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.navigation.CurrentScreen
import com.michaelrmossman.collections.ui.common.AllImagesScreen
import com.michaelrmossman.collections.ui.common.AnImageScreen
import com.michaelrmossman.collections.ui.content.ContentPlaceholder
import com.michaelrmossman.collections.ui.help.HelpScreen
import com.michaelrmossman.collections.ui.main.MainScreen
import com.michaelrmossman.collections.ui.favourites.FavesListScreen
import com.michaelrmossman.collections.ui.favourites.HrefSingleScreen
import com.michaelrmossman.collections.ui.maps.MapScreen
import com.michaelrmossman.collections.ui.single.HrefDetailsScreen
import com.michaelrmossman.collections.ui.variables.SettingsScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CollectionsApp(windowWidthSize: WindowWidthSizeClass) {

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val collectionsViewModel: CollectionsViewModel = viewModel(
        factory = CollectionsViewModel.Factory
    )
    val currentScreenItems = listOf(
        CurrentScreen.ExplorerScreen,
        CurrentScreen.ObjectsScreen,
        CurrentScreen.SpecimensScreen,
        CurrentScreen.FavesScreen,
        CurrentScreen.SettingsScreen,
        CurrentScreen.HelpScreen
    )
    val directive = remember(adaptiveInfo) {
        calculatePaneScaffoldDirective(adaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val faveCount = collectionsViewModel.faveCount.observeAsState(initial = 0)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(
        directive = directive
    )
    var isSearchVisible by remember { mutableStateOf(false) }
    val onClickActions: List<() -> Unit> = listOf(
        { collectionsViewModel.put(CurrentScreen.ExplorerScreen) },
        { collectionsViewModel.put(CurrentScreen.ObjectsScreen) },
        { collectionsViewModel.put(CurrentScreen.SpecimensScreen) },
        { collectionsViewModel.put(CurrentScreen.FavesScreen) },
        { collectionsViewModel.put(CurrentScreen.SettingsScreen) },
        { collectionsViewModel.put(CurrentScreen.HelpScreen) }
    )
    val onToggleSearch = { isSearchVisible = !isSearchVisible }
    val screensEnabled = currentScreenItems.map { screen ->
        when (screen == CurrentScreen.FavesScreen) {
            true -> (faveCount.value != 0)
            else -> true
        }
    }
    val sharedViewModel: SharedViewModel = viewModel(
        factory = SharedViewModel.Factory
    )

    NavDisplay(
        backStack = collectionsViewModel.backStack,
        onBack = {
            collectionsViewModel.pop()
            isSearchVisible = false
            /* Because sharedViewModel is used for several screens, we
               need to reset ResultsListState to its default values */
            if (collectionsViewModel.backStack.size == 1) {
                sharedViewModel.resetListState()
            }
        },
        sceneStrategy = listDetailStrategy,

        entryProvider = entryProvider {
            /* Entries in alphabetical order, by CurrentScreen key */
            entry<CurrentScreen.ExplorerScreen>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        ContentPlaceholder(
                            drawableId =
                                R.drawable.outline_category_search_24,
                            stringId =
                                R.string.details_placeholder_type
                        )
                    }
                )
            ) { currentScreen ->
                CommonQueryScreen(
                    drawableId = currentScreen.drawableId,
                    entries = MediaType.entries,
                    onClickBackButton = { collectionsViewModel.home() },
                    onClickNewSearch = {
                        collectionsViewModel.removeIfNot(
                            CurrentScreen.ExplorerScreen
                        )
                    },
                    onClickSearchResult = { result ->
                        collectionsViewModel.put(
                            CurrentScreen.ResultDetails(
                                result, SearchType.MediaType
                            )
                        )
                    },
                    onLongClickSearchResult = { mapMarker ->
                        collectionsViewModel.put(
                            CurrentScreen.MapScreen(mapMarker)
                        )
                    },
                    searchType = SearchType.MediaType,
                    stringId = currentScreen.titleStringId,
                    windowWidthSize = windowWidthSize
                )
            }

            entry<CurrentScreen.FavesScreen> { currentScreen ->
                FavesListScreen(
                    onClickBackButton = { collectionsViewModel.pop() },
                    onClickFavourite = { faveIndex, favesList ->
                        collectionsViewModel.put(
                            CurrentScreen.HrefSingle(faveIndex, favesList)
                        )
                    },
                    onLongClickFavourite = { mapMarker ->
                        collectionsViewModel.put(
                            CurrentScreen.MapScreen(mapMarker)
                        )
                    }
                )
            }

            entry<CurrentScreen.HelpScreen> { currentScreen ->
                HelpScreen(
                    onClickBackButton = { collectionsViewModel.pop() },
                    stringId = currentScreen.titleStringId
                )
            }

            entry<CurrentScreen.HrefDetails> { currentScreen ->
                HrefDetailsScreen(
                    hrefIndex = currentScreen.hrefIndex,
                    hrefsList = currentScreen.hrefsList,
                    isSearchVisible = isSearchVisible,
                    onClickBackButton = { collectionsViewModel.pop() },
                    onClickHrefItem = { hrefsList, index ->
                        collectionsViewModel.put(
                            CurrentScreen.HrefDetails(
                                hrefsList, index
                            )
                        )
                    },
                    onClickImages = { imageObjects, itemTitle ->
                        collectionsViewModel.put(
                            CurrentScreen.ImagesScreen(
                                imageObjects, itemTitle
                            )
                        )
                    },
                    onClickMapButton = { mapMarker ->
                        collectionsViewModel.put(
                            CurrentScreen.MapScreen(mapMarker)
                        )
                    },
                    onToggleSearch = onToggleSearch,
                    /* Not actually used in this context */
                    searchType = SearchType.MediaType,
                    stringId = currentScreen.titleStringId,
                    windowSize = windowWidthSize
                )
            }

            entry<CurrentScreen.HrefSingle> { currentScreen ->
                HrefSingleScreen(
                    faveIndex = currentScreen.faveIndex,
                    favesList = currentScreen.favesList,
                    /* Not to be taken literally, but since we're
                       not using SearchBoxWithContent(), will
                       mean 0.dp top padding for DetailsCard() */
                    isSearchVisible = true,
                    onClickBackButton = { collectionsViewModel.pop() },
                    onClickHrefItem = { hrefsList, index ->
                        collectionsViewModel.put(
                            CurrentScreen.HrefDetails(
                                hrefsList, index
                            )
                        )
                    },
                    onClickImages = { imageObjects, itemTitle ->
                        collectionsViewModel.put(
                            CurrentScreen.ImagesScreen(
                                imageObjects, itemTitle
                            )
                        )
                    },
                    onClickMapButton = { mapMarker ->
                        collectionsViewModel.put(
                            CurrentScreen.MapScreen(mapMarker)
                        )
                    },
                    /* Not actually used in this context */
                    searchType = SearchType.MediaType,
                    stringId = currentScreen.titleStringId,
                    windowSize = windowWidthSize
                )
            }

            entry<CurrentScreen.ImagesScreen> { currentScreen ->
                AllImagesScreen( // Multi
                    imageObjects = currentScreen.imageObjects,
                    itemTitle = currentScreen.itemTitle,
                    onClickBackButton = { collectionsViewModel.pop() },
//                    onClickImage = { imageObject, itemTitle ->
//                        collectionsViewModel.put(
//                            CurrentScreen.ImageScreen(imageObject, itemTitle)
//                        )
//                    },
                    stringId = currentScreen.titleStringId
                )
            }
            entry<CurrentScreen.ImageScreen> { currentScreen ->
                AnImageScreen( // Single
                    imageObject = currentScreen.imageObject,
                    itemTitle = currentScreen.itemTitle,
                    onClickBackButton = { collectionsViewModel.pop() },
                    stringId = currentScreen.titleStringId
                )
            }

            entry<CurrentScreen.MainScreen> { currentScreen ->
                MainScreen(
                    currentScreenItems = currentScreenItems,
                    onClickActions = onClickActions,
                    screensEnabled = screensEnabled,
                    stringId = currentScreen.titleStringId
                )
            }

            entry<CurrentScreen.MapScreen> { currentScreen ->
                MapScreen(
                    onClickBackButton = { collectionsViewModel.pop() },
                    mapMarker = currentScreen.mapMarker,
                    stringId = currentScreen.titleStringId
                )
            }

            entry<CurrentScreen.ObjectsScreen>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        ContentPlaceholder(
                            drawableId =
                                R.drawable.outline_emoji_objects_24,
                            stringId =
                                R.string.details_placeholder_collection
                        )
                    }
                )
            ) { currentScreen ->
                CommonQueryScreen(
                    drawableId = currentScreen.drawableId,
                    entries = MediaObject.entries,
                    onClickBackButton = { collectionsViewModel.pop() },
                    onClickNewSearch = {
                        collectionsViewModel.removeIfNot(
                            CurrentScreen.ObjectsScreen
                        )
                    },
                    onClickSearchResult = { result ->
                        collectionsViewModel.put(
                            CurrentScreen.ResultDetails(
                                result, SearchType.MediaObject
                            )
                        )
                    },
                    onLongClickSearchResult = { mapMarker ->
                        collectionsViewModel.put(
                            CurrentScreen.MapScreen(mapMarker)
                        )
                    },
                    searchType = SearchType.MediaObject,
                    stringId = currentScreen.titleStringId,
                    windowWidthSize = windowWidthSize
                )
            }

            entry<CurrentScreen.ResultDetails>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { currentScreen ->
                currentScreen.searchResult?.let { result ->
                    ResultDetailsScreen(
                        isSearchVisible = isSearchVisible,
                        onClickBackButton = { collectionsViewModel.pop() },
                        onClickHrefItem = { hrefsList, index ->
                            collectionsViewModel.put(
                                CurrentScreen.HrefDetails(
                                    hrefsList, index
                                )
                            )
                        },
                        onClickImages = { imageObjects, itemTitle ->
                            collectionsViewModel.put(
                                CurrentScreen.ImagesScreen(
                                    imageObjects, itemTitle
                                )
                            )
                        },
                        onClickMapButton = { mapMarker ->
                            collectionsViewModel.put(
                                CurrentScreen.MapScreen(mapMarker)
                            )
                        },
                        onToggleSearch = onToggleSearch,
                        searchResult = result,
                        searchType = currentScreen.searchType,
                        stringId = currentScreen.titleStringId,
                        windowSize = windowWidthSize
                    )
                }
            }

            entry<CurrentScreen.SettingsScreen> { currentScreen ->
                SettingsScreen(
                    onClickBackButton = { collectionsViewModel.pop() },
                    stringId = currentScreen.titleStringId
                )
            }

            entry<CurrentScreen.SpecimensScreen>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        ContentPlaceholder(
                            drawableId =
                                R.drawable.icons_lib_turtle_24,
                            stringId =
                                R.string.details_placeholder_collection
                        )
                    }
                )
            ) { currentScreen ->
                CommonQueryScreen(
                    drawableId = currentScreen.drawableId,
                    entries = MediaSpecimen.entries,
                    onClickBackButton = { collectionsViewModel.home() },
                    onClickNewSearch = {
                        collectionsViewModel.removeIfNot(
                            CurrentScreen.SpecimensScreen
                        )
                    },
                    onClickSearchResult = { result ->
                        collectionsViewModel.put(
                            CurrentScreen.ResultDetails(
                                result, SearchType.MediaSpecimen
                            )
                        )
                    },
                    onLongClickSearchResult = { mapMarker ->
                        collectionsViewModel.put(
                            CurrentScreen.MapScreen(mapMarker)
                        )
                    },
                    searchType = SearchType.MediaSpecimen,
                    stringId = currentScreen.titleStringId,
                    windowWidthSize = windowWidthSize
                )
            }
        }
    )
    // android.util.Log.d("HEY",collectionsViewModel.backStack.size.toString())
}