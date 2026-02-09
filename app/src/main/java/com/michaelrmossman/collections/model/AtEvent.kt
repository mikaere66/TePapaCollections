package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class AtEvent(

    val id: Int = 0,

    val type: String = String(),

    val title: String = String(),

    val atLocation: AtLocation = AtLocation(),

    val eventDate: String = String(),

    val fieldNumber: String = String(),

    val recordedBy: List<RecordedBy> = emptyList(),

    val verbatimEventDate: String = String(),

    val pid: String = String(),

    val iri: String = String(),

    val href: String = String()

    
)