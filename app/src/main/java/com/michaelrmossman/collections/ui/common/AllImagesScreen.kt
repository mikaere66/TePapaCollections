package com.michaelrmossman.collections.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.ui.components.TwoLineAppBar

/* Multiple images */
@Composable
fun AllImagesScreen(
    imageObjects: List<ImageObject>,
    itemTitle: String,
    onClickBackButton: () -> Unit,
    onClickImage: (ImageObject, String) -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val additionalPadding = dimensionResource(R.dimen.padding_mini)
    val context = LocalContext.current
    val horizontalPadding = dimensionResource(R.dimen.padding_mini)
    var isDownloading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val paddingImageHorizontal = dimensionResource(R.dimen.padding_small)
    val paddingTextHorizontal = dimensionResource(R.dimen.padding_medium)
    val paddingVertical = dimensionResource(R.dimen.padding_small)
    val roundedCornerShape = dimensionResource(R.dimen.card_corner_shape)

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                },
                onClickBackButton = onClickBackButton,
                stringId = R.string.app_name,
                subtitle = stringResource(
                    stringId,
                    itemTitle
                )
            )
        }
    ) { contentPadding ->

        Box (
            modifier = Modifier.padding(
                bottom = contentPadding.calculateBottomPadding().plus(
                    additionalPadding
                ),
                end = horizontalPadding,
                start = horizontalPadding,
                top = contentPadding.calculateTopPadding().plus(
                    additionalPadding
                )
            )
            .fillMaxSize()
        ) {

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxHeight(),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.spacing_vertical_mini)
                )
            ) {
                if (
                    !isError
                    ||
                    isDownloading
                ) {
                    itemsIndexed(
                        items = imageObjects,
                    ) { index, imageObject ->
                        Card(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = dimensionResource(
                                    R.dimen.card_elevation
                                )
                            ),
                            onClick = { onClickImage(imageObject, itemTitle) },
                            shape = RoundedCornerShape(roundedCornerShape)
                        ) {
                            AsyncImage(
                                contentDescription = imageObject.title,
                                model = ImageRequest.Builder(
                                    context = context
                                )
                                .data(imageObject.previewUrl)
                                .crossfade(true)
                                .listener(
                                    onError = { _, _ ->
                                        isDownloading = false
                                        isError = true
                                    },
                                    onSuccess = { _, _ ->
                                        isDownloading = false
                                        isError = false
                                    }
                                )
                                .build(),
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            size = roundedCornerShape
                                        )
                                    )
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = paddingImageHorizontal,
                                        vertical = paddingVertical
                                    )
                            )
                            Text(
                                text = stringResource(
                                    R.string.image_label,
                                    index.plus(1),
                                    imageObjects.size,
                                    imageObject.title
                                ),
                                modifier = Modifier.padding(
                                    horizontal = paddingTextHorizontal,
                                    vertical = paddingVertical
                                )
                            )
                        }
                    }
                }

                if (isDownloading || isError) {
                    item(key = -1) {
                        AcquiringImage(
                            isDownloading = isDownloading,
                            isError = isError,
                            modifier = Modifier.fillMaxSize(),
                            onDownloadClick = {
                                isDownloading = true
                                isError = false
                            }, /* Note use of plural images */
                            stringId = R.string.images_retry
                        )
                    }
                }
            }
        }
    }
}