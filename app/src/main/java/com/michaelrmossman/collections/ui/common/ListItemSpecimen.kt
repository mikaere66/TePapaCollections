package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.util.ListItemUtils.getBasisOfRecordText
import com.michaelrmossman.collections.util.ListItemUtils.getCollectionText
import com.michaelrmossman.collections.model.SearchResult.Specimen
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun ListItemSpecimen(
    specimen: Specimen,
    searchType: SearchType,
    modifier: Modifier = Modifier
) {
    /* Have come across at least one listing with NO title */

    val collectionText = getCollectionText(
        collectionLabel = specimen.collectionLabel
    )
//    stringResource(
//        R.string.collection_label,
//        specimen.collectionLabel
//    ).fromHtml()

    val basisOfRecordText = getBasisOfRecordText(
        basisOfRecord = specimen.basisOfRecord
    )
//    stringResource(
//        R.string.basis_of_record,
//        specimen.basisOfRecord
//    ).fromHtml()

    TypeIconWithTitle(
        result = specimen,
        searchType = searchType,
        modifier = modifier
    )
    Text(
        text = collectionText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.padding_mini)
        )
    )
    Text(
        text = basisOfRecordText,
        modifier = modifier.padding(
            dimensionResource(R.dimen.padding_mini)
        )
    )
}