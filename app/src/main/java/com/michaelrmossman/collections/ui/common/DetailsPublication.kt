package com.michaelrmossman.collections.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.ReferType
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.model.SearchResult.Publication
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getReferenceText
import com.michaelrmossman.collections.util.TextUtils.getTextFromString
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun DetailsPublication(
    isNestedContent: Boolean,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    onClickImages: (List<ImageObject>, String) -> Unit,
    publication: Publication,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | author | collectionLabel */

    val undefinedString = stringResource(R.string.common_undefined)

    /* Not "simplified", to maintain readability */
    @Suppress("SimplifiableCallChain")
    val publishers = publication.publisher.map { publisher ->
        publisher.title
    }.joinToString(
        ITEM_SEPARATOR
    )
    val publisherText = pluralStringResource(
        R.plurals.publication_publishers,
        publishers.split(ITEM_SEPARATOR).size,
        publishers
    ).fromHtml()

    val publicationDates = publication.publicationDate.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Publication dates: 2015, 2017" */
    val publicationDatesText = pluralStringResource(
        R.plurals.publication_dates,
        publication.publicationDate.size,
        when (publication.publicationDate.isEmpty()) {
            true -> undefinedString
            else -> publicationDates
        }
    ).fromHtml()

    val purposes = publication.purpose.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Purposes: Publication, Tuhinga" */
    val purposesText = pluralStringResource(
        R.plurals.purposes,
        publication.purpose.size,
        when (publication.purpose.isEmpty()) {
            true -> undefinedString
            else -> purposes
        }
    ).fromHtml()

    val publicationTypes = publication.publicationType.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Categories: sculpture, automobiles" */
    val publicationTypesText = pluralStringResource(
        R.plurals.publication_types,
        publication.publicationType.size,
        when (publication.publicationType.isEmpty()) {
            true -> undefinedString
            else -> publicationTypes
        }
    ).fromHtml()

    val refersTo = publication.refersTo.map { reference ->
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

    val relatedObjectsText = getReferenceText(
        headerText = pluralStringResource(
            R.plurals.related_objects,
            publication.relatedObjects.size
        ),
        results = publication.relatedObjects
    )
    var showRelObjBS by rememberSaveable { mutableStateOf(false) }
    if (showRelObjBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showRelObjBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.RelatedObjects,
            refsList = publication.relatedObjects
        )
    }

    val relatedTopicsText = getReferenceText(
        headerText = pluralStringResource(
            R.plurals.related_topics,
            publication.relatedTopics.size
        ),
        results = publication.relatedTopics
    )
    var showRelTopBS by rememberSaveable { mutableStateOf(false) }
    if (showRelTopBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showRelTopBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.RelatedTopics,
            refsList = publication.relatedTopics
        )
    }

    val thumbnails = when (publication.hasRepresentation.isEmpty()) {
        true -> emptyList()
        else -> publication.hasRepresentation.filter { result ->
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
            refsList = publication.refersTo
        )
    }

    /* = | = | = | = | = | = | = | = */

    Text(
        text = publisherText,
        modifier = modifier
    )
    Text(
        text = publicationDatesText,
        modifier = modifier
    )
    Text(
        text = purposesText,
        modifier = modifier
    )
    Text(
        text = publicationTypesText,
        modifier = modifier
    )
    if (refersTo.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showRefersToBS = true },
            refsText = refersToText
        )
    }
    if (publication.relatedObjects.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showRelObjBS = true },
            refsText = relatedObjectsText
        )
    }
    if (publication.relatedTopics.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showRelTopBS = true },
            refsText = relatedTopicsText
        )
    }
    if (thumbnails.isNotEmpty()) {
        XNumThumbnails(
            modifier = modifier,
            onClickImages = onClickImages,
            results = thumbnails,
            title = publication.title
        )
    }
    MetaDataFooter(
        result = publication
    )
}