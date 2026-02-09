package com.michaelrmossman.collections

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.michaelrmossman.collections.ui.CollectionsApp
import com.michaelrmossman.collections.ui.theme.TePapaCollectionsTheme

class MainActivity: ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Must come before enableEdgeToEdge() */
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            TePapaCollectionsTheme {
                val layoutDirection = LocalLayoutDirection.current
                Surface(
                    modifier = Modifier
                        .padding(
                            start = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateStartPadding(layoutDirection),
                            end = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateEndPadding(layoutDirection)
                        )
                ) {
                    val windowSize = calculateWindowSizeClass(
                        this@MainActivity
                    )
                    CollectionsApp(windowWidthSize = windowSize.widthSizeClass)
                }
            }
        }
    }
}