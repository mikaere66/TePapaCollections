package com.michaelrmossman.collections.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.ReferType
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.model.SearchResult.Object
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getDimensionsHeaderAndTexts
import com.michaelrmossman.collections.util.TextUtils.getReferenceText
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.TextUtils.getTextFromString

@Composable
fun DetailsObject(
    isNestedContent: Boolean,
    `object`: Object,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    onClickImages: (List<ImageObject>, String) -> Unit,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | collectionLabel | isTypeOf */

    val additionalTypes = `object`.additionalType.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Additional type: PhysicalObject" */
    val additionalTypesText = getTextFromList(
        list = `object`.additionalType,
        listFlattened = additionalTypes,
        pluralsId = R.plurals.additional_types
    )

    val captionText = getTextFromString(
        stringId = R.string.caption_formatted,
        string = `object`.captionFormatted
    )

    val createdDates = `object`.production.joinToString(
        ITEM_SEPARATOR
    ) { producer -> producer.verbatimCreatedDate }
    /* e.g. "Created: 1991" */
    val createdDatesText = getTextFromString(
        stringId = R.string.object_created_dates,
        string = createdDates
    )

    val contributors = `object`.production.map { producer ->
        producer.contributor.title
    }
    val contributorsFlattened = contributors.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Contributor: Jeff Thomson" */
    val contributorsText = getTextFromList(
        list = contributors,
        listFlattened = contributorsFlattened,
        pluralsId = R.plurals.object_contributors
    )

    val descriptionText = getTextFromString(
        stringId = R.string.common_description,
        string = `object`.description
    )

    /* Returns an AnnotatedString and a List<String> */
    val dimensionsHeaderAndTexts = getDimensionsHeaderAndTexts(
        dimensionsList = `object`.observedDimension
    )

    val isMadeOfText = getTextFromString(
        stringId = R.string.object_is_made_of,
        string = `object`.isMadeOfSummary
    )

    val isReferencedByText = getReferenceText(
        headerText = stringResource(R.string.referenced_by),
        results = `object`.isReferencedBy
    )

    val refersToList = `object`.refersTo.map { reference ->
        reference.title
    }.sortedWith(
        String.CASE_INSENSITIVE_ORDER // Note sort
    )
    /* e.g. "Contributor: Jeff Thomson" */
    val refersToText = getTextFromString(
        stringId = R.string.refers_to,
        string = refersToList.joinToString(
            ITEM_SEPARATOR
        )
    )

    var showIsReferByBS by rememberSaveable { mutableStateOf(false) }
    if (showIsReferByBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showIsReferByBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.IsReferencedBy,
            refsList = `object`.isReferencedBy
        )
    }

    var showRefersToBS by rememberSaveable { mutableStateOf(false) }
    if (showRefersToBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showRefersToBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.RefersTo,
            refsList = `object`.refersTo
        )
    }

    val relatedWebPage = when (`object`.related.isNotEmpty()) {
        true -> `object`.related
        else -> null
    }

    val thumbnails = when (`object`.hasRepresentation.isEmpty()) {
        true -> emptyList()
        else -> `object`.hasRepresentation.filter { result ->
            result.media == MediaType.ImageObject
            &&
            (result as ImageObject).thumbnailUrl.isNotBlank()
        }
    }

    val creditLineText = getTextFromString(
        stringId = R.string.credit_line,
        string = `object`.creditLine
    )

    /* = | = | = | = | = | = | = | = */

    Text(
        text = additionalTypesText,
        modifier = modifier
    )
    Text(
        text = captionText,
        modifier = modifier
    )
    Text(
        text = createdDatesText,
        modifier = modifier
    )
    Text(
        text = contributorsText,
        modifier = modifier
    )
    Text(
        text = descriptionText,
        modifier = modifier
    )
    if (dimensionsHeaderAndTexts.second.isNotEmpty()) {
        Text(
            text = dimensionsHeaderAndTexts.first,
            modifier = modifier
        )
        dimensionsHeaderAndTexts.second.forEach { dimensionText ->
            Text(
                text = dimensionText,
                modifier = modifier
            )
        }
    }
    Text(
        text = isMadeOfText,
        modifier = modifier
    )
    if (`object`.isReferencedBy.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showIsReferByBS = true },
            refsText = isReferencedByText
        )
    }
    if (refersToList.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showRefersToBS = true },
            refsText = refersToText
        )
    }
    relatedWebPage?.let { related ->
        RelatedWebPage(
            related = related
        )
    }
    if (thumbnails.isNotEmpty()) {
        XNumThumbnails(
            modifier = modifier,
            onClickImages = onClickImages,
            results = thumbnails,
            title = `object`.title
        )
    }
    Text(
        text = creditLineText,
        modifier = modifier
    )
    MetaDataFooter(
        result = `object`
    )
}