package com.michaelrmossman.collections.network

import com.michaelrmossman.collections.model.SearchResult

data class SingleResponse(

    val searchResult: SearchResult? = null,

    val responseCode : Int = 0
)