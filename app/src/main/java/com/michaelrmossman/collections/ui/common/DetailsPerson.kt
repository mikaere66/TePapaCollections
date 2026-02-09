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
import com.michaelrmossman.collections.enum.ReferType
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.Person
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getReferenceText
import com.michaelrmossman.collections.util.fromHtml

/* Used by BOTH person AND position (as in rank) */
@Composable
fun DetailsPerson(
    isNestedContent: Boolean,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    person: Person,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | verbatimBirthDate | nationality */

    val ethnicity = when (person.ethnicity.size) {
        0 -> stringResource(R.string.common_undefined)
        else -> person.ethnicity.joinToString(
            ITEM_SEPARATOR
        )
    }
    val ethnicityText = pluralStringResource(
        R.plurals.ethnicities,
        ethnicity.split(ITEM_SEPARATOR).size,
        ethnicity
    ).fromHtml()

    val gender = when (person.gender.isBlank()) {
        true -> stringResource(R.string.common_undefined)
        else -> person.gender
    }
    val genderText = stringResource(
        R.string.gender,
        gender
    ).fromHtml()

    val deathDate = when (person.verbatimDeathDate.isBlank()) {
        true -> stringResource(R.string.common_not_applicable)
        else -> person.verbatimDeathDate
    }
    val deathDateText = stringResource(
        R.string.death_date,
        deathDate
    ).fromHtml()

    val deathPlace = when (person.deathPlace.isBlank()) {
        true -> stringResource(
            when (person.verbatimDeathDate.isBlank()) {
                true -> R.string.common_not_applicable
                else -> R.string.common_undefined
            }
        )
        else -> person.deathPlace
    }
    val deathPlaceText = stringResource(
        R.string.death_place,
        deathPlace
    ).fromHtml()

    val associatedPartiesText = getReferenceText(
        headerText = pluralStringResource(
            R.plurals.associated_parties_header,
            person.associatedParties.size
        ),
        results = person.associatedParties
    )
    var showAssociatedBS by rememberSaveable { mutableStateOf(false) }
    if (showAssociatedBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showAssociatedBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.AssociatedParties,
            refsList = person.associatedParties
        )
    }

    val isReferencedByText = getReferenceText(
        headerText = stringResource(R.string.referenced_by),
        results = person.isReferencedBy
    )
    var showIsReferByBS by rememberSaveable { mutableStateOf(false) }
    if (showIsReferByBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showIsReferByBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.IsReferencedBy,
            refsList = person.isReferencedBy
        )
    }

    val relatedWebPage = when (person.related.isNotEmpty()) {
        true -> person.related
        else -> null
    }

    /* = | = | = | = | = | = | = | = */

    Text(
        text = ethnicityText,
        modifier = modifier
    )
    Text(
        text = genderText,
        modifier = modifier
    )
    Text(
        text = deathDateText,
        modifier = modifier
    )
    Text(
        text = deathPlaceText,
        modifier = modifier
    )
    if (person.associatedParties.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showAssociatedBS = true },
            refsText = associatedPartiesText
        )
    }
    if (person.isReferencedBy.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showIsReferByBS = true },
            refsText = isReferencedByText
        )
    }
    relatedWebPage?.let { related ->
        RelatedWebPage(
            related = related
        )
    }
    MetaDataFooter(
        result = person
    )
}