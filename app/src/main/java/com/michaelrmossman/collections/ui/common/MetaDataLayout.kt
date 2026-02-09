package com.michaelrmossman.collections.ui.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.ui.theme.TePapaCollectionsTheme
import com.michaelrmossman.collections.util.fromHtml

@Composable
fun MetaDataLayout(
    created: String,
    modified: String,
    modifier: Modifier = Modifier
) {
    val createdLabel = stringResource(
        R.string.meta_created
    ).fromHtml()
    val horizontalPadding = dimensionResource(R.dimen.padding_mini)
    val verticalPadding = dimensionResource(R.dimen.padding_small)
    val modifiedLabel = stringResource(
        R.string.meta_modified
    ).fromHtml()

    /* Text in both "rows" is constrained to end of BOTH labels */
    ConstraintLayout {
        val (label1, text1, label2, text2) = createRefs()
        val barrier = createEndBarrier(label1, label2)

        /* Row 1 */
        Text(
            text = createdLabel,
            modifier = modifier.constrainAs(label1) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }
        )
        Text(
            text = created,
            modifier = modifier.constrainAs(text1) {
                start.linkTo(
                    barrier,
                    margin = horizontalPadding
                )
                top.linkTo(parent.top)
            }
        )

        /* Row 2 */
        Text(
            text = modifiedLabel,
            modifier = modifier.constrainAs(label2) {
                start.linkTo(parent.start)
                top.linkTo(
                    label1.bottom,
                    margin = verticalPadding
                )
            }
        )
        Text(
            text = modified,
            modifier = modifier.constrainAs(text2) {
                start.linkTo(
                    barrier,
                    margin = horizontalPadding
                )
                top.linkTo(
                    text1.bottom,
                    margin = verticalPadding
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MetaDataPreview() {
    TePapaCollectionsTheme {
        MetaDataLayout(
            created  = "2005-05-31T05:37:05Z",
            modified = "2025-09-21T22:53:56Z"
        )
    }
}