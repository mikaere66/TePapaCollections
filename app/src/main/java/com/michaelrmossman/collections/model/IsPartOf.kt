package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class IsPartOf(

    val id: Int = 0,

    val type: String = String(),

    val collection: String = String(),

    val title: String = String(),

    val purpose: List<String> = emptyList(),

    val publicationDate: List<String> = emptyList(),

    val pid: String = String(),

    val iri: String = String(),

    val href: String = String()
)