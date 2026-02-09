package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class Rights(

    val type: String = String(),

    val title: String = String(),

    val description: String = String(),

    val allowsDownload: Boolean = false,

    val iri: String = String()
)