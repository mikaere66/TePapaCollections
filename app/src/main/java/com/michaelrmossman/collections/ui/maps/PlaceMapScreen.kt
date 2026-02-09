package com.michaelrmossman.collections.ui.maps

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.ui.components.TwoLineAppBar
import com.michaelrmossman.collections.util.MAP_ZOOM_IN_OUT_SINGLE_ITEM

/**
 * Shows a [GoogleMap] with single [AMapMarker] map marker
 */
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(
    mapMarker: AMapMarker,
    onClickBackButton: () -> Unit,
    @StringRes stringId: Int,
    onMarkerClick: (Marker) -> Boolean = { false }
) {
    val mapsViewModel: MapsViewModel = viewModel(
        factory = MapsViewModel.Factory
    )
    // android.util.Log.d("HEY","${ mapMarker.lat },${ mapMarker.lon }")

    val cameraPosition = LatLng(
        mapMarker.lat,
        mapMarker.lon
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            cameraPosition, MAP_ZOOM_IN_OUT_SINGLE_ITEM
        )
    }
    var isMapLoading by remember { mutableStateOf(true) }
    val satelliteView by mapsViewModel.satelliteView.observeAsState()
    var zoomEnabled by remember { mutableStateOf(false) }
    var zoomInOut by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = zoomInOut) {
        if (zoomInOut) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mapMarker.lat, mapMarker.lon
                    ), /* If zoom in/out clicked when ALREADY at default zoom,
                          provide feedback by moving map camera just a bit */
                    MAP_ZOOM_IN_OUT_SINGLE_ITEM.plus(0.1F)
                )
            )
            zoomInOut = false
        }
    }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    ZoomInOutButton(
                        isEnabled = zoomEnabled,
                        onClickZoomButton = { zoomInOut = true }
                    )
                },
                onClickBackButton = { onClickBackButton() },
                stringId = stringId,
                subtitle = mapMarker.title
            )
        }
    ) { contentPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            PlaceMap(
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    isMapLoading = false
                    zoomEnabled = true
                },
                satelliteView = satelliteView
            ) {
                CommonMarker(
                    mapMarker = mapMarker,
                    onMarkerClick = onMarkerClick
                )
            }

            if (isMapLoading) {
                MapLoadProgress(
                    isVisible = isMapLoading,
                    modifier = Modifier.matchParentSize()
                )
            }
        }
    }
}