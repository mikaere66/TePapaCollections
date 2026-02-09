package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class Spatial(

    val id: Int = 0,

    val type: String = String(),

    val title: String = String(),

    val prefLabel: String = String(),

    val exactMatch: String = String(),

    val nation: List<String> = emptyList(),

    val scopeNote: String = String(),

    val pid: String = String(),

    val iri: String = String(),

    val href: String = String()
)