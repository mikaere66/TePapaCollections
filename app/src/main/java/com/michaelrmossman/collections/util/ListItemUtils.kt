package com.michaelrmossman.collections.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.TextUtils.getTextFromString

/**
 * Text utility functions used throughout the app
 */
object ListItemUtils {

    @Composable
    fun getBirthDateText(
        birthDate: String
    ) : AnnotatedString {
        return getTextFromString(
            stringId = R.string.birth_date,
            string = birthDate
        )
    }

    @Composable
    fun getAssociatedText(
        associatedParties: List<String>,
        associatedFlattened: String
    ) : AnnotatedString {
        return getTextFromList(
            list = associatedParties,
            listFlattened = associatedFlattened,
            pluralsId = R.plurals.associated_parties
        )
    }

    @Composable
    fun getAuthorsText(
        authors: List<String>,
        authorsFlattened: String
    ) : AnnotatedString {
        return getTextFromList(
            list = authors,
            listFlattened = authorsFlattened,
            pluralsId = R.plurals.publication_authors
        )
    }

    @Composable
    fun getBasisOfRecordText(
        basisOfRecord: String
    ) : AnnotatedString {
        return getTextFromString(
            stringId = R.string.basis_of_record,
            string = basisOfRecord
        )
    }

    fun getBroaderTermsList(
        category: SearchResult.Category
    ) : List<String> {
        return category.broaderTerms.map { broaderTerm ->
            broaderTerm.title.replaceAngleBrackets()
        }.sortedWith(
            String.CASE_INSENSITIVE_ORDER // Note sort
        )
    }
    @Composable
    fun getBroaderTermsText(
        broaderTerms: List<String>,
        broaderTermsFlattened: String
    ) : AnnotatedString {
        return getTextFromList(
            list = broaderTerms,
            listFlattened = broaderTermsFlattened,
            pluralsId = R.plurals.terms_broader
        )
    }

    @Composable
    fun getCategoriesText(
        categories: List<String>,
        categoriesFlattened: String
    ) : AnnotatedString {
        return getTextFromList(
            list = categories,
            listFlattened = categoriesFlattened,
            pluralsId = R.plurals.categories
        )
    }

    @Composable
    fun getCollectionText(
        collectionLabel: String
    ) : AnnotatedString {
        return getTextFromString(
            stringId = R.string.collection_label,
            string = collectionLabel
        )
    }

    @Composable
    fun getCollectionsText(
        collectionLabels: List<String>,
        collectionLabelsFlattened: String
    ) : AnnotatedString {
        return getTextFromList(
            list = collectionLabels,
            listFlattened = collectionLabelsFlattened,
            pluralsId = R.plurals.collection_labels
        )
    }

    @Composable
    fun getEstablishedDateText(
        establishedDate: String
    ) : AnnotatedString {
        return getTextFromString(
            stringId = R.string.established_date,
            string = establishedDate
        )
    }

    @Composable
    fun getFileFormatText(
        fileFormat: String
    ) : AnnotatedString {
        return getTextFromString(
            stringId = R.string.file_format,
            string = fileFormat
        )
    }

    @Composable
    fun getGeoLocationText(
        latitude: Double,
        longitude: Double
    ) : AnnotatedString {
        return getTextFromString(
            stringId = R.string.geo_location_available,
            string = stringResource(when(
                latitude != 0.0
                &&
                longitude != 0.0
            ) {
                true -> R.string.geo_location_yes
                else -> R.string.geo_location_no
            }
        ))
    }

    @Composable
    fun getKingdomText(
        kingdom: String
    ) : AnnotatedString {
        return getTextFromString(
            stringId = R.string.taxon_kingdom,
            string = kingdom
        )
    }

    @Composable
    fun getNarrativeSummaryText(
        narrativeSummary: String
    ) : AnnotatedString {
        return getTextFromString(
            stringId = R.string.narrative_summary,
            string = narrativeSummary
        )
    }


    @Composable
    fun getNationalitiesText(
        nationalities: List<String>,
        nationalitiesFlattened: String
    ) : AnnotatedString {
        return getTextFromList(
            list = nationalities,
            listFlattened = nationalitiesFlattened,
            pluralsId = R.plurals.nationalities
        )
    }

    @Composable
    fun getNationsText(
        nations: List<String>,
        nationsFlattened: String
    ) : AnnotatedString {
        return getTextFromList(
            list = nations,
            listFlattened = nationsFlattened,
            pluralsId = R.plurals.place_nations,
        )
    }

    @Composable
    fun getPermissionsText(
        permissions: List<String>,
        permissionsFlattened: String
    ) : AnnotatedString {
        return getTextFromList(
            list = permissions,
            listFlattened = permissionsFlattened,
            pluralsId = R.plurals.file_permissions
        )
    }

    fun getRelatedTermsList(
        category: SearchResult.Category
    ) : List<String> {
        return category.relatedTerms.map { relatedTerm ->
            relatedTerm.title
        }.sortedWith(
            String.CASE_INSENSITIVE_ORDER // Note sort
        )
    }
    @Composable
    fun getRelatedTermsText(
        relatedTerms: List<String>,
        relatedTermsFlattened: String
    ) : AnnotatedString {
        return getTextFromList(
            list = relatedTerms,
            listFlattened = relatedTermsFlattened,
            pluralsId = R.plurals.related_terms
        )
    }
}