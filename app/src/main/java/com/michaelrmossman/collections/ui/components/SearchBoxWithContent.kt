package com.michaelrmossman.collections.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.util.replaceMacrons
import java.util.Locale

/* SearchBoxWithContent is used to search
   WITHIN results, from [*DetailsScreen]s */
@Composable
fun SearchBoxWithContent(
    content: @Composable () -> Unit,
    contentPadding: PaddingValues,
    hashMap: HashMap<Int, String>,
    isSearchVisible: Boolean,
    onClickSearchItem: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = isSearchVisible,
            enter = slideInVertically(
                /* Enters by sliding down from an offset above the
                   screen. Lambda takes full height of the content */
                initialOffsetY = { fullHeight -> 0.minus(fullHeight) }
            )
        ) {
            SearchBox(
                contentPadding = contentPadding,
                hashMap = hashMap,
                onClickSearchItem = { itemId ->
                    onClickSearchItem(itemId)
                }
            )
        }

        content()
    }
}

@Composable
fun SearchBox(
    contentPadding: PaddingValues,
    hashMap: HashMap<Int, String>,
    onClickSearchItem: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val horizontalPadding = dimensionResource(R.dimen.padding_medium)
    val iconSmallPadding = dimensionResource(R.dimen.padding_small)
    val iconSize = dimensionResource(R.dimen.icon_size_small)
    val lazyListState = rememberLazyListState()
    val roundedCornerShape = dimensionResource(R.dimen.card_corner_shape)
    val sortedMap = hashMap.entries
        /* Sort alphabetically, allowing for special characters */
        .sortedBy  { entry -> entry.value.replaceMacrons() }
        .associate { entry -> entry.key to entry.value }
     /* Just "name" values, or "summary" in the case of Alerts" */
    val sortedValues = sortedMap.values.toList()
    val verticalPadding = dimensionResource(R.dimen.padding_small)
    val verticalSpacing = dimensionResource(R.dimen.spacing_vertical_small)

    OutlinedCard(
        modifier = modifier
            .padding(
                end = horizontalPadding,
                start = horizontalPadding,
                top = contentPadding.calculateTopPadding().plus(
                    verticalPadding
                )
            )
            .fillMaxWidth(),
        shape = RoundedCornerShape(roundedCornerShape), // was 20.dp
        colors = CardDefaults.cardColors()
    ) {
        var searchList  by remember { mutableStateOf(emptyList<String>()) }
        var searchQuery by remember { mutableStateOf(String()) }
        OutlinedTextField(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small)) // was 10.dp
                .fillMaxWidth(),
            shape = RoundedCornerShape(roundedCornerShape), // was 10.dp
            colors = TextFieldDefaults.colors(),
            trailingIcon = {
                IconButton(
                    modifier = Modifier
                        .padding(horizontal = iconSmallPadding)
                        .size(iconSize),
                    onClick = {
                        searchQuery = String()
                        searchList = emptyList()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Backspace,
                        contentDescription = stringResource(
                            R.string.common_search_clear
                        )
                    )
                }
            },
            maxLines = 1,
            singleLine = true,
            value = searchQuery,
            onValueChange = { value ->
                searchQuery = value
                searchList = when (searchQuery.isBlank()) {
                    true -> emptyList()
                    else -> sortedValues.filter { item ->
                        item.lowercase(Locale.getDefault()).contains(
                            searchQuery.lowercase(Locale.getDefault())
                        )
                    }
                }
            },
            placeholder = {
                Text(text = stringResource(R.string.search_within))
            }
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            state = lazyListState,
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_small)
                )
        ) {
            itemsIndexed(
                items = searchList,
            ) { index, searchResult ->
                Text(
                    text = searchResult,
                    modifier = Modifier
                        .padding(
                            vertical = dimensionResource(
                                R.dimen.padding_search_box_vertical
                            ), // was 10.dp
                            horizontal = dimensionResource(
                                R.dimen.padding_search_box_horizontal
                            ) // was 15.dp
                        )
                        .fillMaxWidth()
                        .clickable {
                            searchQuery = String()
                            searchList = emptyList()
                            sortedMap.entries.find { entry ->
                                entry.value == searchResult
                            }?.key?.let { key ->
                                onClickSearchItem(key)
                            }
                        }
                )
                when (index) {
                    searchList.lastIndex -> Spacer(
                        modifier = Modifier.statusBarsPadding()
                    )
                    else -> HorizontalDivider(
                        thickness = dimensionResource(
                            R.dimen.thickness_search_box_divider
                        ), // was 0.5.dp
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}