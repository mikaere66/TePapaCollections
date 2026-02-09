package com.michaelrmossman.collections.ui.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.data.FaveEntity
import com.michaelrmossman.collections.enum.SortFavesBy
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.ui.common.EmptyFaves
import com.michaelrmossman.collections.ui.components.SingleActionMenu
import com.michaelrmossman.collections.ui.components.SortByActionMenu
import com.michaelrmossman.collections.ui.components.TwoLineAppBar
import com.michaelrmossman.collections.util.DialogUtils.ConfirmDeleteAllFavesDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavesListScreen(
    // @DrawableRes drawableId: Int,
    onClickBackButton: () -> Unit,
    onClickFavourite: (Int, List<FaveEntity>) -> Unit,
    onLongClickFavourite: (AMapMarker) -> Unit,
    // @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val viewModel: FavesViewModel = viewModel(factory = FavesViewModel.Factory)
    val favesSortedBy = viewModel.favesSortedBy.observeAsState(initial = 0)
    val favourites by viewModel.favourites.observeAsState(initial = emptyList())
    var showRemoveAllDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    SortByActionMenu(
                        isEnabled = favourites.size > 1,
                        onSortByDateClick = { viewModel.setFavesSortedBy(
                            SortFavesBy.Date
                        )},
                        onSortByNameClick = { viewModel.setFavesSortedBy(
                            SortFavesBy.Name
                        )},
                        onSortByTypeClick = { viewModel.setFavesSortedBy(
                            SortFavesBy.Type
                        )},
                        sortedBy = SortFavesBy.entries[favesSortedBy.value]
                    )
                    SingleActionMenu(
                        onSingleItemClick = { showRemoveAllDialog = true },
                        isEnabled = favourites.isNotEmpty(),
                        itemStringId = R.string.menu_faves_delete_all
                    )
                },
                onClickBackButton = onClickBackButton,
                stringId = R.string.app_name,
                subtitle = stringResource(
                    R.string.faves_subtitle,
                    favourites.size
                )
            )
        }
    ) { contentPadding ->

        when (favourites.isEmpty()) {
            true -> EmptyFaves(
                drawableId = R.drawable.outline_heart_broken_24,
                modifier = Modifier.fillMaxSize()
            )
            else -> FavesList(
                contentPadding = contentPadding,
                favourites = favourites,
                modifier = modifier.padding(
                    start = dimensionResource(R.dimen.padding_medium),
                    end = dimensionResource(R.dimen.padding_medium)
                ),
                onClickFavourite = onClickFavourite,
                onLongClickFavourite = onLongClickFavourite,
                onClickToggleFavourite = { favourite ->
                    viewModel.deleteFave(favourite)
                }
            )
        }

        if (showRemoveAllDialog) {
            ConfirmDeleteAllFavesDialog(
                onClickConfirm = {
                    showRemoveAllDialog = false
                    viewModel.deleteAllFavourites()
                    /* Quit on remove all faves */
                    onClickBackButton()
                },
                onClickDismiss = { showRemoveAllDialog = false }
            )
        }
    }
}

@Composable
fun FavesList(
    contentPadding: PaddingValues,
    favourites: List<FaveEntity>,
    onClickFavourite: (Int, List<FaveEntity>) -> Unit,
    onLongClickFavourite: (AMapMarker) -> Unit,
    onClickToggleFavourite: (FaveEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val columnVerticalPadding = dimensionResource(
        R.dimen.padding_small
    )
    val columnVerticalSpacing = dimensionResource(
        R.dimen.spacing_vertical_small
    )
    val lazyListState = rememberLazyListState()
    val listItemPadding = dimensionResource(R.dimen.list_item_padding)

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = columnVerticalPadding),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(
            columnVerticalSpacing
        )
    ) {
        itemsIndexed(
            items = favourites
        ) { index, fave ->

            ListItemFave(
                fave = fave,
                index = index,
                /* Modifier used by all [Text] composables */
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(listItemPadding),
                onClickFavourite = { faveIndex ->
                    onClickFavourite(faveIndex, favourites)
                },
                onLongClickFavourite = onLongClickFavourite,
                onClickToggleFavourite = {
                    onClickToggleFavourite(fave)
                }
            )
        }
    }
}