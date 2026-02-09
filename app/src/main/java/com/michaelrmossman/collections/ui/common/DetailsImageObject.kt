package com.michaelrmossman.collections.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getTextFromList

@Composable
fun DetailsImageObject(
    imageObject: ImageObject,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | fileFormat | facetPermissionType */

    val additionalTypes = imageObject.additionalType.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Additional type: PhysicalObject" */
    val additionalTypesText = getTextFromList(
        list = imageObject.additionalType,
        listFlattened = additionalTypes,
        pluralsId = R.plurals.additional_types
    )

    /* = | = | = | = | = | = | = | = */

    Text(
        text = additionalTypesText,
        modifier = modifier
    )
    MetaDataFooter(
        result = imageObject
    )
}