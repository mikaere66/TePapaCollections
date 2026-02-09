package com.michaelrmossman.collections.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Suppress("PropertyName")
@Serializable
data class SearchResults(

    @Contextual
    val results: List<SearchResult> = emptyList(),

    /* Every single query has this empty "facets":{}
       element toward the end of the JSON string */
    val facets: JsonElement,

    val _metadata: MetaDataSet
)