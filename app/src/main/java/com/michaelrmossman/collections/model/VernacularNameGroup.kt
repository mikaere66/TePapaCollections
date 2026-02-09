package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class VernacularNameGroup(

    val commonLanguage: String = String(),

    val commonName: String = String()
)