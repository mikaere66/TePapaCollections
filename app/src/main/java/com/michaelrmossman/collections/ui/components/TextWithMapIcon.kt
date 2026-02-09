package com.michaelrmossman.collections.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.AMapMarker

@Composable
fun TextWithMapIcon(
    mapMarker: AMapMarker,
    onClickMapButton: (AMapMarker) -> Unit,
    modifier: Modifier = Modifier
) {
    /* innerPadding is equivalent to medium.minus(small).dp */
    val innerPadding = dimensionResource(R.dimen.details_inner_padding)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(
                R.string.place_on_map
            ),
            modifier = modifier.padding(
                horizontal = innerPadding
            ).clickable {
                onClickMapButton(mapMarker)
            }
        )
        IconButton(
            onClick = {
                onClickMapButton(mapMarker)
            }
        ) {
            Icon(
                contentDescription = null,
                painter = painterResource(
                    R.drawable.outline_map_24
                )
            )
        }
    }
}