package com.michaelrmossman.collections.model

import kotlinx.serialization.Serializable

@Serializable
data class MetaDataSet(

    val resultset: ResultSet = ResultSet()
)