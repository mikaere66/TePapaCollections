package com.michaelrmossman.collections.util

import androidx.annotation.DimenRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.sp
import com.michaelrmossman.collections.CollectionsApplication.Companion.instance
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.ObservedDimension
import com.michaelrmossman.collections.model.SearchResult

/**
 * Text utility functions used throughout the app
 */
object TextUtils {

    @Composable
    @ReadOnlyComposable
    fun fontDimensionResource(@DimenRes id: Int) =
        dimensionResource(id = id).value.sp

    @Composable
    fun getAppSubtitle(
        isInterim: Boolean,
        listSize: Int,
        media: Media?,
        resultCount: Int,
        @StringRes stringId: Int
    ) : String {
        val sb = StringBuilder()
        when (media) {
            null -> sb.append(stringResource(stringId))
            else -> {
                sb.append(stringResource(when (isInterim) {
                    true -> R.string.app_subtitle_filtered_by
                    else -> R.string.app_subtitle_search_by
                }))
                sb.append(" ")
                sb.append(media.toString())
            }
        }
        /* formatWithComma() returns a string */
        if (listSize > 0) {
            sb.append(" ")
            sb.append("(")
            sb.append(listSize.formatWithComma())
            sb.append(" ")
            sb.append(stringResource(R.string.app_subtitle_of))
            sb.append(" ")
            sb.append(resultCount.formatWithComma())
            sb.append(")")
        }
        return sb.toString()
    }

    fun getCollectionForFave(
        result: SearchResult,
        searchType: SearchType
    ) : String = when (searchType) {
        SearchType.MediaObject -> {
            (result as SearchResult.Object).collection
        }
        SearchType.MediaSpecimen -> {
            (result as SearchResult.Specimen).collection
        }
        SearchType.MediaType -> {
            when (result.media) {
                MediaType.Object -> {
                    (result as SearchResult.Object).collection
                }
                MediaType.Publication -> {
                    (result as SearchResult.Publication)
                        .collection.joinToString(
                            RECORD_SEPARATOR
                        )
                }
                MediaType.Specimen -> {
                    (result as SearchResult.Specimen).collection
                }
                MediaType.Topic -> {
                    (result as SearchResult.Topic)
                        .collection.joinToString(
                            RECORD_SEPARATOR
                        )
                }
                else -> String()
            }
        }
    }

    @Composable
    fun getContentSizeFormatted(
        size: Double
    ) : String {
        return when (size) {
            0.0 -> stringResource(R.string.common_undefined)
            else -> {
                val stringId = when (size) {
                    in 1.0..1023.0 -> {
                        R.string.image_info_bytes // 0 dec places
                    }
                    else -> R.string.image_info_x_bytes // 2 dec
                }
                stringResource(
                    stringId,
                    when (size) {
                        in 1.0..1023.0 -> size    // bytes
                        in 1024.0..1048575.0 -> {    // Kb
                            size.div(1024)
                        }
                        in 1048576.0..1073741823.0 -> {
                            size.div(1024).div(1024) // Mb
                        }
                        else -> size.div(            // Gb
                            1024
                        ).div(
                            1024
                        ).div(
                            1024
                        )
                    },
                    when (size) {
                        in 1.0..1023.0 -> {
                            "bytes" // e.g. Size: 123 bytes
                        }
                        in 1024.0..1048575.0 -> {
                            "Kb"      // e.g. Size: 1.23 Kb
                        }
                        in 1048576.0..1073741823.0 -> {
                            "Mb"      // e.g. Size: 1.23 Mb
                        }
                        // e.g. Size: 1.23 Gb, tho unlikely
                        else -> "Gb"
                    }
                )
            }
        }
    }

     /* Used by Object and Specimen */
    @Composable
    fun getDimensionsHeaderAndTexts(
        dimensionsList: List<ObservedDimension>
    ) : Pair<AnnotatedString, List<String>> {
        val dimensionsHeader = stringResource(
            R.string.dimensions_header
        )
        val dimensionsHeaderText = dimensionsHeader.fromHtml()

        val dimensionTexts = when (dimensionsList.size) {
            0 -> listOf(
                stringResource(R.string.dimensions_none)
            )
            in 1..3 -> dimensionsList.map { dimension ->
                dimension.title
            }
            else -> {
                val dimensionAverages = mutableListOf<String>()

                val averageLengths = dimensionsList.filter { dimension ->
                    dimension.length > 0.0
                }
                val averageLength = averageLengths.map { dimension ->
                    dimension.length
                }.average()
                if (averageLength > 0.0) {
                    val sizeUnitText = getDimensionSizeUnit(averageLengths)
                    dimensionAverages.add(stringResource(
                        R.string.dimensions_avg_length,
                        averageLength,
                        sizeUnitText,
                        averageLengths.size,
                        pluralStringResource(
                            R.plurals.dimension_samples,
                            averageLengths.size
                        )
                    ))
                }

                val averageWidths = dimensionsList.filter { dimension ->
                    dimension.width > 0.0
                }
                val averageWidth = averageWidths.map { dimension ->
                    dimension.width
                }.average()
                if (averageWidth > 0.0) {
                    val sizeUnitText = getDimensionSizeUnit(averageWidths)
                    dimensionAverages.add(stringResource(
                        R.string.dimensions_avg_width,
                        averageWidth,
                        sizeUnitText,
                        averageWidths.size,
                        pluralStringResource(
                            R.plurals.dimension_samples,
                            averageWidths.size
                        )
                    ))
                }

                val averageHeights = dimensionsList.filter { dimension ->
                    dimension.height > 0.0
                }
                val averageHeight = averageHeights.map { dimension ->
                    dimension.height
                }.average()
                if (averageHeight > 0.0) {
                    val sizeUnitText = getDimensionSizeUnit(averageHeights)
                    dimensionAverages.add(stringResource(
                        R.string.dimensions_avg_height,
                        averageHeight,
                        sizeUnitText,
                        averageHeights.size,
                        pluralStringResource(
                            R.plurals.dimension_samples,
                            averageHeights.size
                        )
                    ))
                }

                val averageWeights = dimensionsList.filter { dimension ->
                    dimension.weight > 0.0
                }
                val averageWeight = averageWeights.map { dimension ->
                    dimension.weight
                }.average()
                if (averageWeight > 0.0) {
                    val weightUnits = averageWeights.filter { dimension ->
                        dimension.weightUnitText.isNotBlank()
                    }
                    val weightUnitText = when (weightUnits.isNotEmpty()) {
                        true -> weightUnits[0].weightUnitText
                        else -> String()
                    }
                    dimensionAverages.add(stringResource(
                        R.string.dimensions_avg_weight,
                        averageWeight,
                        weightUnitText,
                        averageWeights.size,
                        pluralStringResource(
                            R.plurals.dimension_samples,
                            averageWeights.size
                        )
                    ))
                }

                when (dimensionAverages.isNotEmpty()) {
                    true -> dimensionAverages
                    else -> listOf(
                        stringResource(
                            /* Just for safety */
                            R.string.dimensions_none
                        )
                    )
                }
            }
        }

        return Pair(dimensionsHeaderText, dimensionTexts)
    }

    private fun getDimensionSizeUnit(
        averageDimensions: List<ObservedDimension>
    ) : String {
        val sizeUnits = averageDimensions.filter { dimension ->
            dimension.sizeUnitText.isNotBlank()
        }
        return when (sizeUnits.isNotEmpty()) {
            true -> sizeUnits[0].sizeUnitText
            else -> String()
        }
    }

    @Composable
    private fun getReferenceString(
        media: Media,
        quantity: Int
    ) : String = pluralStringResource(
        R.plurals.referenced_by_item_other,
        quantity,
        media.toString(),
        quantity
    )

    @Composable
    fun getReferenceText(
        headerText: String,
        results: List<SearchResult>
    ) : AnnotatedString {
        /* Returns a UNIQUE list of [SearchResult]s, by mediaType */
        val isReferencedBySet = results.distinctBy { ref ->
            ref.media
        }

        /* Returns a list of strings: unique mediaTypes by name,
           formatted with quantity of mediaType in brackets */
        val isReferencedByNames = isReferencedBySet.map { ref ->
            val filtered = results.filter { referencedBy ->
                referencedBy.media == ref.media
            }
            when (ref.media) {
                MediaType.Category -> when (filtered.size) {
                    1 -> getReferenceString(
                        media = ref.media,
                        /* e.g. "Category (1) */
                        quantity = filtered.size
                    )
                    else -> stringResource(
                        R.string.referenced_by_item_cat,
                        /* e.g. "Categories (3) */
                        filtered.size
                    )
                }
                else -> getReferenceString(
                    media = ref.media,
                    quantity = filtered.size
                )
            }
        }

        /* Put together a bulleted list based on strings */
        val sb = StringBuilder()
        sb.append(headerText)
        sb.append("<UL>")
        isReferencedByNames.forEach { nameWithQty ->
            sb.append("<LI>$nameWithQty</LI>")
        }
        sb.append("</UL>")
        /* e.g.
          "Associated parties: (or "Referenced by ...")
           • Categories (3)
           • Object (1)" */

        return sb.toString().fromHtml()
    }

    fun getSubtitle1Text(
        result: SearchResult,
        searchType: SearchType
    ) : String = when (searchType) {
        SearchType.MediaObject -> {
            (result as SearchResult.Object).collectionLabel
        }
        SearchType.MediaSpecimen -> {
            (result as SearchResult.Specimen).collectionLabel
        }
        SearchType.MediaType -> when (result.media) {
            MediaType.Category -> {
                val relatedTerms = (result as SearchResult.Category)
                    .relatedTerms.map { related ->
                        related.title
                    }
                relatedTerms.joinToString(
                    RECORD_SEPARATOR
                )
            }
            MediaType.ImageObject -> {
                (result as SearchResult.ImageObject).fileFormat
            }
            MediaType.Object -> {
                (result as SearchResult.Object).collectionLabel
            }
            MediaType.Organisation -> {
                val associatedParties = (result as SearchResult.Organisation)
                    .associatedParties.map { associated ->
                        associated.title
                    }
                associatedParties.joinToString(
                    RECORD_SEPARATOR
                )
            }
            MediaType.Person, MediaType.Position -> {
                (result as SearchResult.Person).verbatimBirthDate
            }
            MediaType.Place -> {
                val nations = (result as SearchResult.Place).nation
                nations.joinToString(
                    RECORD_SEPARATOR
                )
            }
            MediaType.Publication -> {
                val authors = (result as SearchResult.Publication)
                    .authors.map { author ->
                        author.title
                    }
                authors.joinToString(
                    RECORD_SEPARATOR
                )
            }
            MediaType.Specimen -> {
                (result as SearchResult.Specimen).collectionLabel
            }
            MediaType.Taxon -> {
                (result as SearchResult.Taxon).kingdom
            }
            MediaType.TextDigitalDocument -> {
                (result as SearchResult.TextDigitalDocument).fileFormat
            }
            MediaType.Topic -> {
                val collectionLabels =
                    (result as SearchResult.Topic).collectionLabel
                collectionLabels.joinToString(
                    RECORD_SEPARATOR
                )
            }
            /* Collaboration and Group */
            else -> instance.getString(R.string.no_further_info)
        }
    }

    fun getSubtitle2Text(
        result: SearchResult,
        searchType: SearchType
    ) : String = when (searchType) {
        SearchType.MediaObject -> {
            val categories = (result as SearchResult.Object)
                .isTypeOf.map { isTypeOf ->
                    isTypeOf.title
                }
            categories.joinToString(
                RECORD_SEPARATOR
            )
        }
        SearchType.MediaSpecimen -> {
            (result as SearchResult.Specimen).basisOfRecord
        }
        SearchType.MediaType -> when (result.media) {
            MediaType.Category -> {
                val relatedTerms = (result as SearchResult.Category)
                    .relatedTerms.map { related ->
                        related.title
                    }
                relatedTerms.joinToString(
                    RECORD_SEPARATOR
                )
            }
            MediaType.ImageObject -> {
                (result as SearchResult.ImageObject)
                    .facetPermissionType.joinToString(
                        RECORD_SEPARATOR
                    )
            }
            MediaType.Object -> {
                val categories = (result as SearchResult.Object)
                    .isTypeOf.map { isTypeOf ->
                        isTypeOf.title
                    }
                categories.joinToString(
                    RECORD_SEPARATOR
                )
            }
            MediaType.Organisation -> {
                (result as SearchResult.Organisation)
                    .verbatimBirthDate
            }
            MediaType.Person, MediaType.Position -> {
                (result as SearchResult.Person)
                    .nationality.joinToString(
                        RECORD_SEPARATOR
                    )
            }
            MediaType.Place -> {
                val geoLocation =
                    (result as SearchResult.Place).geoLocation
                String.format(
                    instance.getString(R.string.geo_location_available),
                    instance.getString(when(
                        geoLocation.lat != 0.0
                        &&
                        geoLocation.lon != 0.0
                    ) {
                        true -> R.string.geo_location_yes
                        else -> R.string.geo_location_no
                    })
                )
            }
            MediaType.Publication -> {
                val collectionLabels =
                    (result as SearchResult.Publication)
                        .collectionLabel
                collectionLabels.joinToString(
                    RECORD_SEPARATOR
                )
            }
            MediaType.Specimen -> {
                (result as SearchResult.Specimen).basisOfRecord
            }
            MediaType.Taxon -> {
                (result as SearchResult.Taxon).basisOfRecord
            }
            MediaType.TextDigitalDocument -> {
                (result as SearchResult.TextDigitalDocument)
                    .facetPermissionType.joinToString(
                        RECORD_SEPARATOR
                    )
            }
            MediaType.Topic -> {
                (result as SearchResult.Topic)
                    .narrativeSummary
            }
            /* Collaboration and Group not found yet */
            else -> String()
        }
    }

    @Composable
    fun getTextFromList(
        list: List<String>,
        listFlattened: String,
        @PluralsRes pluralsId: Int
    ) : AnnotatedString {
        return pluralStringResource(
            pluralsId,
            /* plural "zero" doesn't work */
            when (list.isEmpty()) {
                true -> 1
                else -> list.size
            },
            when (list.isEmpty()) {
                true -> stringResource(R.string.common_undefined)
                else -> listFlattened
            }
        ).fromHtml()
    }

    @Composable
    fun getTextFromString(
        @StringRes stringId: Int,
        string: String,
        capitalise: Boolean = false
    ) : AnnotatedString {
        return stringResource(
            stringId,
            when (string.isBlank()) {
                true -> stringResource(R.string.common_undefined)
                else -> when (capitalise) {
                    true -> string.capitalise()
                    else -> string
                }
            }
        ).fromHtml()
    }

    @Composable
    fun getTextFromStringWithTwo(
        @StringRes stringId: Int,
        string1: String,
        string2: String
    ) : AnnotatedString {
        return stringResource(
            stringId,
            when (string2.isBlank()) {
                true -> stringResource(R.string.common_undefined)
                else -> stringResource(
                    R.string.common_two_args,
                    string1,
                    string2
                )
            }
        ).fromHtml()
    }

    @Composable
    fun getTextFromStringWithPipes(
        @PluralsRes pluralsId: Int,
        string: String
    ) : AnnotatedString {
        val list = string.split(PIPE_SEPARATOR)
        return pluralStringResource(
            pluralsId,
            /* plural "zero" doesn't work */
            when (string.isBlank()) {
                true -> 1
                else -> list.size
            },
            when (string.isBlank()) {
                true -> stringResource(R.string.common_undefined)
                else -> string
            }
        ).fromHtml()
    }

    @Suppress("KotlinConstantConditions") /* Refer second note */
    @Composable
    fun getTitleText(
        media: Media,
        title: String,
        angleBrackets: Boolean = false,
        capitalise: Boolean = false
    ) : AnnotatedString {
        /* Have come across at least one listing with NO title. Also,
           at least one listing with lower case title. Some titles,
           fishing for example, are surrounded by angle brackets */
        val titleText = when (title.isBlank()) {
            true -> stringResource(R.string.common_undefined)
            else -> if (!angleBrackets && !capitalise) {
                title
            } else if (angleBrackets && !capitalise) {
                title.replaceAngleBrackets()
            /* ConstantConditions relates to capitalise:
               full "wording" maintained for readability */
            } else if (!angleBrackets && capitalise) {
                title.capitalise()
            } else { /* brackets must come before capitalise */
                title.replaceAngleBrackets().capitalise()
            }
        }
        return stringResource(
            R.string.media_title,
            media.toString(),
            titleText
        ).fromHtml()
    }

    @Composable
    fun getWebAnnotatedString(
        contentUrl: String,
        linkText: String
    ) : AnnotatedString {
        return buildAnnotatedString {
            val uriHandler = LocalUriHandler.current
            val link = LinkAnnotation.Url(
                contentUrl,
                TextLinkStyles(
                    SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = Color.Blue
                    )
                )
            ) { url ->
                uriHandler.openUri((url as LinkAnnotation.Url).url)
            }
            withLink(link) { append(linkText) }
        }
    }
}