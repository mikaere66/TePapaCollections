package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.MediaObject
import com.michaelrmossman.collections.enum.MediaSpecimen
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.ui.components.TypeIcon
import com.michaelrmossman.collections.ui.theme.TePapaCollectionsTheme
import com.michaelrmossman.collections.util.IconUtils.getMediaForIconBySearchTypeAndMedia
import com.michaelrmossman.collections.util.IconUtils.getMediaIconId
import com.michaelrmossman.collections.util.TextUtils.getTitleText
import com.michaelrmossman.collections.util.capitalise
import com.michaelrmossman.collections.util.fromHtml
import com.michaelrmossman.collections.util.replaceAngleBrackets

@Composable
fun TypeIconWithTitle(
    result: SearchResult,
    modifier: Modifier = Modifier,
    angleBrackets: Boolean = false,
    capitalise: Boolean = false,
    searchType: SearchType? = null
) {
    val titleText = getTitleText(
        angleBrackets = angleBrackets,
        capitalise = capitalise,
        media = result.media,
        title = result.title
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.spacing_horizontal_midi)
        ),
        modifier = modifier
    ) {
        /* For the Object and Specimen types (any Collection),
           base their icon on the name of the collection =) */
        val collection = when (searchType) {
            SearchType.MediaObject -> {
                (result as SearchResult.Object).collection
            }
            SearchType.MediaSpecimen -> {
                (result as SearchResult.Specimen).collection
            }
            else -> when (result.media) {
                MediaType.Object -> {
                    (result as SearchResult.Object).collection
                }
                MediaType.Specimen -> {
                    (result as SearchResult.Specimen).collection
                }
                else -> String()
            }
        }
        TypeIcon(
            drawableId = getMediaIconId(
                getMediaForIconBySearchTypeAndMedia(
                    collection = collection,
                    media = result.media,
                    searchType = searchType
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
    }
}

@Preview(showBackground = true)
@Composable
fun TypeIconWithTitlePreview() {
    TePapaCollectionsTheme {
        TypeIconWithTitle(
            result = SearchResult.Object(
                title = "HQ Holden"
            ),
            searchType = SearchType.MediaObject
        )
    }
}