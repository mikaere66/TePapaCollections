package com.michaelrmossman.collections.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.model.SearchResult

/* Used by both ListItemCard() and DetailsCard() */
@Composable
fun ListItemAll(
    /* Re to [Topic]'s narrative summary */
    fullText: Boolean,
    result: SearchResult,
    searchType: SearchType,
    /* Used by all [SearchResult]s */
    modifier: Modifier = Modifier
) {
    when (result) {
        is SearchResult.Category -> {
            ListItemCategory(
                category = result,
                modifier = modifier
            )
        }
        is SearchResult.Collaboration -> {
            ListItemCollaboration(
                collaboration = result,
                modifier = modifier
            )
        }
        is SearchResult.Group -> {
            ListItemGroup(
                group = result,
                modifier = modifier
            )
        }
        is SearchResult.ImageObject -> {
            ListItemImageObject(
                imageObject = result,
                modifier = modifier
            )
        }
        is SearchResult.Object -> {
            ListItemObject(
                `object` = result,
                searchType = searchType,
                modifier = modifier
            )
        }
        is SearchResult.Organisation -> {
            ListItemOrganisation(
                organisation = result,
                modifier = modifier
            )
        }
        is SearchResult.Person, is SearchResult.Position -> {
            ListItemPerson(
                person = result,
                modifier = modifier
            )
        }
        is SearchResult.Place -> {
            ListItemPlace(
                place = result,
                modifier = modifier
            )
        }
        is SearchResult.Publication -> {
            ListItemPublication(
                publication = result,
                modifier = modifier
            )
        }
        is SearchResult.Specimen -> {
            ListItemSpecimen(
                specimen = result,
                searchType = searchType,
                modifier = modifier
            )
        }
        is SearchResult.Taxon -> {
            ListItemTaxon(
                taxon = result,
                modifier = modifier
            )
        }
        is SearchResult.TextDigitalDocument -> {
            ListItemTextDigitalDocument(
                textDigitalDoc = result,
                modifier = modifier
            )
        }
        is SearchResult.Topic -> {
            ListItemTopic(
                fullText = fullText,
                topic = result,
                modifier = modifier
            )
        }
    }
}