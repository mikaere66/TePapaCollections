package com.michaelrmossman.collections.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.ReferType
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.model.SearchResult.Topic
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getReferenceText
import com.michaelrmossman.collections.util.TextUtils.getTextFromString
import com.michaelrmossman.collections.util.replaceExtraneousParagraphs

@Composable
fun DetailsTopic(
    isNestedContent: Boolean,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    onClickImages: (List<ImageObject>, String) -> Unit,
    topic: Topic,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | collectionLabel | narrativeSummary */

    val narrative = topic.narrative.replaceExtraneousParagraphs()
    val narrativeText = getTextFromString(
        stringId = R.string.topic_narrative,
        string = narrative
    )

    val refersTo = topic.refersTo.map { reference ->
        reference.title
    }.sortedWith(
        String.CASE_INSENSITIVE_ORDER // Note sort
    )
    val refersToFlattened = refersTo.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Category: Gifts" */
    val refersToText = getTextFromString(
        stringId = R.string.refers_to,
        string = refersToFlattened
    )

    val thumbnails = when (topic.hasRepresentation.isEmpty()) {
        true -> emptyList()
        else -> topic.hasRepresentation.filter { result ->
            result.media == MediaType.ImageObject
            &&
            (result as ImageObject).thumbnailUrl.isNotBlank()
        }
    }

    var showRefersToBS by rememberSaveable { mutableStateOf(false) }
    if (showRefersToBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showRefersToBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.RefersTo,
            refsList = topic.refersTo
        )
    }

    val relatedObjectsText = getReferenceText(
        headerText = pluralStringResource(
            R.plurals.related_objects,
            topic.relatedObjects.size
        ),
        results = topic.relatedObjects
    )
    var showRelObjBS by rememberSaveable { mutableStateOf(false) }
    if (showRelObjBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showRelObjBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.RelatedObjects,
            refsList = topic.relatedObjects
        )
    }

    val relatedTopicsText = getReferenceText(
        headerText = pluralStringResource(
            R.plurals.related_topics,
            topic.relatedTopics.size
        ),
        results = topic.relatedTopics
    )
    var showRelTopBS by rememberSaveable { mutableStateOf(false) }
    if (showRelTopBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showRelTopBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.RelatedTopics,
            refsList = topic.relatedTopics
        )
    }

    val relatedWebPage = when (topic.related.isNotEmpty()) {
        true -> topic.related
        else -> null
    }

    /* = | = | = | = | = | = | = | = */

    Text(
        text = narrativeText,
        modifier = modifier
    )
    if (refersTo.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showRefersToBS = true },
            refsText = refersToText
        )
    }
    if (thumbnails.isNotEmpty()) {
        XNumThumbnails(
            modifier = modifier,
            onClickImages = onClickImages,
            results = thumbnails,
            title = topic.title
        )
    }
    if (topic.relatedObjects.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showRelObjBS = true },
            refsText = relatedObjectsText
        )
    }
    if (topic.relatedTopics.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showRelTopBS = true },
            refsText = relatedTopicsText
        )
    }
    relatedWebPage?.let { related ->
        RelatedWebPage(
            related = related
        )
    }
    MetaDataFooter(
        result = topic
    )
}