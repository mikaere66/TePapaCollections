package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class PreviousIdentifier(

    val type: String = String(),

    val identifier: String = String(),

    val title: String = String(),

    val notes: String = String()
)