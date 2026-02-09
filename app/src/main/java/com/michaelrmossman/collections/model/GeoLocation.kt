package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoLocation(

    val lat: Double = 0.0,

    val lon: Double = 0.0
)