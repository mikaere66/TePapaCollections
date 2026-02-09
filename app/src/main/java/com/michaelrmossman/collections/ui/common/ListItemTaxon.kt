package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.Taxon
import com.michaelrmossman.collections.util.ListItemUtils.getBasisOfRecordText
import com.michaelrmossman.collections.util.ListItemUtils.getKingdomText
import com.michaelrmossman.collections.util.TextUtils.getTextFromString

@Composable
fun ListItemTaxon(
    taxon: Taxon,
    modifier: Modifier = Modifier
) {
    val kingdomText = getKingdomText(
        kingdom = taxon.kingdom
    )
//    getTextFromString(
//        stringId = R.string.taxon_kingdom,
//        string = taxon.kingdom
//    )

    val basisOfRecordText = getBasisOfRecordText(
        basisOfRecord = taxon.basisOfRecord
    )
//    getTextFromString(
//        stringId = R.string.basis_of_record,
//        string = taxon.basisOfRecord
//    )

    TypeIconWithTitle(
        result = taxon,
        modifier = modifier
    )
    Text(
        text = kingdomText,
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