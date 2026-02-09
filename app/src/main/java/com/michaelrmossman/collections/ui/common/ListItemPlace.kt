package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.Place
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.ListItemUtils.getNationsText
import com.michaelrmossman.collections.util.ListItemUtils.getGeoLocationText
import com.michaelrmossman.collections.util.TextUtils.getTextFromString

@Composable
fun ListItemPlace(
    place: Place,
    modifier: Modifier = Modifier
) {
    val nations = place.nation
    val nationsFlattened = place.nation.joinToString(
        ITEM_SEPARATOR
    )
    val nationsText = getNationsText(
        nations = nations,
        nationsFlattened = nationsFlattened
    )
//    getTextFromString(
//        stringId = R.string.place_nation,
//        string = place.nation.joinToString(
//            ITEM_SEPARATOR
//        )
//    )

    val geoLocationText = getGeoLocationText(
        latitude = place.geoLocation.lat,
        longitude = place.geoLocation.lon
    )
//    getTextFromString(
//        stringId = R.string.geo_location_available,
//        string = stringResource(when(
//            place.geoLocation.lat != 0.0
//            &&
//            place.geoLocation.lon != 0.0
//        ) {
//            true -> R.string.geo_location_yes
//            else -> R.string.geo_location_no
//        }
//    ))

    TypeIconWithTitle(
        result = place,
        modifier = modifier
    )
    Text(
        text = nationsText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.padding_mini)
        )
    )
    Text(
        text = geoLocationText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.padding_mini)
        )
    )
}