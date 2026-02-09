package com.michaelrmossman.collections.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.util.ModifierUtils.downloading

@Suppress("KotlinConstantConditions") /* isDownloading | isError */
@Composable
fun AcquiringImage(
    isDownloading: Boolean,
    isError: Boolean,
    onDownloadClick: () -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isDownloading) {
            Image(
                contentDescription = stringResource(
                    R.string.loading_anim
                ),
                painter = painterResource(
                    R.drawable.loading_image
                ),
                modifier = Modifier
                    .size(
                        dimensionResource(
                            R.dimen.loading_anim_size
                        )
                    )
                    /* Refer custom modifier in ModifierUtils */
                    .downloading(isDownloading = true)
            )
            Text(
                text = stringResource(R.string.loading_anim),
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.padding_large)
                )
            )

        } else if (isError) {

            RetryThumbnail(
                isDownloading = isDownloading,
                isError = isError,
                modifier = modifier,
                onDownloadClick = onDownloadClick,
                stringId = stringId
            )
        }
    }
}