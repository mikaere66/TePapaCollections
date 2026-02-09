package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.util.TextUtils.getContentSizeFormatted
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnImageInfoBottomSheet(
    imageObject: ImageObject,
    onDismissRequest: () -> Unit,
    zoomImageTitle: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val headerText = stringResource(R.string.image_info_desc).plus(":")
    val iconLargePadding = dimensionResource(R.dimen.padding_great)
    val iconSize = dimensionResource(R.dimen.icon_size_small)
    val lazyListState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState()
    val textHorizontalPadding = dimensionResource(R.dimen.padding_medium)
    val textVerticalPadding = dimensionResource(R.dimen.padding_small)
    val rowVerticalPadding = dimensionResource(R.dimen.padding_small)
    val verticalSpacing = dimensionResource(R.dimen.spacing_vertical_small)

    /* I know; great English, right =) */
    val imageInfos = listOf(
        stringResource(
            R.string.image_info_width,
            imageObject.width.roundToInt()
        ),
        stringResource(
            R.string.image_info_height,
            imageObject.height.roundToInt()
        ),
        getContentSizeFormatted(
            imageObject.contentSize
        )
    )

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            state = lazyListState,
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            item(key = -1) {
                Row(
                    modifier = Modifier.padding(
                        vertical = rowVerticalPadding
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        headerText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(
                                horizontal = textHorizontalPadding
                            )
                            .weight(1F)
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(horizontal = iconLargePadding)
                            .size(iconSize),
                        onClick = { onDismissRequest() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(
                                R.string.bottom_sheet_dismiss
                            )
                        )
                    }
                }
            }
            itemsIndexed(
                items = imageInfos
            ) { _, imageInfo ->
                Text(
                    text = imageInfo,
                    modifier = Modifier.padding(
                        horizontal = textHorizontalPadding,
                        vertical = textVerticalPadding
                    )
                )
            }
            zoomImageTitle?.let { imageTitle ->
                item(key = -2) {
                    imageTitle.invoke()
                }
            }
        }
    }
}