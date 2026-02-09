package com.michaelrmossman.collections.ui.maps

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ZoomOutMap
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.michaelrmossman.collections.R

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun ZoomInOutButton(
    isEnabled: Boolean,
    onClickZoomButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        enabled = isEnabled,
        modifier = modifier,
        onClick = {
            onClickZoomButton()
        }
    ) {
        Icon(
            Icons.Outlined.ZoomOutMap,
            contentDescription = stringResource(
                R.string.menu_zoom_in_out
            )
        )
    }
}