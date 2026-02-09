package com.michaelrmossman.collections

import android.app.Application
import com.michaelrmossman.collections.data.AppContainer
import com.michaelrmossman.collections.data.DefaultAppContainer
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.SupervisorJob

class CollectionsApplication: Application() {

    /* Continue coroutines after viewModelScope lifecycle */
//    val applicationScope = CoroutineScope(SupervisorJob())

    /* AppContainer instance, used to obtain dependencies */
    lateinit var container: AppContainer

    /* Te Papa Collections API key : "secrets.properties" */
    private val apiKey = BuildConfig.MoNZ_API_KEY

    companion object {

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