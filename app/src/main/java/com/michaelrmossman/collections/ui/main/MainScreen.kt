package com.michaelrmossman.collections.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.navigation.CurrentScreen
import com.michaelrmossman.collections.ui.components.ButtonWithIconAndIntro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    currentScreenItems: List<CurrentScreen>,
    onClickActions: List<() -> Unit>,
    screensEnabled: List<Boolean>,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val buttonWidth = dimensionResource(R.dimen.button_width)
    val collectionsHeader = @Composable {
        Text(
            text = stringResource(
                R.string.intro_with_ellipses,
                stringResource(
                    R.string.nav_collections_header
                )
            )
        )
    }
    val composables = mutableListOf<@Composable ()-> Unit>()
    if (
        currentScreenItems.size == onClickActions.size
        &&
        onClickActions.size == screensEnabled.size
    ) {
        currentScreenItems.forEachIndexed { index, currentScreen ->
            composables.add(
                {
                    ButtonWithIconAndIntro(
                        buttonWidth   = buttonWidth,
                        drawableId    = currentScreen.drawableId,
                        isEnabled     = screensEnabled[index],
                        introStringId = currentScreen.introStringId,
                        onClickButton = onClickActions[index],
                        titleStringId = currentScreen.titleStringId
                    )
                }
            )
        }
        /* Insert collections header as second item in list */
        composables.add(1,collectionsHeader)
    }
    val scrollState = rememberScrollState()
    val sectionPadding = dimensionResource(R.dimen.padding_main_section)

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(stringId),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        )
    }) { contentPadding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            if (composables.isNotEmpty()) {
                /* By Type */
                composables[0].invoke()

                if (composables.size > 3) {
                    /* Inner column compacted, to highlight Collections */
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        /* Collections header */
                        composables[1].invoke()
                        /* Objects */
                        composables[2].invoke()
                        /* Specimens */
                        composables[3].invoke()
                    }

                    if (composables.size > 4) {
                        /* Inner column compacted, to group Settings, etc */
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = modifier.fillMaxWidth()
                        ) {
                            for (i in 4 until composables.size) {
                                /* Faves | Settings | Help */
                                composables[i].invoke()
                            }
                        }
                    }
                }
            }
        }
    }
}