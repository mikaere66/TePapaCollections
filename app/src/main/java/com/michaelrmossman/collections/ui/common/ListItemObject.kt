package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.SearchResult.Object
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.ListItemUtils.getCategoriesText
import com.michaelrmossman.collections.util.ListItemUtils.getCollectionText
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.TextUtils.getTextFromString
import com.michaelrmossman.collections.util.capitalise

@Composable
fun ListItemObject(
    `object`: Object,
    searchType: SearchType,
    modifier: Modifier = Modifier
) {
    val collectionText = getCollectionText(
        collectionLabel = `object`.collectionLabel
    )
//    getTextFromString(
//        stringId = R.string.collection_label,
//        string = `object`.collectionLabel
//    )

    val categories = `object`.isTypeOf.map { isTypeOf ->
        isTypeOf.title
    }.map { category ->
        category.capitalise()
    }
    val categoriesFlattened = categories.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Categories: sculpture, automobiles" */
    val categoriesText = getCategoriesText(
        categories = categories,
        categoriesFlattened = categoriesFlattened
    )
//    getTextFromList(
//        list = categories,
//        listFlattened = categoriesFlattened,
//        pluralsId = R.plurals.categories
//    )

    TypeIconWithTitle(
        result = `object`,
        searchType = searchType,
        modifier = modifier
    )
    Text(
        text = collectionText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
    Text(
        text = categoriesText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
}