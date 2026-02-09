package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.model.SearchResult.Taxon
import com.michaelrmossman.collections.model.VernacularName
import com.michaelrmossman.collections.ui.components.TypeIcon
import com.michaelrmossman.collections.util.ITEM_SEPARATOR
import com.michaelrmossman.collections.util.TextUtils.getTextFromList
import com.michaelrmossman.collections.util.TextUtils.getTextFromString
import com.michaelrmossman.collections.util.TextUtils.getTextFromStringWithPipes
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun DetailsTaxon(
    taxon: Taxon,
    modifier: Modifier = Modifier
) {
    /* ListItem shows title | kingdom | basisOfRecord */

    val classText = getTextFromString(
        stringId = R.string.taxon_class,
        string = taxon.`class`
    )

    val familyText = getTextFromString(
        stringId = R.string.taxon_family,
        string = taxon.family
    )

    val genusText = getTextFromString(
        stringId = R.string.taxon_genus,
        string = taxon.genus
    )

    val higherClassificationText = getTextFromStringWithPipes(
        pluralsId = R.plurals.higher_classifications,
        string = taxon.higherClassification
    )

    val orderText = getTextFromString(
        stringId = R.string.taxon_order,
        string = taxon.order
    )

    val phylumText = getTextFromString(
        stringId = R.string.taxon_phylum,
        string = taxon.phylum
    )

    val taxonRankText = getTextFromString(
        stringId = R.string.taxon_rank,
        string = taxon.taxonRank
    )

    val vernacularNames = taxon.vernacularName.map { vernacularName ->
        when (vernacularName.language.isBlank()) {
            /* e.g. "Little Spotted Kiwi" */
            true -> vernacularName.title
            /* e.g. "kiwi pukupuku (Māori)" */
            else -> stringResource(
                R.string.common_two_args,
                vernacularName.title,
                vernacularName.language
            )
        }
    }
    val vernacularNamesText = getTextFromList(
        list = vernacularNames,
        listFlattened = vernacularNames.joinToString(
            ITEM_SEPARATOR
        ),
        pluralsId = R.plurals.vernacular_names
    )

    /* = | = | = | = | = | = | = | = */

    Text(
        text = classText,
        modifier = modifier
    )
    Text(
        text = familyText,
        modifier = modifier
    )
    Text(
        text = genusText,
        modifier = modifier
    )
    Text(
        text = higherClassificationText,
        modifier = modifier
    )
    Text(
        text = orderText,
        modifier = modifier
    )
    Text(
        text = phylumText,
        modifier = modifier
    )
    Text(
        text = taxonRankText,
        modifier = modifier
    )
    Text(
        text = vernacularNamesText,
        modifier = modifier
    )
    MetaDataFooter(
        result = taxon
    )
}