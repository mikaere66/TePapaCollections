package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class BroaderRank(

    val id: Int = 0,

    val type: String = String(),

    val title: String = String(),

    val prefLabel: String = String(),

    val taxonRank: String = String(),

    val scientificName: String = String(),

    val species: String = String(),

    val commonName: List<String> = emptyList(),

    val pid: String = String(),

    val iri: String = String(),

    val href: String = String()
)