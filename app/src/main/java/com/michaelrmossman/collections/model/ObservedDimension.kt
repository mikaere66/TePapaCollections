package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class ObservedDimension(

    val type: String = String(),

    val title: String = String(),

    val width: Double = 0.0,
    val height: Double = 0.0,

    val date: String = String(),

    val depth: Double = 0.0,
    val diameter: Double = 0.0,

    val extentType: String = String(),

    val length: Double = 0.0,
    val sizeUnitText: String = String(),

    val weight: Double = 0.0,
    val weightUnitText: String = String()
)