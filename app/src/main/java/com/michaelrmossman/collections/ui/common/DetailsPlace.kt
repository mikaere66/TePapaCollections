package com.michaelrmossman.collections.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.ui.components.TextWithMapIcon
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getTextFromString
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun DetailsPlace(
    onClickMapButton: (AMapMarker) -> Unit,
    place: SearchResult.Place,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | nation | (has) geoLocation */

    val undefinedString = stringResource(R.string.common_undefined)

    val alternativeTerms = place.alternativeTerms.joinToString(
        ITEM_SEPARATOR
    )
    val alternativeTermsText = pluralStringResource(
        R.plurals.terms_alternative,
        place.alternativeTerms.size,
        when (alternativeTerms.isBlank()) {
            true -> undefinedString
            else -> alternativeTerms
        }
    ).fromHtml()

    val broaderTerms = place.broaderTerms.map { broader ->
        broader.prefLabel
    }.sortedWith(
        String.CASE_INSENSITIVE_ORDER // Note sort
    ).joinToString(
        ITEM_SEPARATOR
    )
    val broaderTermsText = pluralStringResource(
        R.plurals.terms_broader,
        broaderTerms.split(ITEM_SEPARATOR).size,
        when (broaderTerms.isBlank()) {
            true -> undefinedString
            else -> broaderTerms
        }
    ).fromHtml()

    val narrowerTerms = place.narrowerTerms.map { narrower ->
        when (narrower is SearchResult.Place) {
            true -> narrower.prefLabel
            else -> narrower.title
        }
    }.sortedWith(
        String.CASE_INSENSITIVE_ORDER // Note sort
    ).joinToString(
        ITEM_SEPARATOR
    )
    val narrowerTermsText = pluralStringResource(
        R.plurals.terms_narrower,
        narrowerTerms.split(ITEM_SEPARATOR).size,
        when (narrowerTerms.isBlank()) {
            true -> undefinedString
            else -> narrowerTerms
        }
    ).fromHtml()

    val creditLineText = getTextFromString(
        stringId = R.string.credit_line,
        string = place.creditLine
    )

    /* = | = | = | = | = | = | = | = */

    Text(
        text = alternativeTermsText,
        modifier = modifier
    )
    Text(
        text = broaderTermsText,
        modifier = modifier
    )
    Text(
        text = narrowerTermsText,
        modifier = modifier
    )
    if (
        place.geoLocation.lat != 0.0
        &&
        place.geoLocation.lon != 0.0
    ) {
        val markerSnippet = when (place.nation.isEmpty()) {
            true -> stringResource(R.string.common_undefined)
            else -> place.nation[0]
        }
        val mapMarker = AMapMarker(
            lat = place.geoLocation.lat,
            lon = place.geoLocation.lon,
            snippet = markerSnippet,
            title = place.prefLabel
        )
        TextWithMapIcon(
            mapMarker = mapMarker,
            onClickMapButton = onClickMapButton
        )
    }
    Text(
        text = creditLineText,
        modifier = modifier
    )
    MetaDataFooter(
        result = place
    )
}