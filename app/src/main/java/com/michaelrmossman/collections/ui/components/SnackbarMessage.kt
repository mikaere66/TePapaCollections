package com.michaelrmossman.collections.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R

@Composable
fun SnackbarMessage(
    message: String,
    snackbarHostState: SnackbarHostState,
    /* Short = 4000ms | Long = 10000ms */
    duration: SnackbarDuration = SnackbarDuration.Indefinite
) {
    val actionLabel = stringResource(R.string.snackbar_got_it)

    LaunchedEffect(key1 = Unit) {
        snackbarHostState.showSnackbar(
            actionLabel = when (duration) {
                SnackbarDuration.Indefinite -> actionLabel
                else -> null
            },
            duration = duration,
            message = message
        )
    }
}