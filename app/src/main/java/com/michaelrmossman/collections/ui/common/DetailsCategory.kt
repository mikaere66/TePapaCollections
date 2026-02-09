package com.michaelrmossman.collections.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.Category
import com.michaelrmossman.collections.util.TextUtils.getTextFromString
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun DetailsCategory(
    category: Category,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | relatedTerms | broaderTerms */

    val scopeNotes = mutableListOf<String>()
    category.relatedTerms.forEach { relatedTerm ->
        if (relatedTerm.scopeNote.isNotBlank()) {
            scopeNotes.add(relatedTerm.scopeNote)
        }
    }

    val creditLineText = getTextFromString(
        stringId = R.string.credit_line,
        string = category.creditLine
    )

    /* = | = | = | = | = | = | = | = */

    if (scopeNotes.isNotEmpty()) {
        Text(
            text = stringResource(
                R.string.scope_notes
            ).fromHtml(),
            modifier = modifier
        )
        scopeNotes.forEach { scopeNote ->
            Text(
                text = scopeNote,
                modifier = modifier
            )
        }
    }
    Text(
        text = creditLineText,
        modifier = modifier
    )

    MetaDataFooter(
        result = category
    )
}