package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.util.ListItemUtils.getAssociatedText
import com.michaelrmossman.collections.util.ListItemUtils.getEstablishedDateText
import com.michaelrmossman.collections.model.SearchResult.Organisation
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getTextFromString

@Composable
fun ListItemOrganisation(
    organisation: Organisation,
    modifier: Modifier = Modifier
) {
    val undefinedString = stringResource(R.string.common_undefined)

    val associatedParties = organisation.associatedParties.map { party ->
        party.title
    }
    val associatedFlattened = associatedParties.joinToString(
        ITEM_SEPARATOR
    )
    val associatedText = getAssociatedText(
        associatedParties = associatedParties,
        associatedFlattened = associatedFlattened
    )
//    getTextFromString(
//        stringId = R.string.associated_with,
//        string = when (associated.isNotEmpty()) {
//            true -> associated.joinToString(
//                ITEM_SEPARATOR
//            )
//            else -> undefinedString
//        }
//    )

    val establishedDateText = getEstablishedDateText(
        establishedDate = organisation.verbatimBirthDate
    )
//    getTextFromString(
//        stringId = R.string.established_date,
//        string = organisation.verbatimBirthDate
//    )

    TypeIconWithTitle(
        result = organisation,
        modifier = modifier
    )
    Text(
        text = associatedText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
    Text(
        text = establishedDateText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
}