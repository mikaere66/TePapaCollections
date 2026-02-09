package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.util.ListItemUtils.getAuthorsText
import com.michaelrmossman.collections.util.ListItemUtils.getCollectionsText
import com.michaelrmossman.collections.model.SearchResult.Publication
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun ListItemPublication(
    publication: Publication,
    modifier: Modifier = Modifier
) {
    val authors = publication.authors.map { author ->
        author.title
    }
    val authorsFlattened = authors.joinToString(
        ITEM_SEPARATOR
    )
    val authorsText = getAuthorsText(
        authors = authors,
        authorsFlattened = authorsFlattened
    )
//    pluralStringResource(
//        R.plurals.publication_authors,
//        authors.size,
//        authorsFlattened
//    ).fromHtml()

    val collectionLabels = publication.collectionLabel
    val collectionLabelsFlattened = collectionLabels.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Collections: History, Taonga Māori" */
    val collectionLabelsText = getCollectionsText(
        collectionLabels = collectionLabels,
        collectionLabelsFlattened = collectionLabelsFlattened
    )
//    getTextFromList(
//        list = publication.collectionLabel,
//        listFlattened = collectionLabels,
//        pluralsId = R.plurals.collection_labels
//    )

    TypeIconWithTitle(
        result = publication,
        modifier = modifier
    )
    Text(
        text = authorsText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
    Text(
        text = collectionLabelsText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
}