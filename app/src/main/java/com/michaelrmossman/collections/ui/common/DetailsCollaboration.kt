package com.michaelrmossman.collections.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.Collaboration
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun DetailsCollaboration(
    collaboration: Collaboration,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | (noFurtherInfo */

    MetaDataFooter(
        result = collaboration
    )
}