package com.michaelrmossman.collections.ui.content

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.ui.components.MessageWithIcon
import com.michaelrmossman.collections.ui.components.SingleLineAppBar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ContentPlaceholder(
    @DrawableRes drawableId: Int,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val additionalPadding = dimensionResource(R.dimen.padding_content_card)
    val cardCornerShape = dimensionResource(R.dimen.card_corner_shape)
    val cardElevation = dimensionResource(R.dimen.card_elevation)
    val imageSize = dimensionResource(R.dimen.placeholder_image_size)
    val spacerSize = dimensionResource(R.dimen.padding_large)

    Scaffold(
        topBar = {
            SingleLineAppBar(
                stringId = 0
            )
        },
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) { contentPadding ->

        Card(
            modifier = modifier.padding(
                top = contentPadding.calculateTopPadding().plus(
                    additionalPadding
                ),
                end = contentPadding.calculateEndPadding(
                    LayoutDirection.Ltr
                ).plus(additionalPadding),
                bottom = contentPadding.calculateBottomPadding().plus(
                    additionalPadding
                )
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = cardElevation
            ),
            shape = RoundedCornerShape(size = cardCornerShape)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(drawableId),
                    modifier = Modifier.size(imageSize),
                    contentDescription = stringResource(
                        R.string.background_description
                    )
                )
                Spacer(modifier = Modifier.size(spacerSize))
                Text(text = stringResource(stringId))
                MessageWithIcon(
                    drawableId = R.drawable.outline_manage_search_24,
                    stringId = R.string.details_manage_search_message
                )
            }
        }
    }
}