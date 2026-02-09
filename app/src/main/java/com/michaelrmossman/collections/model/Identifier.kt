package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class Identifier(

    val type: String = String(),

    val identifier: String = String(),

    val title: String = String()
)