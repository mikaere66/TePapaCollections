package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class Related(

    val type: String = String(),

    val title: String = String(),

    val contentUrl: String = String(),

    val iri: String = String()
)