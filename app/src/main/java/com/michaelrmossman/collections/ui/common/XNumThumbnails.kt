package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.pluralStringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.util.fromHtml

@Suppress("KotlinConstantConditions") /* isDownloading | isError */
@Composable
fun XNumThumbnails(
    onClickImages: (List<ImageObject>, String) -> Unit,
    results: List<SearchResult>,
    title: String, /* e.g. SearchResult Title */
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    /* Variable, depending on screen width (3, 5, or 7). Refer integers */
    val countImages = integerResource(R.integer.thumbnails_to_display)
    val imagesCount = results.size
    val imagePaddingHorizontal = dimensionResource(R.dimen.padding_small)
    val imagePaddingVertical = dimensionResource(R.dimen.padding_mini)
    val maxGridHeight = dimensionResource(R.dimen.thumbnail_grid_max_height)
    val roundedCornerShape = dimensionResource(R.dimen.card_corner_shape)

    val validImageObjects = mutableListOf<ImageObject>()
    /* Just preview UP TO max [countImages] thumbnails */
    for (i in 0 until minOf(countImages, imagesCount)) {
        validImageObjects.add(results[i] as ImageObject)
    }

    if (validImageObjects.isNotEmpty()) {
        val imageObjectsText = pluralStringResource(
            R.plurals.image_objects,
            imagesCount,
            imagesCount
        ).fromHtml()
        var isDownloading by remember { mutableStateOf(true) }
        var isError by remember { mutableStateOf(false) }

        Text(
            text = imageObjectsText,
            modifier = modifier
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(count = countImages),
            content = {
                itemsIndexed(
                    items = validImageObjects
                ) { _, imageObject ->

                    when (
                        isError
                        &&
                        !isDownloading
                    ) {
                        false -> AsyncImage(
                            contentDescription = imageObject.title,
                            model = ImageRequest.Builder(
                                context = context
                            )
                            .data(imageObject.thumbnailUrl)
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
                                .clickable {
                                    val imageObjects = results.map { result ->
                                        result as ImageObject
                                    }
                                    onClickImages(imageObjects, title)
                                }
                                .clip(
                                    RoundedCornerShape(
                                        size = roundedCornerShape
                                    )
                                )
                                .padding(
                                    horizontal = imagePaddingHorizontal,
                                    vertical = imagePaddingVertical
                                )
                        )
                        else -> RetryThumbnail(
                            isDownloading = isDownloading,
                            isError = isError,
                            modifier = Modifier.fillMaxWidth(),
                            onDownloadClick = {
                                isDownloading = true
                                isError = false
                            },
                            stringId = R.string.image_retry
                        )
                    }
                }
            },
            modifier = Modifier.heightIn(
                /* Required due to nested scroll */
                max = maxGridHeight
            )
        )
    }
}