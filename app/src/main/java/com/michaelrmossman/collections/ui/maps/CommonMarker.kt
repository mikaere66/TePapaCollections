package com.michaelrmossman.collections.ui.maps

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.util.BitmapParameters
import com.michaelrmossman.collections.util.IconColor
import com.michaelrmossman.collections.util.MAP_MARKER_BACKGROUND_ALPHA
import com.michaelrmossman.collections.util.vectorToBitmap

/**
 * Shows a single [AMapMarker] map marker
 */
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun CommonMarker(
    mapMarker: AMapMarker,
    onMarkerClick: (Marker) -> Boolean
) {
    val colors = IconColor(
        iconColor = MaterialTheme.colorScheme.onPrimary,
        backgroundColor = MaterialTheme.colorScheme.primary.copy(
            alpha = MAP_MARKER_BACKGROUND_ALPHA
        ),
        borderColor = MaterialTheme.colorScheme.primary
    )
    val placeIcon = vectorToBitmap(
        LocalContext.current,
        BitmapParameters(
            id = R.drawable.outline_place_24,
            iconColor = colors.iconColor.toArgb(),
            backgroundColor = colors.backgroundColor.toArgb()
        )
    )
    Marker(
        icon = placeIcon,
        state = rememberMarkerState(
            position = LatLng(
                mapMarker.lat,
                mapMarker.lon
            )
        ),
        title = mapMarker.title,
        snippet = mapMarker.snippet,
        onClick = { marker ->
            onMarkerClick(marker)
            false
        }
    )
}