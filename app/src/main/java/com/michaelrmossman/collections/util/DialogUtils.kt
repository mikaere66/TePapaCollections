package com.michaelrmossman.collections.util

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.MediaObject
import com.michaelrmossman.collections.enum.MediaSpecimen
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.ui.theme.TePapaCollectionsTheme
import com.michaelrmossman.collections.util.DialogUtils.ConfirmDeleteAllFavesDialog
import com.michaelrmossman.collections.util.DialogUtils.ConfirmResetSettingsDialog
import com.michaelrmossman.collections.util.DialogUtils.IconsLegendDialog
import com.michaelrmossman.collections.util.DialogUtils.QueryTypeDialog
import com.michaelrmossman.collections.util.DialogUtils.SelectMediaDialog
import com.michaelrmossman.collections.util.IconUtils.getMediaIconId

/**
 * Dialog utility functions used throughout the app
 */
object DialogUtils {

    // const val TAG = "DialogUtils"

    @Composable
    fun CommonSimpleDialog(
        onClickConfirm: () -> Unit,
        onClickDismiss: () -> Unit,
        @StringRes confirmId: Int,
        @StringRes dismissId: Int,
        @StringRes textId: Int,
        @StringRes titleId: Int,
        modifier: Modifier = Modifier
    ) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the
                // dialog or on the back button. If you want to disable
                // that functionality, simply use empty onDismissRequest
                onClickDismiss()
            },
            title = {
                Text(text = stringResource(titleId).plus("?"))
            },
            text = {
                Text(
                    text = stringResource(textId).fromHtml(),
                    textAlign = TextAlign.Justify
                )
            },
            dismissButton = {
                TextButton (
                    onClick = { onClickDismiss() }
                ) {
                    Text(text = stringResource(dismissId))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { onClickConfirm() }
                ) {
                    Text(text = stringResource(confirmId))
                }
            }
        )
    }

    @Composable
    fun ConfirmDeleteAllFavesDialog(
        onClickConfirm: () -> Unit,
        onClickDismiss: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        CommonSimpleDialog(
            confirmId = R.string.dialog_confirm,
            dismissId = R.string.dialog_deny,
            modifier = modifier,
            onClickConfirm = onClickConfirm,
            onClickDismiss = onClickDismiss,
            textId = R.string.faves_message,
            titleId = R.string.menu_faves_delete_all
        )
    }

    @Composable
    fun ConfirmResetSettingsDialog(
        onClickConfirm: () -> Unit,
        onClickDismiss: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        CommonSimpleDialog(
            confirmId = R.string.dialog_confirm,
            dismissId = R.string.dialog_deny,
            modifier = modifier,
            onClickConfirm = onClickConfirm,
            onClickDismiss = onClickDismiss,
            textId = R.string.settings_reset_message,
            titleId = R.string.menu_reset_settings
        )
    }

    @Composable
    fun IconsLegendDialog(
        entries: Iterable<Media>,
        onClickConfirm: () -> Unit,
        title: String,
        modifier: Modifier = Modifier
    ) {
        val horizontalPadding = dimensionResource(
            R.dimen.dialog_icons_padding
        )
        val verticalPadding = dimensionResource(
            R.dimen.padding_medium
        )
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                onClickConfirm() /* Refer note at BOF */
            },
            title = { Text(text = title) },
            text = {
                Column {
                    entries.forEachIndexed { index, mediaType ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(
                                    getMediaIconId(mediaType)
                                ),
                                contentDescription = null,
                                modifier = Modifier.padding(
                                    end = horizontalPadding,
                                    start = horizontalPadding,
                                    top = when (index) {
                                        0 -> 0.dp
                                        else -> verticalPadding
                                    }
                                )
                            )
                            Text(
                                text = mediaType.toString(),
                                modifier = Modifier.padding(
                                    top = when (index) {
                                        0 -> 0.dp
                                        else -> verticalPadding
                                    }
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton (
                    onClick = { onClickConfirm() }
                ) {
                    Text(
                        text = stringResource(
                            R.string.dialog_got_it
                        )
                    )
                }
            }
        )
    }

    @Composable
    private fun QueryOrSelectMediaItem(
        horizontalPadding: Dp,
        index: Int,
        isSelected: Boolean,
        onClickConfirm: (Int) -> Unit,
        text: String,
        verticalPadding: Dp
    ) {
        Row(
            modifier = Modifier.clickable {
                onClickConfirm(index)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                contentDescription = null,
                imageVector = when (isSelected) {
                    true -> Icons.Outlined.RadioButtonChecked
                    else -> Icons.Outlined.RadioButtonUnchecked
                },
                modifier = Modifier.padding(
                    end = horizontalPadding,
                    start = horizontalPadding,
                    top = when (index) {
                        -1 -> 0.dp
                        else -> verticalPadding
                    }
                )
            )
            Text(
                text = text,
                modifier = Modifier.padding(
                    top = when (index) {
                        -1 -> 0.dp
                        else -> verticalPadding
                    }
                )
            )
        }
    }

    @Composable
    fun QueryTypeDialog(
        currentSelection: Int,
        onClickConfirm: (Int) -> Unit,
        onClickDismiss: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val horizontalPadding = dimensionResource(
            R.dimen.dialog_icons_padding
        )
        val verticalPadding = dimensionResource(
            R.dimen.padding_medium
        )
        val queryOptions = stringArrayResource(R.array.query_options)
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                onClickDismiss() /* Refer note at BOF */
            },
            title = {
                Text(text = stringResource(R.string.search_options))
            },
            text = {
                Column {
                    queryOptions.forEachIndexed { index, queryOption ->
                        QueryOrSelectMediaItem(
                            horizontalPadding = horizontalPadding,
                            index = index,
                            isSelected = (currentSelection == index),
                            onClickConfirm = onClickConfirm,
                            text = queryOption,
                            verticalPadding = verticalPadding
                        )
                    }
                }
            },
            confirmButton = {
                TextButton (
                    onClick = { onClickDismiss() }
                ) {
                    Text(text = stringResource(R.string.dialog_cancel))
                }
            }
        )
    }

    @Composable
    fun SelectMediaDialog(
        currentSelection: Media?,
        entries: Iterable<Media>,
        onClickConfirm: (Int) -> Unit,
        onClickDismiss: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val horizontalPadding = dimensionResource(
            R.dimen.dialog_icons_padding
        )
        val verticalPadding = dimensionResource(
            R.dimen.padding_medium
        )
        AlertDialog(
            modifier = modifier,
            onDismissRequest = {
                onClickDismiss() /* Refer note at BOF */
            },
            title = {
                Text(text = stringResource(R.string.search_media))
            },
            text = {
                Column {
                    QueryOrSelectMediaItem(
                        horizontalPadding = horizontalPadding,
                        index = -1,
                        isSelected = (currentSelection == null),
                        onClickConfirm = onClickConfirm,
                        text = stringResource(R.string.search_all),
                        verticalPadding = verticalPadding
                    )
                    entries.forEachIndexed { index, mediaType ->
                        QueryOrSelectMediaItem(
                            horizontalPadding = horizontalPadding,
                            index = index,
                            isSelected = (currentSelection == mediaType),
                            onClickConfirm = onClickConfirm,
                            text = mediaType.toString(),
                            verticalPadding = verticalPadding
                        )
                    }
                }
            },
            confirmButton = {
                TextButton (
                    onClick = { onClickDismiss() }
                ) {
                    Text(text = stringResource(R.string.dialog_cancel))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmDeletePreview() {
    TePapaCollectionsTheme {
        ConfirmDeleteAllFavesDialog(
            onClickConfirm = {},
            onClickDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmResetPreview() {
    TePapaCollectionsTheme {
        ConfirmResetSettingsDialog(
            onClickConfirm = {},
            onClickDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MediaObjectIconsLegendPreview() {
    TePapaCollectionsTheme {
        IconsLegendDialog(
            entries = MediaObject.entries,
            onClickConfirm = {},
            title = stringResource(R.string.menu_icons_legend)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MediaSpecimenIconsLegendPreview() {
    TePapaCollectionsTheme {
        IconsLegendDialog(
            entries = MediaSpecimen.entries,
            onClickConfirm = {},
            title = stringResource(R.string.menu_icons_legend)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MediaTypeIconsLegendPreview() {
    TePapaCollectionsTheme {
        IconsLegendDialog(
            entries = MediaType.entries,
            onClickConfirm = {},
            title = stringResource(R.string.menu_icons_legend)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectMediaPreview() {
    TePapaCollectionsTheme {
        SelectMediaDialog(
            currentSelection = MediaType.Organisation,
            entries = MediaType.entries,
            onClickConfirm = {},
            onClickDismiss = {}
        )
    }
}

/* This preview is intentionally out of order so that the
   bigger previews fit side-by-side in Android Studio */
@Preview(showBackground = true)
@Composable
fun QueryTypePreview() {
    TePapaCollectionsTheme {
        QueryTypeDialog(
            currentSelection = 1,
            onClickConfirm = {},
            onClickDismiss = {}
        )
    }
}