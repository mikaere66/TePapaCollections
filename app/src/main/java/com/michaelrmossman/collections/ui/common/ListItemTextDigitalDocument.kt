package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.TextDigitalDocument
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.ListItemUtils.getFileFormatText
import com.michaelrmossman.collections.util.ListItemUtils.getPermissionsText
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.TextUtils.getTextFromString

@Composable
fun ListItemTextDigitalDocument(
    textDigitalDoc: TextDigitalDocument,
    modifier: Modifier = Modifier
) {
    val formatText = getFileFormatText(
        fileFormat = textDigitalDoc.fileFormat
    )
//    getTextFromString(
//        stringId = R.string.file_format,
//        string = textDigitalDoc.fileFormat
//    )

    val permissions = textDigitalDoc.facetPermissionType
    val permissionsFlattened = permissions.joinToString(
        ITEM_SEPARATOR
    )
    /* e.g. "Permissions: CreativeCommons, Downloadable" */
    val permissionsText = getPermissionsText(
        permissions = permissions,
        permissionsFlattened = permissionsFlattened
    )
//    getTextFromList(
//        list = textDigitalDoc.facetPermissionType,
//        listFlattened = permissions,
//        pluralsId = R.plurals.file_permissions
//    )

    TypeIconWithTitle(
        result = textDigitalDoc,
        modifier = modifier
    )
    Text(
        text = formatText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
    Text(
        text = permissionsText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.list_item_padding)
        )
    )
}