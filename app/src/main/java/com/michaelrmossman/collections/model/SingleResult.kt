package com.michaelrmossman.collections.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.jsonNull

@Serializable
data class SingleResult(

    @Contextual
    val result: SearchResult? = null

    /* facets and _metadata not used for href items */
)