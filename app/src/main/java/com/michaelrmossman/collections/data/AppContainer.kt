package com.michaelrmossman.collections.data

import android.content.Context
import com.michaelrmossman.collections.database.SettingsDatabase
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.network.CollectionsApiService
import com.michaelrmossman.collections.util.COLLECTIONS_API_URL
import com.michaelrmossman.collections.util.COLLECTIONS_DATA_TYPE
import com.michaelrmossman.collections.util.DEBUG_JSON_ADDITIONAL_MESSAGES
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {

    val database: SettingsDatabase

    val historyRepository: HistoryRepository

    val favesRepository: FavouritesRepository

    val networkRepository: NetworkRepository

    val settingsRepository: SettingsRepository
}

class DefaultAppContainer(
    private val apiKey: String, context: Context
) : AppContainer {

    val appModule = SerializersModule {
        polymorphic(SearchResult::class) {
            subclass(SearchResult.Category::class)
            subclass(SearchResult.Collaboration::class)
            subclass(SearchResult.Group::class)
            subclass(SearchResult.ImageObject::class)
            subclass(SearchResult.Object::class)
            subclass(SearchResult.Organisation::class)
            subclass(SearchResult.Person::class)
            subclass(SearchResult.Place::class)
            subclass(SearchResult.Position::class)
            subclass(SearchResult.Publication::class)
            subclass(SearchResult.Specimen::class)
            subclass(SearchResult.Taxon::class)
            subclass(SearchResult.TextDigitalDocument::class)
            subclass(SearchResult.Topic::class)
        }
    }
    val json = Json {
        serializersModule = appModule
        ignoreUnknownKeys = true // TODO
    }
    private val interceptor = HttpLoggingInterceptor().apply {
        if (DEBUG_JSON_ADDITIONAL_MESSAGES) {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
    /* The above solution shows logcat messages similar to the previous
       Retrofit method, using setLogLevel(RestAdapter.LogLevel.FULL) */
    private val client =
       OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(
            json.asConverterFactory(
                COLLECTIONS_DATA_TYPE.toMediaType()
            )
        )
        .baseUrl(COLLECTIONS_API_URL)
        .client(client)
        .build()
    private val retrofitService: CollectionsApiService by lazy {
        retrofit.create(CollectionsApiService::class.java)
    }

    override val database = SettingsDatabase.getDatabase(context)

    override val historyRepository: HistoryRepository by lazy {
        HistoryRepository(database.historyDao())
    }

    override val favesRepository: FavouritesRepository by lazy {
        FavouritesRepository(
            database.favouritesDao(), database.settingsDao()
        )
    }

    override val networkRepository: NetworkRepository by lazy {
        NetworkRepository(apiKey, retrofitService)
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(database.settingsDao())
    }
}