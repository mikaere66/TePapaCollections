package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class EvidenceFor(

    val type: String = String(),

    val title: String = String(),

    val atEvent: AtEvent = AtEvent()
)