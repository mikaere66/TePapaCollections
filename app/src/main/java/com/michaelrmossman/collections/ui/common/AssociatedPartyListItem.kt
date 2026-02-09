package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun AssociatedPartyListItem(
    reference: SearchResult,
    modifier: Modifier = Modifier
) {
    val associatedTypeText = stringResource(
        R.string.common_type,
        reference.media
    ).fromHtml()

    val associatedNameText = stringResource(
        R.string.common_name,
        reference.title
    ).fromHtml()

    Text(
        text = associatedTypeText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
    Text(
        text = associatedNameText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
}