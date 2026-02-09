package com.michaelrmossman.collections.ui.common

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.ui.favourites.FavesViewModel
import com.michaelrmossman.collections.util.TextUtils.getCollectionForFave
import com.michaelrmossman.collections.util.TextUtils.getSubtitle1Text
import com.michaelrmossman.collections.util.TextUtils.getSubtitle2Text
import com.michaelrmossman.collections.util.fromHtml

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleFaveBottomSheet(
    result: SearchResult,
    onDismissRequest: () -> Unit,
    searchType: SearchType,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val headerText = stringResource(R.string.menu_toggle_fave)
    val iconLargePadding = dimensionResource(R.dimen.padding_great)
    val iconSize = dimensionResource(R.dimen.icon_size_small)
    val sheetState = rememberModalBottomSheetState()
    val horizontalPadding = dimensionResource(R.dimen.padding_medium)
    var isFavourite by remember { mutableStateOf<Boolean?>(null) }
    val textVerticalPadding = dimensionResource(R.dimen.padding_small)
    val rowVerticalPadding = dimensionResource(R.dimen.padding_small)
    var toggleFaveResult by remember { mutableIntStateOf(0) }
    var toggleFavourite by remember { mutableStateOf(false) }
    val verticalSpacing = dimensionResource(R.dimen.spacing_vertical_small)
    val viewModel: FavesViewModel = viewModel(factory = FavesViewModel.Factory)

    LaunchedEffect(key1 = Unit) {
        isFavourite = viewModel.isFavourite(
            itemId = result.id,
            itemType = result.media
        )
    }

    LaunchedEffect(key1 = toggleFaveResult) {
        if (toggleFaveResult != 0) {
            isFavourite?.let { favourite ->
                val stringId = when (toggleFaveResult) {
                    -1 -> R.string.faves_error
                    else -> when (favourite) {
                        true -> R.string.fave_removed
                        else -> R.string.fave_added
                    }
                }
                Toast.makeText(
                    context,
                    stringId,
                    when (toggleFaveResult) {
                        -1 -> Toast.LENGTH_LONG
                        else -> Toast.LENGTH_SHORT
                    }
                ).show()

                when (toggleFaveResult) {
                    -1 -> toggleFavourite = false
                    /* Dismiss the bottomSheet upon
                       successful add / rem fave */
                    else -> onDismissRequest()
                }
            }
        }
    }

    LaunchedEffect(key1 = toggleFavourite) {
        if (toggleFavourite) {
            val latitude: Double
            val locationTitle: String?
            val longitude: Double
            when (result.media) {
                MediaType.Place -> {
                    with (result as SearchResult.Place) {
                        latitude = geoLocation.lat
                        locationTitle = prefLabel
                        longitude = geoLocation.lon
                    }
                }
                else -> {
                    latitude = 0.0
                    locationTitle = null
                    longitude = 0.0
                }
            }
            toggleFaveResult = viewModel.toggleFavourite(
                collection = getCollectionForFave(
                    result = result,
                    searchType = searchType
                ),
                href = result.href,
                itemId = result.id,
                itemType = result.media,
                searchType = searchType,
                subtitle1 = getSubtitle1Text(
                    result = result,
                    searchType = searchType
                ),
                subtitle2 = getSubtitle2Text(
                    result = result,
                    searchType = searchType
                ),
                title = result.title,
                /* These three only for MediaType.Place */
                latitude = latitude,
                locationTitle = locationTitle,
                longitude = longitude
            )
        }
    }

    isFavourite?.let { favourite ->
        ModalBottomSheet(
            onDismissRequest = { onDismissRequest() },
            sheetState = sheetState
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(verticalSpacing),
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    )
            ) {
                Row(
                    modifier = Modifier.padding(
                        vertical = rowVerticalPadding
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        headerText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(
                                horizontal = horizontalPadding
                            )
                            .weight(1F)
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(horizontal = iconLargePadding)
                            .size(iconSize),
                        onClick = { onDismissRequest() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(
                                R.string.bottom_sheet_dismiss
                            )
                        )
                    }
                }
                Column(
                    /* Width of Text will define width of Button */
                    modifier = Modifier.width(IntrinsicSize.Max)
                ) {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = horizontalPadding,
                            vertical = textVerticalPadding
                        ),
                        text = stringResource(
                            when (isFavourite) {
                                true -> R.string.fave_is_favourite_message
                                else -> R.string.fave_not_favourite_message
                            },
                            result.title
                        ).fromHtml() // Note html
                    )
                    Button(
                        modifier = Modifier.padding(
                            horizontal = horizontalPadding,
                            vertical = rowVerticalPadding
                        ),
                        onClick = { toggleFavourite = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(when (isFavourite) {
                                    true -> {
                                        R.drawable.baseline_bookmark_remove_24
                                    }
                                    else -> {
                                        R.drawable.baseline_bookmark_add_24
                                    }
                                }),
                                modifier = modifier.padding(
                                    horizontal = dimensionResource(
                                        R.dimen.padding_medium
                                    )
                                ),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.weight(0.2F))
                            Text(text = stringResource(when(isFavourite) {
                                true -> R.string.faves_remove_desc
                                else -> R.string.faves_add_desc
                            }))
                            Spacer(modifier = Modifier.weight(0.8F))
                        }
                    }
                }
            }
        }
    }
}