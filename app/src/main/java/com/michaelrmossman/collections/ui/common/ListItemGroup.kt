package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.model.SearchResult.Group
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun ListItemGroup(
    group: Group,
    modifier: Modifier = Modifier
) {
    val noFurtherInfoText = stringResource(
        R.string.no_further_info
    )

    TypeIconWithTitle(
        result = group,
        modifier = modifier
    )
    Text(
        text = noFurtherInfoText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
}