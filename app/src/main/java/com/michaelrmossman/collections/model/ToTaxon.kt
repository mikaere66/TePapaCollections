package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class ToTaxon(

    val id: Int = 0,

    val type: String = String(),

    val title: String = String(),

    val scientificName: String = String(),

    val higherClassification: String = String(),

    val family: String = String(),

    val genus: String = String(),

    val species: String = String(),

    val rank: String = String(),

    val vernacularName: List<VernacularName> = emptyList(),

    val pid: String = String(),

    val iri: String = String(),

    val href: String = String()
)