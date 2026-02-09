package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class Publisher(

    val id: Int = 0,

    val type: String = String(),

    val title: String = String(),

    val verbatimBirthDate: String = String(),

    val birthDate: String = String(),

    val briefName: String = String(),

    val pid: String = String(),

    val iri: String = String(),

    val href: String = String()
)