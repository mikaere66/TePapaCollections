package com.michaelrmossman.collections.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ManageSearch
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.util.DialogUtils.QueryTypeDialog
import com.michaelrmossman.collections.util.DialogUtils.SelectMediaDialog

/* Is leading icon for SearchBoxWithButton */
@Composable
fun SearchBoxActionMenu(
    currentMedia: Media?,
    currentQueryType: Int,
    entries: Iterable<Media>,
    onClickMediaItem: (Int) -> Unit,
    onClickQueryItem: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var showMediaDialog by remember { mutableStateOf(false) }
    var showQueryDialog by remember { mutableStateOf(false) }

    val onClickManageSearch: (Boolean) -> Unit = { isMedia ->
        when (isMedia) {
            true -> showMediaDialog = true
            else -> showQueryDialog = true
        }
    }

    if (showMediaDialog) {
        SelectMediaDialog(
            currentSelection = currentMedia,
            entries = entries,
            onClickConfirm = { index ->
                showMediaDialog = false
                onClickMediaItem(index)
            },
            onClickDismiss = { showMediaDialog = false }
        )
    }

    if (showQueryDialog) {
        QueryTypeDialog(
            currentSelection = currentQueryType,
            onClickConfirm = { index ->
                showQueryDialog = false
                onClickQueryItem(index)
            },
            onClickDismiss = { showQueryDialog = false }
        )
    }

    IconButton(
        onClick = { expanded = true },
        /* Modifier same as trailing icon */
        modifier = modifier
    ) {
        Icon(
            contentDescription = stringResource(
                R.string.search_desc
            ),
            imageVector = Icons.AutoMirrored.Outlined.ManageSearch
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(stringResource(R.string.search_media))
            },
            onClick = {
                expanded = false
                onClickManageSearch(true)
            }
        )
        DropdownMenuItem(
            text = {
                Text(stringResource(R.string.search_options))
            },
            onClick = {
                expanded = false
                onClickManageSearch(false)
            }
        )
    }
}