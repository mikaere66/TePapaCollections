package com.michaelrmossman.collections.ui.common

import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.util.TextUtils.getTextFromString
import com.michaelrmossman.collections.util.TextUtils.getWebAnnotatedString

@Composable
fun MetaDataFooter(
    result: SearchResult,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dividerPadding = dimensionResource(R.dimen.padding_small)
    val metaData = result._meta
    val metaAlpha = 0.6F

    val rightsHolderText = getTextFromString(
        stringId = R.string.rights_holder,
        string = result.rightsHolder.replace(
            "Te Papa Tongarewa",
            "<BR>Te Papa Tongarewa"
        )
    )

    val rightsAccessText = getWebAnnotatedString(
        contentUrl = result.accessRights,
        linkText = stringResource(R.string.rights_access)
    )

    val created = when(metaData.created.isBlank()) {
        true -> stringResource(R.string.common_undefined)
        else -> metaData.created
    }

    val modified = when(metaData.modified.isBlank()) {
        true -> stringResource(R.string.common_undefined)
        else -> metaData.modified
    }

    HorizontalDivider(
        modifier = Modifier.padding(
            vertical = dividerPadding
        )
    )
    Text(
        text = rightsHolderText,
        modifier = modifier
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            /* Note no modifier */
            text = rightsAccessText
        )
        IconButton(onClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                result.accessRights.toUri()
            )
            context.startActivity(intent)
        }) {
            Icon(
                contentDescription = stringResource(
                    R.string.open_in_new_desc
                ),
                imageVector =
                    Icons.AutoMirrored.Outlined.OpenInNew
            )
        }
    }
    /* ConstraintLayout */
    MetaDataLayout(
        created  = created,
        modified = modified,
        modifier = Modifier.alpha(metaAlpha)
    )
}