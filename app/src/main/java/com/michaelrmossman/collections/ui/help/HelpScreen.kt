package com.michaelrmossman.collections.ui.help

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.MediaObject
import com.michaelrmossman.collections.enum.MediaSpecimen
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.ui.components.DynamicActionMenu
import com.michaelrmossman.collections.ui.components.TwoLineAppBar
import com.michaelrmossman.collections.util.fromHtml
import com.michaelrmossman.collections.util.DialogUtils.IconsLegendDialog

@Composable
fun HelpScreen(
    onClickBackButton: () -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val cardPadding = dimensionResource(R.dimen.padding_small)
    val columnVerticalPadding = dimensionResource(
        R.dimen.padding_small
    )
    val columnVerticalSpacing = dimensionResource(
        R.dimen.spacing_vertical_small
    )
    val helpSections = stringArrayResource(R.array.help_sections)
    val lazyListState = rememberLazyListState()
    val textHorizontalPadding = dimensionResource(R.dimen.padding_medium)
    val textVerticalPadding = dimensionResource(R.dimen.padding_small)

    val sampleMenuLabels = stringArrayResource(R.array.help_samples)
    val sampleEnumEntries = listOf(
        MediaType.entries,
        MediaObject.entries,
        MediaSpecimen.entries
    )
    val sampleMenuBooleans = List(sampleMenuLabels.size) {
        sampleEnumEntries.size == sampleMenuLabels.size
    }
    var showSampleMenu by remember { mutableIntStateOf(-1) }
    if (showSampleMenu != -1) {
        IconsLegendDialog(
            entries = sampleEnumEntries[showSampleMenu],
            onClickConfirm = { showSampleMenu = -1 },
            title = sampleMenuLabels[showSampleMenu]
        )
    }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    if (sampleMenuBooleans.all { true }) {
                        DynamicActionMenu(
                            isEnabled = sampleMenuBooleans,
                            menuLabels = sampleMenuLabels.toList(),
                            onClickActions = (
                                0 until sampleMenuLabels.size
                            ).map { index ->
                                { showSampleMenu = index }
                            }
                        )
                    }
                },
                onClickBackButton = { onClickBackButton() },
                stringId = R.string.app_name,
                subtitle = stringResource(stringId)
            )
        }
    ) { contentPadding ->

        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = columnVerticalPadding),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(
                columnVerticalSpacing
            )
        ) {
            itemsIndexed(
                items = helpSections
            ) { index, helpSection ->

                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = dimensionResource(
                            R.dimen.card_elevation
                        )
                    ),
                    modifier = Modifier.padding(
                        horizontal = cardPadding
                    ),
                    shape = RoundedCornerShape(
                        dimensionResource(R.dimen.card_corner_shape)
                    )
                ) {

                    Text(
                        text = helpSection.fromHtml(),
                        textAlign = TextAlign.Justify,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = textHorizontalPadding,
                                vertical = textVerticalPadding
                            )
                    )
                }
            }
        }
    }
}