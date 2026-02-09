package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

/* Named as such so that it appears
   before all the Json data classes */
@Serializable
data class AMapMarker(

    val lat: Double,

    val lon: Double,

    val snippet: String,

    val title: String
)