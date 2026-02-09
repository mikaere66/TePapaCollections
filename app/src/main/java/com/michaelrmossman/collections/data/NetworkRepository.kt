package com.michaelrmossman.collections.data

import android.util.Log
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.SearchResults
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.network.CollectionsApiService
import com.michaelrmossman.collections.network.SearchResponse
import com.michaelrmossman.collections.network.SingleResponse
import com.michaelrmossman.collections.util.COLLECTIONS_DATA_TYPE
import com.michaelrmossman.collections.util.DEBUG_SHOW_ADDITIONAL_MESSAGES
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkRepository(
    private val apiKey: String,
    private val apiService: CollectionsApiService
) {

    /* Called from SharedViewModel */
    fun getSearchResults( // Multi
        callback: (SearchResponse) -> Unit,
        media: Media?,
        queryType: Int,
        searchQuery: String,
        searchType: SearchType,
        size: Int,
        startFrom: Int
    ) {
        val sb = StringBuilder()
        when (searchQuery.contains(" ")) {
            false -> { /* Note false */
                sb.append(searchQuery)
            }
            else -> {
                sb.append("(") /* Note opening, closing backets */
                when (queryType) {
                    2 -> {
                        sb.append("\"$searchQuery\"") // As phrase
                    }
                    1 -> {
                        val words = searchQuery.split(" ")
                        words.forEachIndexed { index, word ->
                            sb.append(
                                when (index) {        // All words
                                    words.lastIndex -> word
                                    else -> word.plus(" AND ")
                                }
                            )
                        }
                    }
                    else -> sb.append(searchQuery)     // Any word
                }
                sb.append(")")
            }
        }
        when (searchType) {
            SearchType.MediaSpecimen,
            SearchType.MediaObject -> {
                media?.let { collection ->
                    sb.append(" ")
                    sb.append("AND")
                    sb.append(" ")
                    sb.append("collection:${ collection }")
                }
                sb.append(" ")
                sb.append("AND")
                sb.append(" ")
                // https://data.tepapa.govt.nz/collection/object?q=jellyfish%20AND%20type%3AObject&size=10&from=0
                if (searchType == SearchType.MediaObject) {
                    sb.append("type:${ MediaType.Object }")
                }
                // https://data.tepapa.govt.nz/collection/search?q=jellyfish%20AND%20type%3ASpecimen&size=10&from=0
                if (searchType == SearchType.MediaSpecimen) {
                    sb.append("type:${ MediaType.Specimen }")
                }
            }
            SearchType.MediaType -> {
                media?.let { type ->
                    sb.append(" ")
                    sb.append("AND")
                    sb.append(" ")
                    sb.append("type:${ type }")
                }
            }
        }
        val call = when (searchType) {
            SearchType.MediaObject -> apiService.getSearchResultsByObject(
                apiKey = apiKey,
                typeKey = COLLECTIONS_DATA_TYPE,
                query = sb.toString(),
                size = size,
                from = startFrom
            )
            SearchType.MediaType,
            SearchType.MediaSpecimen -> apiService.getSearchResultsByType(
                apiKey = apiKey,
                typeKey = COLLECTIONS_DATA_TYPE,
                query = sb.toString(),
                size = size,
                from = startFrom
            )
        }
        if (DEBUG_SHOW_ADDITIONAL_MESSAGES) {
            Log.d(TAG, sb.toString())
        }
        call.enqueue(object: Callback<SearchResults> {
            override fun onResponse(
                call:     Call<SearchResults>,
                response: Response<SearchResults>
            ) {
                if (response.errorBody() != null) {
                    callback(
                        SearchResponse(
                            responseCode = 404
                        )
                    )

                } else if (response.isSuccessful) {
                    response.body()?.let { searchResponse ->
                        callback(
                            SearchResponse(
                                searchResults = searchResponse,
                                responseCode = response.code()
                            )
                        )
                    }

                } else {
                    callback(
                        SearchResponse(
                            responseCode = response.code()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<SearchResults>, throwable: Throwable
            ) {
                callback(
                    SearchResponse(
                        responseCode = -1
                    )
                )
                println(throwable)
            }
        })
    }

    /* Called from HrefsList | HrefSingle viewModels */
    fun getSingleResult( // Single
        callback: (SingleResponse) -> Unit,
        href: String
    ) {
        val call = apiService.getSearchResultByUrl(
            apiKey = apiKey,
            typeKey = COLLECTIONS_DATA_TYPE,
            href = href
        )
        call.enqueue(object: Callback<SearchResult> {
            override fun onResponse(
                call:     Call<SearchResult>,
                response: Response<SearchResult>
            ) {
                if (response.errorBody() != null) {
                    callback(
                        SingleResponse(
                            responseCode = 404
                        )
                    )

                } else if (response.isSuccessful) {
                    response.body()?.let { searchResponse ->
                        callback(
                            SingleResponse(
                                searchResult = searchResponse,
                                responseCode = response.code()
                            )
                        )
                    }

                } else {
                    callback(
                        SingleResponse(
                            responseCode = response.code()
                        )
                    )
                }
            }

            override fun onFailure(
                call: Call<SearchResult>, throwable: Throwable
            ) {
                callback(
                    SingleResponse(
                        responseCode = -1
                    )
                )
                println(throwable)
            }
        })
    }

    companion object { const val TAG = "NetworkRepository" }
}