package com.michaelrmossman.collections.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.util.TextUtils.fontDimensionResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoLineAppBar(
    actions: @Composable RowScope.() -> Unit,
    onClickBackButton: () -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    val subtitleFontSize = fontDimensionResource(R.dimen.subtitle_font_size)

    TopAppBar(
        title = {
            Column {
                /* Title not used if TopAppBar is @ centre of large screen */
                if (stringId != 0) {
                    Text(
                        text = stringResource(stringId),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                subtitle?.let { sub ->
                    Text(
                        fontSize = subtitleFontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = sub
                    )
                }
            }
        },
        navigationIcon = {
            if (stringId != 0) { /* Same as above: NavigationIcon not used */
                BackButton(onClickBackButton = onClickBackButton)
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor =
                MaterialTheme.colorScheme.primaryContainer,
            titleContentColor =
                MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier
    )
}