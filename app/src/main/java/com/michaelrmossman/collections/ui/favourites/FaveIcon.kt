package com.michaelrmossman.collections.ui.favourites

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import com.michaelrmossman.collections.R

@Composable
fun FaveIcon(
    isFave: Boolean,
    onClickToggleFavourite: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    IconButton(
        enabled = isEnabled,
        onClick = { onClickToggleFavourite() }
    ) {
        Icon(
            painter = painterResource(when (isFave) {
                true -> R.drawable.baseline_bookmark_remove_24
                else -> R.drawable.baseline_bookmark_add_24
            }),
            contentDescription = stringResource(when (isFave) {
                true -> R.string.faves_add_desc
                else -> R.string.faves_remove_desc
            }),
            modifier = modifier
        )
    }
}