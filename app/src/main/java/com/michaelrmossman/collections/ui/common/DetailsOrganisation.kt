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
import com.michaelrmossman.collections.model.SearchResult.Organisation
import com.michaelrmossman.collections.util.TextUtils.getReferenceText
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun DetailsOrganisation(
    isNestedContent: Boolean,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    organisation: Organisation,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | associatedParties | verbatimBirthDate */

    val deathDateText = stringResource(
        R.string.dissolved_date,
        when (organisation.verbatimDeathDate.isBlank()) {
            true -> stringResource(R.string.common_undefined)
            else -> organisation.verbatimDeathDate
        }
    ).fromHtml()

    val associatedPartiesText = getReferenceText(
        headerText = pluralStringResource(
            R.plurals.associated_parties_header,
            organisation.associatedParties.size
        ),
        results = organisation.associatedParties
    )
    var showAssociatedBS by rememberSaveable { mutableStateOf(false) }
    if (showAssociatedBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showAssociatedBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.AssociatedParties,
            refsList = organisation.associatedParties
        )
    }

    val isReferencedByText = getReferenceText(
        headerText = stringResource(R.string.referenced_by),
        results = organisation.isReferencedBy
    )
    var showIsReferByBS by rememberSaveable { mutableStateOf(false) }
    if (showIsReferByBS) {
        ReferenceBottomSheet(
            isNestedContent = isNestedContent,
            onDismissRequest = { showIsReferByBS = false },
            onClickHrefItem = onClickHrefItem,
            referType = ReferType.IsReferencedBy,
            refsList = organisation.isReferencedBy
        )
    }

    val relatedWebPage = when (organisation.related.isNotEmpty()) {
        true -> organisation.related
        else -> null
    }

    /* = | = | = | = | = | = | = | = */

    Text(
        text = deathDateText,
        modifier = modifier
    )
    if (organisation.associatedParties.isNotEmpty()) {
        ReferenceTextWithIcon(
            modifier = modifier,
            onClickReferences = { showAssociatedBS = true },
            refsText = associatedPartiesText
        )
    }
    if (organisation.isReferencedBy.isNotEmpty()) {
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
        result = organisation
    )
}