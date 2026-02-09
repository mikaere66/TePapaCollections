package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class Contributor(

    val id: Int = 0,

    val type: String = String(),

    val title: String = String(),

    val verbatimBirthDate: String = String(),
    val birthDate: String = String(),

    val verbatimDeathDate: String = String(),
    val deathDate: String = String(),

    val nationality: List<String> = emptyList(),

    val pid: String = String(),

    val iri: String = String(),

    val href: String = String()
)