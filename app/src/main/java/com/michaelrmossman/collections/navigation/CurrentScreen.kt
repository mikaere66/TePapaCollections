package com.michaelrmossman.collections.navigation

import androidx.navigation3.runtime.NavKey
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.data.FaveEntity
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.AMapMarker
import com.michaelrmossman.collections.model.SearchResult
import com.michaelrmossman.collections.model.SearchResult.ImageObject
import com.michaelrmossman.collections.util.IconUtils.getMediaIconId
import kotlinx.serialization.Serializable

sealed interface CurrentScreen: NavKey {

    val drawableId: Int
    val introStringId: Int
    val titleStringId: Int

    // Home

    @Serializable
    data object MainScreen: CurrentScreen {
        override val drawableId: Int =
            R.mipmap.ic_launcher_round
        override val introStringId: Int =
            R.string.intro_main
        override val titleStringId: Int =
            R.string.app_name
    }

    // Common

    @Serializable
    data class HrefDetails(
        val hrefsList: List<SearchResult>,
        val hrefIndex: Int
    ) : CurrentScreen {
        override val drawableId: Int =
            R.drawable.outline_link_24
        override val introStringId: Int =
            R.string.intro_hrefs
        override val titleStringId: Int =
            R.string.app_name
    }

    @Serializable
    data class ResultDetails(
        val searchResult: SearchResult?,
        val searchType: SearchType
    ) : CurrentScreen {
        override val drawableId: Int =
            R.drawable.outline_category_search_24
        override val introStringId: Int =
            R.string.intro_explorer
        override val titleStringId: Int =
            R.string.app_name
    }

    // Types

    @Serializable
    data object ExplorerScreen: CurrentScreen {
        override val drawableId: Int =
            R.drawable.outline_category_search_24
        override val introStringId: Int =
            R.string.intro_explorer
        override val titleStringId: Int =
            R.string.nav_search
    }

    // Images

    @Serializable
    data class ImagesScreen( // Multi
        val imageObjects: List<ImageObject>,
        val itemTitle: String
    ) : CurrentScreen {
        override val drawableId: Int =
            R.drawable.outline_images_mode_24
        override val introStringId: Int =
            R.string.intro_images
        override val titleStringId: Int =
            R.string.nav_images
    }
    data class ImageScreen( // Single
        val imageObject: ImageObject,
        val itemTitle: String
    ) : CurrentScreen {
        override val drawableId: Int =
            R.drawable.outline_image_24
        override val introStringId: Int =
            R.string.intro_image
        override val titleStringId: Int =
            R.string.nav_image
    }

    // Objects

    @Serializable
    data object ObjectsScreen: CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Object)
        override val introStringId: Int =
            R.string.intro_object
        override val titleStringId: Int =
            R.string.nav_objects
    }
    @Serializable
    data class ObjectDetails(
        val searchResult: SearchResult
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Object)
        override val introStringId: Int =
            R.string.intro_object
        override val titleStringId: Int =
            R.string.app_name
    }

    // Specimens

    @Serializable
    data object SpecimensScreen: CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Specimen)
        override val introStringId: Int =
            R.string.intro_specimen
        override val titleStringId: Int =
            R.string.nav_specimens
    }
    @Serializable
    data class SpecimenDetails(
        val searchResult: SearchResult
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Specimen)
        override val introStringId: Int =
            R.string.intro_specimen
        override val titleStringId: Int =
            R.string.app_name
    }

    // Other result types

    @Serializable
    data class CategoryDetails(
        val searchResult: SearchResult
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Category)
        override val introStringId: Int =
            R.string.intro_category
        override val titleStringId: Int =
            R.string.nav_type_category
    }

    @Serializable
    data class ImageObjectDetails(
        val searchResult: SearchResult
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.ImageObject)
        override val introStringId: Int =
            R.string.intro_image_object
        override val titleStringId: Int =
            R.string.nav_type_image_object
    }

    @Serializable
    data class MapScreen(
        val mapMarker: AMapMarker
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Place)
        override val introStringId: Int =
            R.string.intro_place
        override val titleStringId: Int =
            R.string.common_map_title
    }

    @Serializable
    data class OrganisationDetails(
        val searchResult: SearchResult
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Organisation)
        override val introStringId: Int =
            R.string.intro_organisation
        override val titleStringId: Int =
            R.string.nav_type_organisation
    }

    @Serializable
    data class PersonDetails(
        val searchResult: SearchResult
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Person)
        override val introStringId: Int =
            R.string.intro_person
        override val titleStringId: Int =
            R.string.nav_type_person
    }

    @Serializable
    data class PlaceDetails(
        val searchResult: SearchResult
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Place)
        override val introStringId: Int =
            R.string.intro_place
        override val titleStringId: Int =
            R.string.nav_type_place
    }

    @Serializable
    data class PublicationDetails(
        val searchResult: SearchResult
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Publication)
        override val introStringId: Int =
            R.string.intro_publication
        override val titleStringId: Int =
            R.string.nav_type_publication
    }

    @Serializable
    data class TaxonDetails(
        val searchResult: SearchResult
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Taxon)
        override val introStringId: Int =
            R.string.intro_taxon
        override val titleStringId: Int =
            R.string.nav_type_taxon
    }

    @Serializable
    data class TopicDetails(
        val searchResult: SearchResult
    ) : CurrentScreen {
        override val drawableId: Int =
            getMediaIconId(MediaType.Topic)
        override val introStringId: Int =
            R.string.intro_topic
        override val titleStringId: Int =
            R.string.nav_type_topic
    }

    // Favourites

    @Serializable
    data object FavesScreen: CurrentScreen {
        override val drawableId: Int =
            R.drawable.outline_bookmark_stacks_24
        override val introStringId: Int =
            R.string.intro_faves
        override val titleStringId: Int =
            R.string.nav_faves
    }

    @Serializable
    data class HrefSingle(
        val faveIndex: Int,
        val favesList: List<FaveEntity>
    ) : CurrentScreen {
        override val drawableId: Int =
            R.drawable.outline_bookmarks_24
        override val introStringId: Int =
            R.string.intro_faves
        override val titleStringId: Int =
            R.string.app_name
    }

    // Settings

    @Serializable
    data object SettingsScreen: CurrentScreen {
        override val drawableId: Int =
            R.drawable.outline_settings_24
        override val introStringId: Int =
            R.string.intro_settings
        override val titleStringId: Int =
            R.string.nav_settings
    }

    // Help

    @Serializable
    data object HelpScreen: CurrentScreen {
        override val drawableId: Int =
            R.drawable.baseline_help_outline_24
        override val introStringId: Int =
            R.string.intro_help
        override val titleStringId: Int =
            R.string.nav_help
    }
}