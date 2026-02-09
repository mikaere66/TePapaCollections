package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class Identification(

    val type: String = String(),

    val title: String = String(),

    val dateIdentified: String = String(),

    val identifiedBy: IdentifiedBy = IdentifiedBy(),

    val qualifiedName: String = String(),

    val toTaxon: ToTaxon = ToTaxon(),

    val typeStatus: String = String()
)