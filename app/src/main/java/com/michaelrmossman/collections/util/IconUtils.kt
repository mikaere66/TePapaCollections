package com.michaelrmossman.collections.util

import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.MediaObject
import com.michaelrmossman.collections.enum.MediaSpecimen
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.SearchType

/**
 * Icon utility functions used throughout the app
 */
object IconUtils {

    /* Refer caller re collection.isBlank() */
    fun getMediaForIconBySearchTypeAndMedia(
        collection: String,
        media: Media,
        searchType: SearchType?
    ) : Media {
        return when (collection.isBlank()) {
            true -> media
            else -> when (searchType) {
                SearchType.MediaObject -> {
                    MediaObject.valueOf(collection)
                }
                SearchType.MediaSpecimen -> {
                    MediaSpecimen.valueOf(collection)
                }
                else -> when (media) {
                    MediaType.Object -> {
                        MediaObject.valueOf(collection)
                    }
                    MediaType.Specimen -> {
                        MediaSpecimen.valueOf(collection)
                    }
                    else -> media
                }
            }
        }
    }

    fun getMediaIconId(media: Media): Int {
        return when (media) {
            /* These three are duplicated by various [MediaType]s */
            Media.MediaObject -> R.drawable.outline_emoji_objects_24
            Media.MediaSpecimen -> R.drawable.icons_lib_turtle_24
            Media.MediaType -> R.drawable.outline_category_24

            MediaObject.Art -> R.drawable.icons_lib_art_24
            MediaObject.CollectedArchives -> R.drawable.outline_archive_24
            MediaObject.History -> R.drawable.outline_history_edu_24
            MediaObject.MuseumArchives -> R.drawable.outline_museum_24
            MediaObject.PacificCultures -> R.drawable.icons_lib_people_24
            MediaObject.Philatelic -> R.drawable.icons_lib_stamp_24
            MediaObject.Photography -> R.drawable.outline_photo_camera_24
            MediaObject.RareBooks -> R.drawable.outline_book_4_24
            MediaObject.TaongaMaori -> R.drawable.icons_lib_prized_24

            MediaSpecimen.Archaeozoology -> R.drawable.outline_humerus_24
            MediaSpecimen.Birds -> R.drawable.icons_lib_bird_24
            MediaSpecimen.Crustacea -> R.drawable.icons_lib_bug_24
            MediaSpecimen.Fish -> R.drawable.icons_lib_fish_24
            MediaSpecimen.FossilVertebrates -> R.drawable.outline_skeleton_24
            MediaSpecimen.Geology -> R.drawable.icons_lib_earth_24
            MediaSpecimen.Insects -> R.drawable.outline_bug_report_24
            MediaSpecimen.LandMammals -> R.drawable.outline_landscape_2_24
            MediaSpecimen.MarineInvertebrates -> R.drawable.outline_donut_small_24
            MediaSpecimen.MarineMammals -> R.drawable.outline_water_24
            MediaSpecimen.Molluscs -> R.drawable.icons_lib_mollusc_24
            MediaSpecimen.Plants -> R.drawable.icons_lib_plant_24
            MediaSpecimen.ReptilesAndAmphibians -> R.drawable.icons_lib_reptile_24

            MediaType.Category -> R.drawable.outline_category_24
            MediaType.Collaboration -> R.drawable.outline_workspaces_24
            MediaType.Group -> R.drawable.outline_group_work_24
            MediaType.ImageObject -> R.drawable.outline_image_24
            MediaType.Object -> R.drawable.outline_emoji_objects_24
            MediaType.Organisation -> R.drawable.icons_lib_organisation_24
            MediaType.Person -> R.drawable.outline_person_24
            MediaType.Place -> R.drawable.outline_place_24
            MediaType.Position -> R.drawable.outline_person_shield_24
            MediaType.Publication -> R.drawable.icons_lib_publication_24
            MediaType.Specimen -> R.drawable.icons_lib_turtle_24
            MediaType.Taxon -> R.drawable.icons_lib_micro_organism_24
            MediaType.TextDigitalDocument -> R.drawable.outline_text_snippet_24
            MediaType.Topic -> R.drawable.outline_topic_24
        }
    }
}