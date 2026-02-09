package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class FacetBirthDate(

    val century: String = String(),

    val dayOfWeek: String = String(),

    val decadeOfCentury: String = String(),

    val era: String = String(),

    val monthOfYear: String = String(),

    val temporal: String = String(),

    val verbatim: String = String(),

    val year: String = String()
)