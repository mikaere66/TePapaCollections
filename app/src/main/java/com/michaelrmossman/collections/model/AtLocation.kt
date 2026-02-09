package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class AtLocation(

    val type: String = String(),

    val title: String = String(),

    val continent: String = String(),

    val country: String = String(),

    val district: String = String(),

    val locality: String = String(),

    val mappingCentroid: GeoLocation = GeoLocation(),

    val stateProvince: String = String(),

    val waterBody: String = String()
)