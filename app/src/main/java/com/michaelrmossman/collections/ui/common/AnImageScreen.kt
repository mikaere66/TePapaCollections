package com.michaelrmossman.collections.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.michaelrmossman.collections.CollectionsApplication
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.ui.SharedViewModel
import com.michaelrmossman.collections.ui.components.ImageInfoButton
import com.michaelrmossman.collections.ui.components.TwoLineAppBar

/* Single image. Disabled 20260707: imageObject.contentUrl
   returns error message "Missing Authentication Token" */
@Composable
fun AnImageScreen(
    imageObject: ImageObject,
    itemTitle: String,
    onClickBackButton: () -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val additionalPadding = dimensionResource(R.dimen.padding_mini)
    val apiKey = CollectionsApplication.Companion.apiKey
    val columnModifier = Modifier.fillMaxSize()
    val context = LocalContext.current
    val sharedViewModel: SharedViewModel = viewModel(
        factory = SharedViewModel.Factory
    )
    val horizontalPadding = dimensionResource(R.dimen.padding_mini)
    var isDownloading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val localWindowInfo = LocalWindowInfo.current
    val paddingImageHorizontal = dimensionResource(R.dimen.padding_small)
    val paddingTextHorizontal = dimensionResource(R.dimen.padding_medium)
    val paddingVertical = dimensionResource(R.dimen.padding_small)
    val roundedCornerShape = dimensionResource(R.dimen.card_corner_shape)
    val screenHeight = localWindowInfo.containerSize.height
    val screenWidth = localWindowInfo.containerSize.width
    val scrollState = rememberScrollState()
    var showInfoBS by remember { mutableStateOf(false) }
    val titleText = @Composable {
        Text(
            text = imageObject.title,
            modifier = Modifier.padding(
                horizontal = paddingTextHorizontal,
                vertical = paddingVertical
            )
        )
    }

    val zoomFullImage by sharedViewModel.zoomFullImage.observeAsState()
    if (showInfoBS) {
        zoomFullImage?.let { zoomImage ->
            AnImageInfoBottomSheet(
                imageObject = imageObject,
                onDismissRequest = { showInfoBS = false },
                zoomImageTitle = when (zoomImage == 1) {
                    true -> titleText
                    else -> null
                }
            )
        }
    }

    /* If this option is enabled by the user, image will be FULL size,
       based on screen bounds ... then image will be scrollable EITHER
       horizontally or vertically (as appropriate) to view its content */
    val zoomableColumnModifier = when (
        zoomFullImage != null
        &&
        zoomFullImage == 1
    ) {
        true -> {
            when (screenWidth > screenHeight) {
                true -> columnModifier.verticalScroll(scrollState)
                else -> columnModifier.horizontalScroll(scrollState)
            }
        }
        else -> columnModifier
    }
    val zoomableImageContentScale = when(
        zoomFullImage != null
        &&
        zoomFullImage == 1
    ) {
        true -> ContentScale.Inside
        /* Coil default is Fit */
        else -> ContentScale.Fit
    }
    var zoomableImageModifier by remember {
        mutableStateOf(
            Modifier
                .clip(
                    RoundedCornerShape(
                        size = roundedCornerShape
                    )
                )
                .fillMaxWidth()
                .padding(
                    horizontal = paddingImageHorizontal,
                    vertical = paddingVertical
                )
        )
    }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    ImageInfoButton(
                        onClickInfoButton = { showInfoBS = true }
                    )
                },
                onClickBackButton = onClickBackButton,
                stringId = R.string.app_name,
                subtitle = stringResource(
                    stringId,
                    itemTitle
                )
            )
        }
    ) { contentPadding ->

        Box (
            modifier = Modifier.padding(
                bottom = contentPadding.calculateBottomPadding().plus(
                    additionalPadding
                ),
                end = horizontalPadding,
                start = horizontalPadding,
                top = contentPadding.calculateTopPadding().plus(
                    additionalPadding
                )
            )
        ) {
            Column(
                modifier = zoomableColumnModifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = dimensionResource(
                            R.dimen.card_elevation
                        )
                    ),
                    modifier = modifier.fillMaxSize(),
                    shape = RoundedCornerShape(roundedCornerShape)
                ) {
                    if (
                        !isError
                        ||
                        isDownloading
                    ) {
                        AsyncImage(
                            contentDescription = imageObject.title,
                            contentScale = zoomableImageContentScale,
                            model = ImageRequest.Builder(
                                context = context
                            )
                            .data(imageObject.contentUrl)
                            // TODO: this is NOT working!
                            .addHeader("Authorization", "Bearer $apiKey")
                            .crossfade(true)
                            .listener(
                                onError = { _, _ ->
                                    isDownloading = false
                                    isError = true
                                },
                                onSuccess = { _, result ->
                                    zoomFullImage?.let { zoomImage ->
                                        if (zoomImage == 1) {
                                            val intrinsicWidth =
                                                result.drawable.intrinsicWidth
                                            val intrinsicHeight =
                                                result.drawable.intrinsicHeight
                                            // modifier/scaling based on dimens
                                            zoomableImageModifier = when (
                                                intrinsicWidth > intrinsicHeight
                                            ) {
                                                true -> Modifier.fillMaxWidth()
                                                else -> Modifier.fillMaxHeight()
                                            }
                                        }
                                    }
                                    isDownloading = false
                                    isError = false
                                }
                            )
                            .build(),
                            modifier = zoomableImageModifier
                        )
                        /* Only show title if NOT fullscreen.
                           In fullscreen, its' shown in BS */
                        zoomFullImage?.let { zoomImage ->
                            if (zoomImage == 0) {
                                titleText.invoke()
                            }
                        }
                    }
                }
            }

            if (isDownloading || isError) {
                AcquiringImage(
                    isDownloading = isDownloading,
                    isError = isError,
                    modifier = Modifier.fillMaxSize(),
                    onDownloadClick = {
                        isDownloading = true
                        isError = false
                    },
                    stringId = R.string.image_retry
                )
            }
        }
    }
}