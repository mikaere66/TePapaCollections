package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class VernacularName(

    val type: String = String(),

    val title: String = String(),

    val language: String = String(),

    val spatial: String = String()
)