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
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.model.SearchResult.Group
import com.michaelrmossman.collections.ui.components.TypeIcon
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.TextUtils.getTextFromString
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun DetailsGroup(
    group: Group,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | (noFurtherInfo */


    MetaDataFooter(
        result = group
    )
}