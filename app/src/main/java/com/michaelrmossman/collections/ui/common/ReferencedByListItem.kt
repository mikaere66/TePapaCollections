package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.AnnotatedString
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getTextFromList

@Composable
fun ReferencedByListItem(
    reference: SearchResult,
    modifier: Modifier = Modifier
) {
    var publicationDates: AnnotatedString? = null
    var purposesText: AnnotatedString? = null

    if (reference is SearchResult.Publication) {
        publicationDates = getTextFromList(
            list = reference.publicationDate,
            listFlattened = reference.publicationDate.joinToString(
                ITEM_SEPARATOR
            ),
            pluralsId = R.plurals.publication_dates
        )
        purposesText = getTextFromList(
            list = reference.purpose,
            listFlattened = reference.purpose.joinToString(
                ITEM_SEPARATOR
            ),
            pluralsId = R.plurals.common_purposes
        )
    }

    if (reference is SearchResult.Topic) {
        publicationDates = getTextFromList(
            list = reference.publicationDate,
            listFlattened = reference.publicationDate.joinToString(
                ITEM_SEPARATOR
            ),
            pluralsId = R.plurals.publication_dates
        )
        purposesText = getTextFromList(
            list = reference.purpose,
            listFlattened = reference.purpose.joinToString(
                ITEM_SEPARATOR
            ),
            pluralsId = R.plurals.common_purposes
        )
    }

    purposesText?.let { purposes ->
        Text(
            text = purposes,
            modifier = modifier.padding(
                dimensionResource(R.dimen.list_item_padding)
            )
        )
    }
    when (publicationDates) {
        null -> Spacer(
            modifier = Modifier.size(
                dimensionResource(R.dimen.padding_nano)
            )
        )
        else -> Text(
            text = publicationDates,
            modifier = modifier.padding(
                dimensionResource(R.dimen.list_item_padding)
            )
        )
    }
}