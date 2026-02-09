package com.michaelrmossman.collections.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.util.ModifierUtils.downloading

@Composable
fun RetryThumbnail(
    isDownloading: Boolean,
    isError: Boolean,
    /* In this case, modifier is NOT optional */
    modifier: Modifier, 
    onDownloadClick: () -> Unit,
    @StringRes stringId: Int
) {
    val downloadText = when (isDownloading) {
        true -> String()
        else -> stringResource(stringId)
    }

    Row(
        /* fillMaxWidth or fillMaxSize, depending on caller */
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            enabled = !isDownloading,
            onClick = onDownloadClick
        ) {
            Icon(
                contentDescription = null,
                painter = painterResource(
                    when (isDownloading) {
                        true -> R.drawable.loading_image
                        else -> R.drawable.outline_broken_image_24
                    }
                ),
                /* Refer custom modifier in ModifierUtils */
                modifier = Modifier.downloading(isDownloading),
                tint = when (isError) {
                    true -> Color.Red
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
        Text(
            text = downloadText,
            modifier = Modifier.clickable {
                onDownloadClick()
            }
        )
    }
}