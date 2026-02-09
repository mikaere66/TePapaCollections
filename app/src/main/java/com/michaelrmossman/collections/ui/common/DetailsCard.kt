package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.ImageObject

@Composable
fun DetailsCard(
    contentPadding: PaddingValues,
    isNestedContent: Boolean,
    isSearchVisible: Boolean,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    onClickImages: (List<ImageObject>, String) -> Unit,
    onClickMapButton: (AMapMarker) -> Unit,
    result: SearchResult,
    searchType: SearchType,
    windowSize: WindowWidthSizeClass,
    /* Modifier used by all [SearchResult]s */
    modifier: Modifier = Modifier
) {
    val cardCornerShape = dimensionResource(R.dimen.card_corner_shape)
    val cardElevation = dimensionResource(R.dimen.card_elevation)
    val cardHorizontalPadding = dimensionResource(
        R.dimen.card_horizontal_padding
    )
    val cardVerticalPadding = dimensionResource(R.dimen.padding_small)
    val columnHorizontalPadding = dimensionResource(R.dimen.padding_small)
    val columnVerticalPadding = dimensionResource(R.dimen.padding_small)
    val scrollState = rememberScrollState()
    val searchBoxPaddingBottom = contentPadding.calculateBottomPadding()
    val searchBoxPaddingTop = when (isSearchVisible) {
        true -> when (windowSize == WindowWidthSizeClass.Compact) {
            true -> 0.dp
            else -> contentPadding.calculateTopPadding()
        }
        else -> contentPadding.calculateTopPadding()
    }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = cardElevation
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = searchBoxPaddingBottom.plus(
                    cardVerticalPadding
                ),
                end = cardHorizontalPadding,
                start = cardHorizontalPadding,
                top = searchBoxPaddingTop.plus(
                    cardVerticalPadding
                )
            ),
        shape = RoundedCornerShape(size = cardCornerShape)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = columnHorizontalPadding,
                    vertical = columnVerticalPadding
                )
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_vertical_small)
            )
        ) {

            /* Also used by ListItemCard() */
            ListItemAll(
                fullText = true,
                result = result,
                searchType = searchType,
                modifier = modifier
            )

            when (result) {
                is SearchResult.Category -> {
                    DetailsCategory(
                        category = result,
                        modifier = modifier
                    )
                }
                is SearchResult.Collaboration -> {
                    DetailsCollaboration(
                        collaboration = result,
                        modifier = modifier
                    )
                }
                is SearchResult.Group -> {
                    DetailsGroup(
                        group = result,
                        modifier = modifier
                    )
                }
                is SearchResult.ImageObject -> {
                    DetailsImageObject(
                        imageObject = result,
                        modifier = modifier
                    )
                }
                is SearchResult.Object -> {
                    DetailsObject(
                        isNestedContent = isNestedContent,
                        `object` = result,
                        onClickHrefItem = onClickHrefItem,
                        onClickImages = onClickImages,
                        modifier = modifier
                    )
                }
                is SearchResult.Organisation -> {
                    DetailsOrganisation(
                        isNestedContent = isNestedContent,
                        onClickHrefItem = onClickHrefItem,
                        organisation = result,
                        modifier = modifier
                    )
                }
                is SearchResult.Person, is SearchResult.Position -> {
                    DetailsPerson(
                        isNestedContent = isNestedContent,
                        onClickHrefItem = onClickHrefItem,
                        person = result,
                        modifier = modifier
                    )
                }
                is SearchResult.Place -> {
                    DetailsPlace(
                        onClickMapButton = onClickMapButton,
                        place = result,
                        modifier = modifier
                    )
                }
                is SearchResult.Publication -> {
                    DetailsPublication(
                        isNestedContent = isNestedContent,
                        onClickHrefItem = onClickHrefItem,
                        onClickImages = onClickImages,
                        publication = result,
                        modifier = modifier
                    )
                }
                is SearchResult.Specimen -> {
                    DetailsSpecimen(
                        isNestedContent = isNestedContent,
                        onClickHrefItem = onClickHrefItem,
                        onClickImages = onClickImages,
                        onClickMapButton = onClickMapButton,
                        specimen = result,
                        modifier = modifier
                    )
                }
                is SearchResult.Taxon -> {
                    DetailsTaxon(
                        taxon = result,
                        modifier = modifier
                    )
                }
                is SearchResult.TextDigitalDocument -> {
                    DetailsTextDigitalDocument(
                        textDigitalDoc = result,
                        modifier = modifier
                    )
                }
                is SearchResult.Topic -> {
                    DetailsTopic(
                        isNestedContent = isNestedContent,
                        onClickHrefItem = onClickHrefItem,
                        onClickImages = onClickImages,
                        topic = result,
                        modifier = modifier
                    )
                }
            }
        }
    }
}