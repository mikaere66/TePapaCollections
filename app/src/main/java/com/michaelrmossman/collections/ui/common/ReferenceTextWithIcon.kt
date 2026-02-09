package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.michaelrmossman.collections.R

/* Used for Person | Organisation for associatedParties */

/* Used for Object | Organisation | Person | Specimen for isReferencedBy */

/* Used for Object | Publication | Topic for refersTo */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferenceTextWithIcon(
    onClickReferences: () -> Unit,
    refsText: AnnotatedString,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = refsText,
            modifier = modifier.clickable {
                onClickReferences()
            }
            .weight(1F)
        )
        IconButton(
            onClick = onClickReferences
        ) {
            Icon(
                contentDescription = stringResource(
                    R.string.bottom_sheet_desc
                ),
                painter = painterResource(
                    R.drawable.outline_bottom_panel_open_24 // was outline_bottom_sheets_24
                )
            )
        }
    }
}