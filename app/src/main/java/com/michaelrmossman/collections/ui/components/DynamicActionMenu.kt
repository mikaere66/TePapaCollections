package com.michaelrmossman.collections.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R

@Composable
fun DynamicActionMenu(
    isEnabled: List<Boolean>,
    menuLabels: List<String>,
    onClickActions: List<() -> Unit>
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            Icons.Filled.MoreVert,
            contentDescription = stringResource(
                R.string.menu_toggle_desc
            )
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        if (
            isEnabled.size == menuLabels.size
            &&
            menuLabels.size == onClickActions.size
        ) {
            onClickActions.forEachIndexed { index, onClickAction ->
                DropdownMenuItem(
                    enabled = isEnabled[index],
                    onClick = {
                        expanded = false
                        onClickAction()
                    },
                    text = {
                        Text(
                            menuLabels[index]
                        )
                    }
                )
            }
        }
    }
}