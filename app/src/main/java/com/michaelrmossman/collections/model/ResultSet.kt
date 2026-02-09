package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class ResultSet(

    val count: Int = 0,

    val from: Int = 0,

    val size: Int = 0,

    val truncated: Boolean = false
)