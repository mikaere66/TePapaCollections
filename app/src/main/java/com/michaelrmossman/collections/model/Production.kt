package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class Production(

    val type: String = String(),

    val title: String = String(),

    val contributor: Contributor = Contributor(),

    val createdDate: String = String(),

    val facetCreatedDate: FacetCreatedDate = FacetCreatedDate(),

    val role: String = String(),

    val spatial: Spatial = Spatial(),

    val verbatimCreatedDate: String = String()
)