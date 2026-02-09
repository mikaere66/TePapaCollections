package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.AnnotatedString
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.util.TextUtils.getTextFromString

@Composable
fun RefersToListItem(
    reference: SearchResult,
    modifier: Modifier = Modifier
) {
    var birthDateText: AnnotatedString? = null
    var scopeNoteText: AnnotatedString? = null

    if (reference is SearchResult.Category) {
        scopeNoteText = getTextFromString(
            stringId = R.string.scope_note,
            string = reference.scopeNote
        )
    }

    if (reference is SearchResult.Organisation) {
        birthDateText = getTextFromString(
            stringId = R.string.established_date,
            string = reference.verbatimBirthDate
        )
    }

    if (reference is SearchResult.Person) {
        birthDateText = getTextFromString(
            stringId = R.string.birth_date,
            string = reference.verbatimBirthDate
        )
    }

    if (reference is SearchResult.Place) {
        scopeNoteText = getTextFromString(
            stringId = R.string.scope_note,
            string = reference.scopeNote
        )
    }

    birthDateText?.let { birthDate ->
        Text(
            text = birthDate,
            modifier = modifier.padding(
                dimensionResource(R.dimen.list_item_padding)
            )
        )
    }
    scopeNoteText?.let { scopeNote ->
        Text(
            text = scopeNote,
            modifier = modifier.padding(
                dimensionResource(R.dimen.list_item_padding)
            )
        )
    }
}