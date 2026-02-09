package com.michaelrmossman.collections.enum

enum class MediaObject: Media { // 9

    Art,
    CollectedArchives,
    History,
    MuseumArchives,
    PacificCultures,
    Philatelic,
    Photography,
    RareBooks,
    TaongaMaori {
        /* Required for actual search queries */
        override fun toString() = "TaongaMāori"
    }
}