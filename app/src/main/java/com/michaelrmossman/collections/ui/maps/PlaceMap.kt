package com.michaelrmossman.collections.ui.maps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.michaelrmossman.collections.model.AMapMarker

/**
 * Shows a [GoogleMap] with single [AMapMarker] map marker
 */
@Composable
fun PlaceMap(
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit,
    satelliteView: Int?,
    content: @Composable @GoogleMapComposable () -> Unit
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = { onMapLoaded() },
        properties = MapProperties(
            mapType = when (satelliteView == 1) {
                /* Although referred to as SATELLITE within this app,
                   MapType.SATELLITE does NOT show place names, etc. */
                true -> MapType.HYBRID
                else -> MapType.NORMAL
            }
        )
    ) {
        content()
    }
}