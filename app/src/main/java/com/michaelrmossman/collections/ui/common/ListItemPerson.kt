package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.util.ListItemUtils.getBirthDateText
import com.michaelrmossman.collections.util.ListItemUtils.getNationalitiesText
import com.michaelrmossman.collections.model.SearchResult.Person
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.TextUtils.getTextFromString

@Composable
fun ListItemPerson(
    person: Person,
    modifier: Modifier = Modifier
) {
    val birthDateText = getBirthDateText(
        birthDate = person.verbatimBirthDate
    )
//    getTextFromString(
//        stringId = R.string.birth_date,
//        string = person.verbatimBirthDate
//    )

    val nationalities = person.nationality
    val nationalitiesFlattened = nationalities.joinToString(
        ITEM_SEPARATOR
    )
    val nationalitiesText = getNationalitiesText(
        nationalities = nationalities,
        nationalitiesFlattened = nationalitiesFlattened
    )
//    getTextFromList(
//        list = nationalities,
//        listFlattened = nationalitiesFlattened,
//        pluralsId = R.plurals.nationalities
//    )

    TypeIconWithTitle(
        result = person,
        modifier = modifier
    )
    Text(
        text = birthDateText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
    Text(
        text = nationalitiesText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
}