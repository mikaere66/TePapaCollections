package com.michaelrmossman.collections.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.Related
import com.michaelrmossman.collections.util.RELATED_WEB_PAGE_TYPE
import com.michaelrmossman.collections.util.TextUtils.getWebAnnotatedString
import com.michaelrmossman.collections.util.openUrlInBrowser

/* Object | Organisation | Person | Topic */
@Composable
fun RelatedWebPage(
    related: List<Related>,
    modifier: Modifier = Modifier
) {
    related.forEach { rel ->

        if (rel.type == RELATED_WEB_PAGE_TYPE) {

            if (rel.contentUrl.isNotBlank()) {

                val context = LocalContext.current
                val linkText = getWebAnnotatedString(
                    contentUrl = rel.contentUrl,
                    linkText = when (rel.title.isBlank()) {
                        true -> stringResource(
                            R.string.related_web_page_without_title
                        )
                        else -> stringResource(
                            R.string.related_web_page_with_title,
                            rel.title
                        )
                    }
                )
                val openLink: () -> Unit = {
                    context.openUrlInBrowser(rel.contentUrl)
                }

                Text(
                    text = linkText,
                    modifier = modifier.clickable { openLink() }
                )
            }
        }
    }
}