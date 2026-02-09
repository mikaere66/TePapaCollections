package com.michaelrmossman.collections.network

import com.michaelrmossman.collections.model.SearchResults

data class SearchResponse(

    val searchResults: SearchResults? = null,

    val responseCode : Int = 0
)