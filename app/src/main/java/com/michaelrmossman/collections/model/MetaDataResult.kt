package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class MetaDataResult(

    val created: String = String(),

    val modified: String = String(),

    val qualityScore: Double = 0.0
)