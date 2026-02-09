package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.Category
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.ListItemUtils.getBroaderTermsList
import com.michaelrmossman.collections.util.ListItemUtils.getBroaderTermsText
import com.michaelrmossman.collections.util.ListItemUtils.getRelatedTermsList
import com.michaelrmossman.collections.util.ListItemUtils.getRelatedTermsText
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.replaceAngleBrackets

@Composable
fun ListItemCategory(
    category: Category,
    modifier: Modifier = Modifier
) {
    val relatedTerms = getRelatedTermsList(category)
//    category.relatedTerms.map { related ->
//        related.title
//    }.sortedWith(
//        String.CASE_INSENSITIVE_ORDER // Note sort
//    )
    val relatedTermsFlattened = relatedTerms.joinToString(
        ITEM_SEPARATOR
    )
    val relatedTermsText = getRelatedTermsText(
        relatedTerms = relatedTerms,
        relatedTermsFlattened = relatedTermsFlattened
    )
//    getTextFromList(
//        list = relatedTerms,
//        listFlattened = relatedTermsFlattened,
//        pluralsId = R.plurals.related_terms
//    )

    val broaderTerms = getBroaderTermsList(category)
//    category.broaderTerms.map { broader ->
//        broader.title.replaceAngleBrackets()
//    }.sortedWith(
//        String.CASE_INSENSITIVE_ORDER // Note sort
//    )
    val broaderTermsFlattened = broaderTerms.joinToString(
        ITEM_SEPARATOR
    )
    val broaderTermsText = getBroaderTermsText(
        broaderTerms = broaderTerms,
        broaderTermsFlattened = broaderTermsFlattened
    )
//    getTextFromList(
//        list = broaderTerms,
//        listFlattened = broaderTerms.joinToString(
//            ITEM_SEPARATOR
//        ),
//        pluralsId = R.plurals.terms_broader
//    )

    TypeIconWithTitle(
        angleBrackets = true,
        capitalise = true,
        result = category,
        modifier = modifier
    )
    Text(
        text = relatedTermsText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
    Text(
        text = broaderTermsText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
}