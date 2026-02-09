package com.michaelrmossman.collections.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.util.EMOJI_ERROR_DELAY
import com.michaelrmossman.collections.util.EmojiInputFilter.Companion.containsEmoji
import kotlinx.coroutines.delay

/* SearchBoxWithButton is used to perform MoNZ
   query; found at top of CommonQueryScreen */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBoxWithButton(
    currentMedia: Media?,
    currentQueryType: Int,
    entries: Iterable<Media>,
    isEnabled: Boolean,
    onClickClearButton: () -> Unit,
    onClickMediaItem: (Int) -> Unit,
    onClickQueryItem: (Int) -> Unit,
    onClickSearchButton: (String) -> Unit,
    onTextChanged: (String) -> Unit,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    val commonPadding = dimensionResource(R.dimen.padding_small)
    val iconLargeSize = dimensionResource(R.dimen.icon_size_large)
    val iconPadding = dimensionResource(R.dimen.padding_large)
    val iconSmallSize = dimensionResource(R.dimen.icon_size_small)
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isError) {
        if (isError) {
            delay(EMOJI_ERROR_DELAY) // 2000ms
            isError = false
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            colors = TextFieldDefaults.colors(),
            isError = isError,
            /* Not supported by all keyboards,
               hence need for EmojiInputFilter */
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii
            ),
            leadingIcon = {
                SearchBoxActionMenu(
                    currentMedia = currentMedia,
                    currentQueryType = currentQueryType,
                    entries = entries,
                    modifier = Modifier
                        .padding(horizontal = iconPadding)
                        .size(iconSmallSize),
                    onClickMediaItem = onClickMediaItem,
                    onClickQueryItem = onClickQueryItem
                )
            },
            maxLines = 1,
            modifier = Modifier
                .padding(
                    top = commonPadding,
                    end = commonPadding,
                    start = commonPadding
                )
                .weight(1F),
            onValueChange = { value ->
                if (containsEmoji(
                    value,
                    0,
                    value.length
                )) {
                    isError = true
                }
                onTextChanged(value)
            },
            placeholder = {
                val placeholderText = stringResource(
                    R.string.common_search_hint
                )
                Text(text = placeholderText)
            },
            shape = RoundedCornerShape(
                dimensionResource(R.dimen.card_corner_shape)
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    modifier = Modifier
                        .padding(horizontal = iconPadding)
                        .size(iconSmallSize),
                    onClick = {
                        onClickClearButton()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Clear,
                        contentDescription = stringResource(
                            R.string.common_search_clear
                        )
                    )
                }
            },
            value = searchQuery
        )

        IconButton(
            enabled = (isEnabled && searchQuery.length > 1),
            onClick = {
                onClickSearchButton(searchQuery)
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContentColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.padding(top = commonPadding)
        ) {
            Icon(
                modifier = Modifier.size(iconLargeSize),
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(
                    R.string.common_search_desc
                )
            )
        }
    }
}