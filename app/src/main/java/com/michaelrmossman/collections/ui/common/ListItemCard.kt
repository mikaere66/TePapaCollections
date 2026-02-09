package com.michaelrmossman.collections.ui.common

import android.widget.Toast
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.state.ResultsListState

@Composable
fun ListItemCard(
    onClickSearchResult: (SearchResult) -> Unit,
    onLongClickSearchResult: (AMapMarker) -> Unit,
    result: SearchResult,
    searchType: SearchType,
    /* Modifier used by all [SearchResult]s */
    modifier: Modifier = Modifier
) {
    val columnHorizontalPadding = dimensionResource(R.dimen.padding_medium)
    val columnVerticalPadding = dimensionResource(R.dimen.padding_small)
    val context = LocalContext.current
    var showToast by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = showToast) {
        if (showToast != 0) {
            val message = String.format(
                context.getString(when (showToast == 1) {
                    true -> R.string.toast_none_place
                    else -> R.string.toast_none_other
                }),
                when (showToast == 1) {
                    true -> (result as SearchResult.Place).prefLabel
                    else -> result.media.toString()
                }
            )
            showToast = 0
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.card_corner_shape)
        ),
        modifier = Modifier.combinedClickable(
            onClick = {
                onClickSearchResult(result)
            },
            onLongClick = {
                when (result is SearchResult.Place) {
                    true -> when (
                        result.geoLocation.lat != 0.0
                        &&
                        result.geoLocation.lon != 0.0
                    ) {
                        false -> showToast = 1 /* No coords for place */
                        else -> {
                            val mapMarker = AMapMarker(
                                lat = result.geoLocation.lat,
                                lon = result.geoLocation.lon,
                                snippet = result.nation[0],
                                title = result.prefLabel
                            )
                            onLongClickSearchResult(mapMarker)
                        }
                    }
                    else -> showToast = 2 /* No maps AT ALL for other */
                }
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(
                end = columnHorizontalPadding,
                start = columnHorizontalPadding,
                top = columnVerticalPadding
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_vertical_small)
            )
        ) {
            /* Also used by DetailsCard() */
            ListItemAll(
                fullText = false,
                result = result,
                searchType = searchType,
                /* Modifier used by all [SearchResult]s */
                modifier = modifier
            )
        }
    }
}