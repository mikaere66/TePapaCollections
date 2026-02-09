package com.michaelrmossman.collections.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterNone
import androidx.compose.material.icons.outlined.HistoryToggleOff
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.VpnKeyOff
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.ui.components.ButtonWithIcon
import com.michaelrmossman.collections.ui.components.MessageWithIcon
import com.michaelrmossman.collections.ui.theme.TePapaCollectionsTheme
import com.michaelrmossman.collections.util.ModifierUtils.downloading
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun EmptyFaves(
    @DrawableRes drawableId: Int,
    modifier: Modifier = Modifier
) {
    val color = colorResource(R.color.empty_list)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(drawableId),
            contentDescription = null,
            modifier = Modifier.size(
                dimensionResource(R.dimen.common_screens_icon_size)
            ),
            colorFilter = ColorFilter.tint(
                color, blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = stringResource(R.string.favourites_empty),
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_mega)
            )
        )
    }
}

@Composable
fun EmptyFilterScreen(
    canDownload: Boolean,
    mediaType: Media?,
    onClickDownloadMore: () -> Unit,
    query: String,
    modifier: Modifier = Modifier
) {
    /* Shouldn't be able to get to this point if
       mediaType is null, but just for safety */
    mediaType?.let { type ->
        val color = colorResource(R.color.empty_list)
        val message1 = stringResource(
            R.string.empty_filter_1,
            query,
            type.toString(),
            when (canDownload) {
                true -> String()
                else -> stringResource(
                    R.string.empty_filter_0 /* "or" */
                )
            },
            when (canDownload) {
                true -> ","
                else -> String()
            }
        )

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Outlined.FilterNone,
                contentDescription = null,
                modifier = Modifier.size(
                    dimensionResource(R.dimen.common_screens_icon_size)
                ),
                colorFilter = ColorFilter.tint(
                    color, blendMode = BlendMode.SrcIn
                )
            )
            Text(
                text = message1.fromHtml(),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.padding_mega)
                )
            )

            if (canDownload) {
                var downloading by remember { mutableStateOf(false) }
                val message2 = stringResource(R.string.empty_filter_2)
                Column(
                    /* Width of Text will define width of Button */
                    modifier = Modifier.width(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = message2.fromHtml(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            dimensionResource(R.dimen.padding_mini)
                        )
                    )
                    ButtonWithIcon(
                        drawableId = when (downloading) {
                            true -> R.drawable.outline_downloading_24
                            else -> R.drawable.outline_download_24
                        },
                        isEnabled = !downloading,
                        stringId = when (downloading) {
                            true -> R.string.loading_anim
                            else -> R.string.download_more
                        },
                        /* Refer custom modifier in ModifierUtils */
                        modifier = Modifier.downloading(
                            isDownloading = downloading
                        ),
                        onClickButton = {
                            downloading = true
                            onClickDownloadMore()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptySearchScreen(
    mediaType: Media?,
    onClickRemoveFilter: () -> Unit,
    query: String,
    modifier: Modifier = Modifier
) {
    val color = colorResource(R.color.empty_list)
    val message1 = stringResource(
        R.string.empty_list,
        query
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(
                dimensionResource(R.dimen.common_screens_icon_size)
            ),
            colorFilter = ColorFilter.tint(
                color, blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = message1.fromHtml(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_mega)
            )
        )
        /* If "search by" was used, offer to retry WITHOUT filter */
        mediaType?.let { type ->
            Column(
                /* Width of Text will define width of Button */
                modifier = Modifier.width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val message2 = stringResource(
                    R.string.empty_message,
                    type.toString()
                )
                Text(
                    text = message2.fromHtml(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        vertical = dimensionResource(
                            R.dimen.padding_great
                        )
                    )
                )
                ButtonWithIcon(
                    drawableId =
                        R.drawable.outline_filter_list_off_24,
                    stringId = R.string.filter_off,
                    onClickButton = onClickRemoveFilter
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_connection_error_100),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_large)
            )
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.loading_retry))
        }
    }
}

@Composable
fun ForbiddenScreen(
    modifier: Modifier = Modifier
) {
    val color = colorResource(R.color.empty_list)
    val message = stringResource(
        R.string.forbidden_api_key
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.VpnKeyOff,
            contentDescription = null,
            modifier = Modifier.size(
                dimensionResource(R.dimen.common_screens_icon_size)
            ),
            colorFilter = ColorFilter.tint(
                color, blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = message.fromHtml(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_mega)
            )
        )
    }
}

@Composable
fun InitScreen(
    @DrawableRes drawableId: Int,
    @StringRes stringId: Int,
    windowWidthSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    if (windowWidthSize == WindowWidthSizeClass.Compact) {
        val color = colorResource(R.color.empty_list)
        val message = stringResource(stringId)

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(drawableId),
                contentDescription = null,
                modifier = Modifier.size(
                    dimensionResource(R.dimen.common_screens_icon_size)
                ),
                colorFilter = ColorFilter.tint(
                    color, blendMode = BlendMode.SrcIn
                )
            )
            Text(
                text = message,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.padding_mega)
                )
            )
            MessageWithIcon(
                drawableId = R.drawable.outline_manage_search_24,
                stringId = R.string.details_manage_search_message
            )
        }
    }
}

@Composable
fun InvalidScreen(
    modifier: Modifier = Modifier
) {
    val color = colorResource(R.color.empty_list)
    val message = stringResource(R.string.filter_invalid)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            contentDescription = null,
            imageVector = Icons.Outlined.HistoryToggleOff,
            modifier = Modifier.size(
                dimensionResource(R.dimen.common_screens_icon_size)
            ),
            colorFilter = ColorFilter.tint(
                color, blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = message.fromHtml(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_large)
            )
        )
    }
}

@Composable
fun LoadingScreen(
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            contentDescription = stringResource(R.string.loading_anim),
            painter = painterResource(R.drawable.loading_image),
            modifier = Modifier
                .size(
                    dimensionResource(R.dimen.loading_anim_size)
                )
                /* Refer custom modifier in ModifierUtils */
                .downloading(isDownloading = true)
        )
        Text(
            text = stringResource(stringId),
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_large)
            )
        )
    }
}

@Composable
fun NotFoundScreen(
    query: String,
    modifier: Modifier = Modifier
) {
    val color = colorResource(R.color.empty_list)
    val message = stringResource(
        R.string.found_not_message,
        query
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.WarningAmber,
            contentDescription = null,
            modifier = Modifier.size(
                dimensionResource(R.dimen.common_screens_icon_size)
            ),
            colorFilter = ColorFilter.tint(
                color, blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = message.fromHtml(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_mega)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyFilterPreview() {
    TePapaCollectionsTheme {
        EmptyFilterScreen(
            canDownload = true,
            mediaType = MediaType.Person,
            onClickDownloadMore = {},
            query = "Robert Muldoon"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptySearchPreview() {
    TePapaCollectionsTheme {
        EmptySearchScreen(
            mediaType = MediaType.Position,
            onClickRemoveFilter = {},
            query = "Sergeant Shultz"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    TePapaCollectionsTheme {
        ErrorScreen({})
    }
}

@Preview(showBackground = true)
@Composable
fun ForbiddenScreenPreview() {
    TePapaCollectionsTheme {
        ForbiddenScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun InitScreenPreview() {
    TePapaCollectionsTheme {
        InitScreen(
            drawableId =
                R.drawable.outline_category_search_24,
            stringId =
                R.string.details_placeholder_type,
            windowWidthSize =
                WindowWidthSizeClass.Compact
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InvalidScreenPreview() {
    TePapaCollectionsTheme {
        InvalidScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    TePapaCollectionsTheme {
        LoadingScreen(
            R.string.loading_anim
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotFoundScreenPreview() {
    TePapaCollectionsTheme {
        NotFoundScreen(
            query = "rhinoceros"
        )
    }
}