package com.michaelrmossman.collections.network

import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResults
import com.michaelrmossman.collections.util.COLLECTIONS_API_KEY
import com.michaelrmossman.collections.util.COLLECTIONS_OBJECT_URL
import com.michaelrmossman.collections.util.COLLECTIONS_SEARCH_URL
import com.michaelrmossman.collections.util.COLLECTIONS_TYPE_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface CollectionsApiService {

    /* .../collection/object?q=[query]&size=[size]&from=[from] */
    @GET(COLLECTIONS_OBJECT_URL)
    fun getSearchResultsByObject(
        @Header(COLLECTIONS_API_KEY) apiKey: String,
        @Header(COLLECTIONS_TYPE_KEY) typeKey: String,
        @Query("q") query: String,
        @Query("size") size: Int,
        @Query("from") from: Int
    ) : Call<SearchResults>

    /* .../collection/search?q=[query]&size=[size]&from=[from] */
    @GET(COLLECTIONS_SEARCH_URL)
    fun getSearchResultsByType(
        @Header(COLLECTIONS_API_KEY) apiKey: String,
        /* Type key NOT related to COLLECTION type */
        @Header(COLLECTIONS_TYPE_KEY) typeKey: String,
        @Query("q") query: String,
        @Query("size") size: Int,
        @Query("from") from: Int
    ) : Call<SearchResults>

    @GET
    fun getSearchResultByUrl(
        @Header(COLLECTIONS_API_KEY) apiKey: String,
        @Header(COLLECTIONS_TYPE_KEY) typeKey: String,
        @Url href: String
    ) : Call<SearchResult>
}