package com.michaelrmossman.collections

import android.app.Application
import com.michaelrmossman.collections.data.AppContainer
import com.michaelrmossman.collections.data.DefaultAppContainer

class CollectionsApplication: Application() {

    /* AppContainer instance, used to obtain dependencies */
    lateinit var container: AppContainer

    companion object {
        /* Te Papa API key: from file secrets.properties */
        @Suppress("ConstPropertyName")
        const val apiKey = BuildConfig.MoNZ_API_KEY

        lateinit var instance: CollectionsApplication
    }

    override fun onCreate() {
        super.onCreate()

        container = DefaultAppContainer(
            apiKey, applicationContext
        )

        instance = this
    }
}