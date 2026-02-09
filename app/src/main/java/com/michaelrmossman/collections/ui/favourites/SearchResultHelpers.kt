package com.michaelrmossman.collections.ui.favourites

import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.model.SearchResult.Category

object SearchResultHelpers {

    fun getCategory(
        id: Int,
        title: String,
        href: String,
        media: Media,
        // TODO
    ) : Category = Category(
        id = id,
        title = title,
        href = href,
        media = media
    )
}