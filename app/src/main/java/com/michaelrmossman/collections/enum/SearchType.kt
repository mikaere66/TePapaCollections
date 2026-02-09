package com.michaelrmossman.collections.enum

import kotlinx.serialization.Serializable

@Serializable
enum class SearchType {
    MediaObject,
    MediaSpecimen,
    MediaType
}