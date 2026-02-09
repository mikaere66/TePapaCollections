package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class BroaderTerm(

    val id: Int = 0,

    val type: String = String(),

    val title: String = String(),

    val prefLabel: String = String(),

    val creditLine: String = String(),

    val exactMatch: String = String(),

    val scopeNote: String = String(),

    val pid: String = String(),

    val iri: String = String(),

    val href: String = String()
)