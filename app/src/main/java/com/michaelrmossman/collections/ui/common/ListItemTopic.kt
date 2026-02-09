package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.text.style.TextOverflow
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.Topic
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.ListItemUtils.getCollectionsText
import com.michaelrmossman.collections.util.ListItemUtils.getNarrativeSummaryText
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.TextUtils.getTextFromString

@Composable
fun ListItemTopic(
    fullText: Boolean,
    topic: Topic,
    modifier: Modifier = Modifier
) {
    val collectionLabels = topic.collectionLabel
    val collectionLabelsFlattened = collectionLabels.joinToString(
        ITEM_SEPARATOR
    )
    val collectionText = getCollectionsText(
        collectionLabels = collectionLabels,
        collectionLabelsFlattened = collectionLabelsFlattened
    )
//    getTextFromList(
//        list = topic.collectionLabel,
//        listFlattened = collections,
//        pluralsId = R.plurals.collection_labels
//    )

    val summaryMaxLines = when (fullText) {
        true -> Int.MAX_VALUE // Details
        else -> integerResource( // List
            id = R.integer.narrative_summary_max_lines
        )
    }
    val summaryText = getNarrativeSummaryText(
        narrativeSummary = topic.narrativeSummary
    )
//    getTextFromString(
//        stringId = R.string.narrative_summary,
//        string = topic.narrativeSummary
//    )

    TypeIconWithTitle(
        result = topic,
        modifier = modifier
    )
    Text(
        text = collectionText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
    Text(
        text = summaryText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        ),
        maxLines = summaryMaxLines,
        overflow = TextOverflow.Ellipsis
    )
}