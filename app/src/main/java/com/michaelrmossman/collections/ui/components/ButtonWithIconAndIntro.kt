package com.michaelrmossman.collections.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.michaelrmossman.collections.R

@Composable
fun ButtonWithIconAndIntro(
    buttonWidth: Dp,
    @DrawableRes drawableId: Int,
    @StringRes introStringId: Int,
    onClickButton: () -> Unit,
    @StringRes titleStringId: Int,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            dimensionResource(R.dimen.padding_mini)
        ),
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.spacing_vertical_mini)
        )
    ) {
        ButtonWithIcon(
            buttonWidth = buttonWidth,
            drawableId = drawableId,
            isEnabled = isEnabled,
            onClickButton = onClickButton,
            stringId = titleStringId,
            modifier = modifier
        )
        Text(
            text = stringResource(
                R.string.intro_with_ellipses,
                stringResource(introStringId)
            )
        )
    }
}