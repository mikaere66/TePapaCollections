package com.michaelrmossman.collections.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R

/* Used on appBar in [HrefDetailsScreen] */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadStatus(
    isDownloading: Boolean,
    isError: Boolean,
    isForbidden: Boolean,
    isNotFound: Boolean,
    onClickDownloadButton: () -> Unit,
    modifier: Modifier = Modifier
) {

    AnimatedVisibility(
        visible = isDownloading
    ) {
        CircularProgressIndicator(
            modifier = modifier
                .padding(
                    dimensionResource(
                        R.dimen.padding_download_circular_progress
                    )
                )
                .size(
                    dimensionResource(R.dimen.icon_size_small)
                ),
            color = TopAppBarDefaults.topAppBarColors().actionIconContentColor,
            strokeWidth = dimensionResource(
                R.dimen.circular_progress_stroke_width
            )
        )
    }

    AnimatedVisibility(
        visible = (
            isError
            ||
            isForbidden
            ||
            isNotFound
        )
    ) {
        IconButton(
            enabled = (
                !isDownloading
                &&
                !isForbidden
                &&
                !isNotFound
            ),
            onClick = { onClickDownloadButton() }
        ) {
            Icon(
                contentDescription = stringResource(
                    when (isError) {
                        true -> R.string.loading_retry
                        else -> R.string.loading_disabled
                    }
                ),
                painter = painterResource(
                    when (isError) {
                        true -> R.drawable.outline_download_24
                        else -> R.drawable.outline_download_off_24
                    }
                ),
                tint = Color.Red
            )
        }
    }
}