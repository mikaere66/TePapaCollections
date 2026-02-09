package com.michaelrmossman.collections.enum

/* Okay, so it's not an enum, but
   enums do implement this =) */
sealed interface Media {

    data object MediaObject: Media
    data object MediaSpecimen: Media
    data object MediaType: Media
}