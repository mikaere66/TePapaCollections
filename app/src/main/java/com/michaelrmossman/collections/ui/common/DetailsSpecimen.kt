package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.ReferType
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.model.SearchResult.Specimen
import com.michaelrmossman.collections.ui.components.TextWithMapIcon
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getDimensionsHeaderAndTexts
import com.michaelrmossman.collections.util.TextUtils.getReferenceText
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.TextUtils.getTextFromString
import com.michaelrmossman.collections.util.TextUtils.getTextFromStringWithPipes
import com.michaelrmossman.collections.util.TextUtils.getTextFromStringWithTwo
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun DetailsSpecimen(
    isNestedContent: Boolean,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    onClickImages: (List<ImageObject>, String) -> Unit,
    onClickMapButton: (AMapMarker) -> Unit,
    specimen: Specimen,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | collectionLabel | basisOfRecord */

    val dividerPadding = dimensionResource(R.dimen.padding_small)

    val additionalTypes = specimen.additionalType.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Additional types: PhysicalObject, BiologicalObject" */
    val additionalTypesText = getTextFromList(
        list = specimen.additionalType,
        listFlattened = additionalTypes,
        pluralsId = R.plurals.additional_types
    )

    val captionText = getTextFromString(
        stringId = R.string.caption_formatted,
        string = specimen.captionFormatted
    )

    val descriptionText = getTextFromString(
        stringId = R.string.common_description,
        string = specimen.description
    )

    /* Returns an [AnnotatedString] and a list of [String]s */
    val dimensionsHeaderAndTexts = getDimensionsHeaderAndTexts(
        dimensionsList = specimen.observedDimension
    )

    val evidenceHeader = getTextFromString(
        stringId = R.string.evidence_header,
        string = specimen.evidenceFor.type
    )
    var evidenceOnMap: @Composable (() -> Unit)? = null
    val evidenceTexts = mutableListOf<AnnotatedString>()
    if (specimen.evidenceFor.title.isNotBlank()) {
        evidenceTexts.add(getTextFromString(
            stringId = R.string.evidence_title,
            string = specimen.evidenceFor.title
        ))
        /* Not "simplified", to maintain readability */
        @Suppress("SimplifiableCallChain")
        evidenceTexts.add(stringResource(
            R.string.evidence_recorded_by,
            specimen.evidenceFor.atEvent.recordedBy.map { recordedBy ->
                recordedBy.title
            }.joinToString(
                ITEM_SEPARATOR
            )
        ).fromHtml())
        evidenceTexts.add(getTextFromString(
            stringId = R.string.evidence_state_province,
            string = specimen.evidenceFor.atEvent.atLocation.stateProvince
        ))
        evidenceTexts.add(getTextFromString(
            stringId = R.string.evidence_country,
            string = specimen.evidenceFor.atEvent.atLocation.country
        ))
        if (
            specimen.evidenceFor.atEvent.atLocation.mappingCentroid.lat != 0.0
            &&
            specimen.evidenceFor.atEvent.atLocation.mappingCentroid.lon != 0.0
        ) {
            evidenceOnMap = {
                val mapMarker = AMapMarker(
                    lat = specimen.evidenceFor.atEvent
                        .atLocation.mappingCentroid.lat,
                    lon = specimen.evidenceFor.atEvent
                        .atLocation.mappingCentroid.lon,
                    snippet = specimen.evidenceFor
                        .atEvent.atLocation.stateProvince,
                    title = stringResource(
                        R.string.evidence_map_title,
                        specimen.evidenceFor.atEvent.type
                    )
                )
                TextWithMapIcon(
                    mapMarker = mapMarker,
                    onClickMapButton = onClickMapButton
                )
            }
        }
    }

    /* This [Identification] section has been refactored
       to show individual [Identification] items, rather
       than flattened lists... however [VernacularName]
       remains unchanged, hence the two overall lists */
    val identificationHeader = stringResource(
        R.string.id_header,
        specimen.identification.size,
        pluralStringResource(
            R.plurals.id_specimens,
            specimen.identification.size
        )
    ).fromHtml()
    val identifiedSpecimens = mutableListOf<List<AnnotatedString>>()
    if (specimen.identification.isNotEmpty()) {
        /* Map to a list of lists, i.e. List<List<VernacularName>> */
        val vernacularLists = specimen.identification.filter { identification ->
            /* Only if [VernacularName](s) exist in identification */
            identification.toTaxon.vernacularName.isNotEmpty()
        }.map { identification ->
            identification.toTaxon.vernacularName
        }
        /* Map to another list of lists, i.e. List<List<String>> */
        val vernacularNameLists = vernacularLists.map { list ->
            list.map { listItem ->
                when (listItem.language.isBlank()) {
                    /* e.g. "Little Spotted Kiwi" */
                    true -> listItem.title
                    /* e.g. "kiwi pukupuku (Māori)" */
                    else -> stringResource(
                        R.string.common_two_args,
                        listItem.title,
                        listItem.language
                    )
                }
            }
        }
        /* Now map to one final list, i.e. List<AnnotatedString> */
        val vernacularNameTexts = vernacularNameLists.map { list ->
            getTextFromList(
                list = list,
                listFlattened = list.joinToString(
                    ITEM_SEPARATOR
                ),
                pluralsId = R.plurals.id_vernacular_names
            )
        }

        specimen.identification.forEachIndexed { index, identification ->
            val identifiers = mutableListOf<AnnotatedString>()

            identifiers.add(getTextFromString(
                stringId = R.string.id_qual_name,
                string = identification.qualifiedName
            ))
            identifiers.add(getTextFromStringWithTwo(
                stringId = R.string.id_by,
                string1 = identification.identifiedBy.title,
                string2 = identification.identifiedBy.type
            ))
            identifiers.add(getTextFromString(
                stringId = R.string.id_date,
                string = identification.dateIdentified
            ))
            identifiers.add(getTextFromStringWithPipes(
                pluralsId = R.plurals.id_higher_classifications,
                string = identification.toTaxon.higherClassification
            ))
            if (
                specimen.identification.size
                == /* Just for safety */
                vernacularNameTexts.size
            ) {
                identifiers.add(vernacularNameTexts[index])
            }

            identifiedSpecimens.add(identifiers)
        }
    }

    val isReferencedByText = getReferenceText(
        headerText = stringResource(R.string.referenced_by),
        results = specimen.isReferencedBy
    )

    var showIsReferByBS by rememberSaveable { mutableStateOf(false) }
    if (showIsReferByBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showIsReferByBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.IsReferencedBy,
            refsList = specimen.isReferencedBy
        )
    }

    val thumbnails = when (specimen.hasRepresentation.isEmpty()) {
        true -> emptyList()
        else -> specimen.hasRepresentation.filter { result ->
            result.media == MediaType.ImageObject
            &&
            (result as ImageObject).thumbnailUrl.isNotBlank()
        }
    }

    val creditLineText = getTextFromString(
        stringId = R.string.credit_line,
        string = specimen.creditLine
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
        text = descriptionText,
        textAlign = TextAlign.Justify,
        modifier = modifier
    )
    Text(
        text = dimensionsHeaderAndTexts.first,
        modifier = modifier
    )
    /* If no dimensions, first item will be a msg saying so */
    dimensionsHeaderAndTexts.second.forEach { dimensionText ->
        Text(
            text = dimensionText,
            modifier = modifier
        )
    }
    if (evidenceTexts.isNotEmpty()) {
        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = dividerPadding
            )
        )
        Text(
            text = evidenceHeader,
            modifier = modifier
        )
        evidenceTexts.forEachIndexed { index, evidenceText ->
            Text(
                text = evidenceText,
                modifier = modifier
            )
        }
        evidenceOnMap?.invoke() /* Row containing Text & IconButton */
    }
    if (identifiedSpecimens.isNotEmpty()) {
        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = dividerPadding
            )
        )
        Text(
            text = identificationHeader,
            modifier = modifier
        )
        identifiedSpecimens.forEachIndexed { index, identification ->
            identification.forEach { identificationText ->
                Text(
                    text = identificationText,
                    modifier = modifier
                )
            }
            if (index != identifiedSpecimens.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = dividerPadding
                    )
                )
            }
        }
    }
    if (specimen.isReferencedBy.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showIsReferByBS = true },
            refsText = isReferencedByText
        )
    }
    if (thumbnails.isNotEmpty()) {
        XNumThumbnails(
            modifier = modifier,
            onClickImages = onClickImages,
            results = thumbnails,
            title = specimen.title
        )
    }
    Text(
        text = creditLineText,
        modifier = modifier
    )
    MetaDataFooter(
        result = specimen
    )
}