package com.michaelrmossman.collections.ui.favourites

import android.widget.Toast
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.data.FaveEntity
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.MediaObject
import com.michaelrmossman.collections.enum.MediaSpecimen
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.model.ApiScore
import com.michaelrmossman.collections.model.MetaDataResult
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.Category
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.ui.components.TypeIcon
import com.michaelrmossman.collections.ui.theme.TePapaCollectionsTheme
import com.michaelrmossman.collections.util.IconUtils.getMediaForIconBySearchTypeAndMedia
import com.michaelrmossman.collections.util.IconUtils.getMediaIconId
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.RECORD_SEPARATOR
import com.michaelrmossman.collections.util.ListItemUtils.getAssociatedText
import com.michaelrmossman.collections.util.ListItemUtils.getAuthorsText
import com.michaelrmossman.collections.util.ListItemUtils.getBasisOfRecordText
import com.michaelrmossman.collections.util.ListItemUtils.getBirthDateText
import com.michaelrmossman.collections.util.ListItemUtils.getBroaderTermsList
import com.michaelrmossman.collections.util.ListItemUtils.getBroaderTermsText
import com.michaelrmossman.collections.util.ListItemUtils.getCategoriesText
import com.michaelrmossman.collections.util.ListItemUtils.getCollectionText
import com.michaelrmossman.collections.util.ListItemUtils.getCollectionsText
import com.michaelrmossman.collections.util.ListItemUtils.getEstablishedDateText
import com.michaelrmossman.collections.util.ListItemUtils.getFileFormatText
import com.michaelrmossman.collections.util.ListItemUtils.getGeoLocationText
import com.michaelrmossman.collections.util.ListItemUtils.getKingdomText
import com.michaelrmossman.collections.util.ListItemUtils.getNarrativeSummaryText
import com.michaelrmossman.collections.util.ListItemUtils.getNationalitiesText
import com.michaelrmossman.collections.util.ListItemUtils.getNationsText
import com.michaelrmossman.collections.util.ListItemUtils.getPermissionsText
import com.michaelrmossman.collections.util.ListItemUtils.getRelatedTermsList
import com.michaelrmossman.collections.util.ListItemUtils.getRelatedTermsText
import com.michaelrmossman.collections.util.TextUtils.getTextFromString
import com.michaelrmossman.collections.util.TextUtils.getTitleText
import com.michaelrmossman.collections.util.capitalise
import com.michaelrmossman.collections.util.fromHtml
import com.michaelrmossman.collections.util.parseMillisToKiwiDate
import com.michaelrmossman.collections.util.replaceAngleBrackets

@Composable
fun ListItemFave(
    fave: FaveEntity,
    index: Int,
    onClickToggleFavourite: () -> Unit,
    onClickFavourite: (Int) -> Unit,
    onLongClickFavourite: (AMapMarker) -> Unit,
    /* Modifier used by all [Text] composables */
    modifier: Modifier = Modifier
) {
    val columnHorizontalPadding = dimensionResource(R.dimen.padding_medium)
    val columnVerticalPadding = dimensionResource(R.dimen.padding_mini)
    val columnVerticalSpacing = dimensionResource(
        R.dimen.spacing_vertical_mini
    )
    val context = LocalContext.current
    /* Only relevant to place.nation for snippet */
    var nationsFlattened: String? = null
    /* Only relevant to topic.narrativeSummary */
    val summaryMaxLines = integerResource(
        id = R.integer.narrative_summary_max_lines
    )
    var showToast by remember { mutableIntStateOf(0) }

    val searchType = SearchType.valueOf(fave.searchType)
    val media = when (searchType) {
        SearchType.MediaObject -> {
            MediaObject.valueOf(fave.media)
        }
        SearchType.MediaSpecimen -> {
            MediaSpecimen.valueOf(fave.media)
        }
        SearchType.MediaType -> {
            MediaType.valueOf(fave.media)
        }
    }

    val titleText = getTitleText(
        angleBrackets = (
            media == MediaType.Category
        ),
        capitalise = (
            media == MediaType.Category
        ),
        media = media,
        title = fave.title
    )
    val subtitle1Text = when (searchType) {
        SearchType.MediaObject -> getCollectionText(
            collectionLabel = fave.subtitle1
        )
        SearchType.MediaSpecimen -> getCollectionText(
            collectionLabel = fave.subtitle1
        )
        SearchType.MediaType -> when (media) {
            MediaType.Category -> {
                val relatedTerms = fave.subtitle1.split(
                    RECORD_SEPARATOR
                )
                val relatedTermsFlattened = relatedTerms.joinToString(
                    ITEM_SEPARATOR
                )
                getRelatedTermsText(
                    relatedTerms = relatedTerms,
                    relatedTermsFlattened = relatedTermsFlattened
                )
            }
            MediaType.ImageObject -> getFileFormatText(
                fileFormat = fave.subtitle1
            )
            MediaType.Object -> getCollectionText(
                collectionLabel = fave.subtitle1
            )
            MediaType.Organisation -> {
                val associatedParties = fave.subtitle1.split(
                    RECORD_SEPARATOR
                )
                val associatedFlattened = associatedParties.joinToString(
                    ITEM_SEPARATOR
                )
                getAssociatedText(
                    associatedParties = associatedParties,
                    associatedFlattened = associatedFlattened
                )
            }
            MediaType.Person, MediaType.Position -> getBirthDateText(
                birthDate = fave.subtitle1
            )
            MediaType.Place -> {
                val nations = fave.subtitle1.split(
                    RECORD_SEPARATOR
                )
                nationsFlattened = nations.joinToString(
                    ITEM_SEPARATOR
                )
                getNationsText(
                    nations = nations,
                    nationsFlattened = nationsFlattened
                )
            }
            MediaType.Publication -> {
                val authors = fave.subtitle1.split(
                    RECORD_SEPARATOR
                )
                val authorsFlattened = authors.joinToString(
                    ITEM_SEPARATOR
                )
                getAuthorsText(
                    authors = authors,
                    authorsFlattened = authorsFlattened
                )
            }
            MediaType.Specimen -> getCollectionText(
                collectionLabel = fave.subtitle1
            )
            MediaType.Taxon -> getKingdomText(
                kingdom = fave.subtitle1
            )
            MediaType.TextDigitalDocument -> getFileFormatText(
                fileFormat = fave.subtitle1
            )
            MediaType.Topic -> {
                val collectionLabels = fave.subtitle1.split(
                    RECORD_SEPARATOR
                )
                val collectionLabelsFlattened = collectionLabels.joinToString(
                    ITEM_SEPARATOR
                )
                getCollectionsText(
                    collectionLabels = collectionLabels,
                    collectionLabelsFlattened = collectionLabelsFlattened
                )
            }
            /* Collaboration and Group */
            else -> buildAnnotatedString {
                append(stringResource(R.string.no_further_info))
            }
        }
    }
    val subtitle2Text = when (searchType) {
        SearchType.MediaObject -> {
            val categories = fave.subtitle2.split(
                RECORD_SEPARATOR
            ).map { category ->
                category.capitalise()
            }
            val categoriesFlattened = categories.joinToString(
                ITEM_SEPARATOR
            )
            getCategoriesText(
                categories = categories,
                categoriesFlattened = categoriesFlattened
            )
        }
        SearchType.MediaSpecimen -> getBasisOfRecordText(
            basisOfRecord = fave.subtitle2
        )
        SearchType.MediaType -> when (media) {
            MediaType.Category -> {
                val broaderTerms = fave.subtitle2.split(
                    RECORD_SEPARATOR
                )
                val broaderTermsFlattened = broaderTerms.joinToString(
                    ITEM_SEPARATOR
                )
                getBroaderTermsText(
                    broaderTerms = broaderTerms,
                    broaderTermsFlattened = broaderTermsFlattened
                )
            }
            MediaType.ImageObject -> {
                val permissions = fave.subtitle2.split(
                    RECORD_SEPARATOR
                )
                val permissionsFlattened = permissions.joinToString(
                    ITEM_SEPARATOR
                )
                getPermissionsText(
                    permissions = permissions,
                    permissionsFlattened = permissionsFlattened
                )
            }
            MediaType.Object -> {
                val categories = fave.subtitle2.split(
                    RECORD_SEPARATOR
                ).map { category ->
                    category.capitalise()
                }
                val categoriesFlattened = categories.joinToString(
                    ITEM_SEPARATOR
                )
                getCategoriesText(
                    categories = categories,
                    categoriesFlattened = categoriesFlattened
                )
            }
            MediaType.Organisation -> getEstablishedDateText(
                establishedDate = fave.subtitle2
            )
            MediaType.Person, MediaType.Position -> {
                val nationalities = fave.subtitle2.split(
                    RECORD_SEPARATOR
                )
                val nationalitiesFlattened = nationalities.joinToString(
                    ITEM_SEPARATOR
                )
                getNationalitiesText(
                    nationalities = nationalities,
                    nationalitiesFlattened = nationalitiesFlattened
                )
            }
            MediaType.Place -> getGeoLocationText(
                latitude = fave.latitude,
                longitude = fave.longitude
            )
            MediaType.Publication -> {
                val collectionLabels = fave.subtitle2.split(
                    RECORD_SEPARATOR
                )
                val collectionLabelsFlattened = collectionLabels.joinToString(
                    ITEM_SEPARATOR
                )
                getCollectionsText(
                    collectionLabels = collectionLabels,
                    collectionLabelsFlattened = collectionLabelsFlattened
                )
            }
            MediaType.Specimen -> getBasisOfRecordText(
                basisOfRecord = fave.subtitle2
            )
            MediaType.Taxon -> getBasisOfRecordText(
                basisOfRecord = fave.subtitle2
            )
            MediaType.TextDigitalDocument -> {
                val permissions = fave.subtitle2.split(
                    RECORD_SEPARATOR
                )
                val permissionsFlattened = permissions.joinToString(
                    ITEM_SEPARATOR
                )
                getPermissionsText(
                    permissions = permissions,
                    permissionsFlattened = permissionsFlattened
                )
            }
            MediaType.Topic -> getNarrativeSummaryText(
                narrativeSummary = fave.subtitle2
            )
            /* Collaboration and Group not found yet */
            else -> buildAnnotatedString { append(String()) }
        }
    }

    LaunchedEffect(key1 = showToast) {
        if (showToast != 0) {
            val message = String.format(
                context.getString(when (showToast == 1) {
                    true -> R.string.toast_none_place
                    else -> R.string.toast_none_other
                }),
                fave.title
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
                onClickFavourite(index)
            },
            onLongClick = {
                when (media == MediaType.Place) {
                    true -> when (
                        fave.latitude != 0.0
                        &&
                        fave.longitude != 0.0
                    ) {
                        false -> showToast = 1 /* No coords for place */
                        else -> {
                            val mapMarker = AMapMarker(
                                lat = fave.latitude,
                                lon = fave.longitude,
                                snippet = nationsFlattened ?: String(),
                                title = fave.locationTitle ?: String()
                            )
                            onLongClickFavourite(mapMarker)
                        }
                    }
                    else -> showToast = 2 /* No maps AT ALL for other */
                }
            }
        )
    ) {

        Column(
            modifier = Modifier.padding(
                bottom = columnVerticalPadding,
                end = columnHorizontalPadding,
                start = columnHorizontalPadding
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                columnVerticalSpacing
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.spacing_horizontal_midi)
                ),
                modifier = modifier
            ) {
                TypeIcon(
                    drawableId = getMediaIconId(
                        getMediaForIconBySearchTypeAndMedia(
                            collection = when (
                                SearchType.valueOf(fave.searchType)
                            ) {
                                SearchType.MediaObject -> {
                                    fave.collection
                                }
                                SearchType.MediaSpecimen -> {
                                    fave.collection
                                }
                                else -> String()
                            },
                            media = media,
                            searchType = SearchType.valueOf(
                                fave.searchType
                            )
                        )
                    )
                )
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = modifier.weight(1F)
                )
                FaveIcon(
                    isFave = true,
                    onClickToggleFavourite = onClickToggleFavourite
                )
            }
            Text(
                text = subtitle1Text,
                modifier = modifier
            )
            Text(
                text = subtitle2Text,
                modifier = modifier
            )
            if (fave.added != 0L) {
                Text(
                    text = getTextFromString(
                        stringId = R.string.faves_added,
                        string = fave.added.parseMillisToKiwiDate()
                    ),
                    modifier = modifier
                )
            }
        }
    }
}