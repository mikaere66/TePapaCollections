package com.michaelrmossman.collections.ui.common

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.ui.components.TypeIcon
import com.michaelrmossman.collections.util.IconUtils.getMediaIconId
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun ReferenceCard(
    content: @Composable () -> Unit,
    hrefIndex: Int,
    isNestedContent: Boolean,
    onClickHrefItem: (List<SearchResult>, Int) -> Unit,
    reference: SearchResult,
    refsList: List<SearchResult>,
    modifier: Modifier = Modifier
) {
    val columnHorizontalPadding = dimensionResource(R.dimen.padding_medium)
    val columnVerticalPadding = dimensionResource(R.dimen.padding_small)
    val context = LocalContext.current
    var showToast by remember { mutableStateOf(false) }

    if (showToast) {
        Toast.makeText(
            context,
            R.string.reference_not_available,
            Toast.LENGTH_LONG
        ).show()
        @Suppress("AssignedValueIsNeverRead")
        showToast = false
    }

    val onClick = { refsList: List<SearchResult>, hrefIndex: Int ->
        when (isNestedContent) {
            true -> showToast = true
            else -> onClickHrefItem(
                refsList, hrefIndex
            )
        }
    }

    val titleText = stringResource(
        R.string.media_title,
        reference.media,
        reference.title
    ).fromHtml()

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.padding_small)
        ),
        onClick = {
            onClick(
                refsList, hrefIndex
            )
        },
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.card_corner_shape)
        )
    ) {
        Column(
            modifier = Modifier.padding(
                end = columnHorizontalPadding,
                start = columnHorizontalPadding,
                top = columnVerticalPadding
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_vertical_small)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.spacing_horizontal_midi)
                ),
                modifier = modifier
            ) {
                TypeIcon(
                    drawableId = getMediaIconId(reference.media)
                )
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = modifier.weight(1F)
                )
            }
            content()
        }
    }
}