package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.ReferType
import com.michaelrmossman.collections.model.SearchResult

/* Used for Person | Organisation for associatedParties */

/* Used for Object | Organisation | Person | Specimen for isReferencedBy */

/* Used for Object | Publication | Topic for refersTo */

/* Used for Publication | Topic for relatedObjects */
/* Used for Publication | Topic for relatedTopics */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferenceBottomSheet(
    isNestedContent: Boolean,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    onDismissRequest: () -> Unit,
    referType: ReferType,
    refsList: List<SearchResult>,
    modifier: Modifier = Modifier
) {
    val headerText = when (referType) {
        ReferType.AssociatedParties -> pluralStringResource(
            R.plurals.associated_parties_header,
            refsList.size,
            refsList.size
        )
        ReferType.IsReferencedBy -> stringResource(
            R.string.referenced_by_header,
            refsList.size
        )
        ReferType.RefersTo -> stringResource(
            R.string.refers_to_header,
            refsList.size
        )
        ReferType.RelatedObjects -> pluralStringResource(
            R.plurals.related_objects_header,
            refsList.size,
            refsList.size
        )
        ReferType.RelatedTopics -> pluralStringResource(
            R.plurals.related_topics_header,
            refsList.size,
            refsList.size
        )
    }
    val iconLargePadding = dimensionResource(R.dimen.padding_great)
    val iconSize = dimensionResource(R.dimen.icon_size_small)
    val sheetState = rememberModalBottomSheetState()
    val textHorizontalPadding = dimensionResource(R.dimen.padding_medium)
    val listSorted = refsList.sortedBy { reference -> reference.title }
    val rowVerticalPadding = dimensionResource(R.dimen.padding_small)
    val verticalSpacing = dimensionResource(R.dimen.spacing_vertical_small)

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            item(key = -1) {
                Row(
                    modifier = Modifier.padding(
                        vertical = rowVerticalPadding
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        headerText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(
                                horizontal = textHorizontalPadding
                            )
                            .weight(1F)
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(horizontal = iconLargePadding)
                            .size(iconSize),
                        onClick = { onDismissRequest() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(
                                R.string.bottom_sheet_dismiss
                            )
                        )
                    }
                }
            }

            itemsIndexed(
                items = listSorted
            ) { index, reference ->

                ReferenceCard(
                    hrefIndex = index,
                    isNestedContent = isNestedContent,
                    onClickHrefItem = onClickHrefItem,
                    reference = reference,
                    refsList = listSorted,
                    content = {
                        /* Modifiers used by all list item contents */
                        when (referType) {
                            ReferType.AssociatedParties -> {
                                AssociatedPartyListItem(
                                    reference = reference,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            ReferType.IsReferencedBy -> {
                                ReferencedByListItem(
                                    reference = reference,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            ReferType.RefersTo -> {
                                RefersToListItem(
                                    reference = reference,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            ReferType.RelatedObjects,
                            ReferType.RelatedTopics -> {
                                ReferencedByListItem(
                                    reference = reference,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}