package com.michaelrmossman.collections.model

import com.michaelrmossman.collections.enum.Media
import com.michaelrmossman.collections.enum.MediaType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArrayBuilder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Suppress("PropertyName")
sealed interface SearchResult {

    val id: Int
    /* "type" is used as polymorphic "class discriminator" */
    val title: String
    val pid: String
    val iri: String
    val href: String
    val rightsHolder: String
    val accessRights: String
    val _meta: MetaDataResult
    val _api: ApiScore
    /* Added, in place of type */
    val media: Media

    @Serializable
    @SerialName("Category")
    data class Category(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Category,

        val exactMatch: String = String(),
        val inScheme: String = String(),
        val prefLabel: String = String(),
        val relatedTerms: List<RelatedTerm> = emptyList(),
        val creditLine: String = String(),
        val broaderTerms: List<BroaderTerm> = emptyList(),
        val alternativeTerms: List<String> = emptyList(),
        val narrowerTerms: List<SearchResult> = emptyList(),
        val scopeNote: String = String()
    ) : SearchResult

    @Serializable
    @SerialName("Collaboration")
    data class Collaboration(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Collaboration,
    ) : SearchResult

    @Serializable
    @SerialName("Group")
    data class Group(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Group,
    ) : SearchResult

    @Serializable
    @SerialName("ImageObject")
    open class ImageObject(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.ImageObject,

        val additionalType: List<String> = emptyList(),
        val identifier: String = String(),
        val contentUrl: String = String(),
        val previewUrl: String = String(),
        val thumbnailUrl: String = String(),
        val iiifUrl: String = String(),
        val width: Double = 0.0,
        val height: Double = 0.0,
        val contentSize: Double = 0.0,
        val facetMediaType: List<String> = emptyList(),
        val facetPermissionType: List<String> = emptyList(),
        val fileFormat: String = String(),
        val rights: Rights = Rights()
    ) : SearchResult

    @Serializable
    @SerialName("Object")
    data class Object(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Object,

        val additionalType: List<String> = emptyList(),
        val collection: String = String(),
        val collectionLabel: String = String(),
        val identifier: String = String(),
        val previousIdentifier: List<PreviousIdentifier> = emptyList(),
        val production: List<Production> = emptyList(),
        val productionUsedTechnique: List<ProductionUsedTechnique> = emptyList(),
        val isTypeOf: List<IsTypeOf> = emptyList(),
        val isMadeOfSummary: String = String(),
        val isMadeOf: List<IsMadeOf> = emptyList(),
        val depicts: List<SearchResult> = emptyList(),
        val description: String = String(),
        val isAbout: List<SearchResult> = emptyList(), // New
        val refersTo: List<SearchResult> = emptyList(),
        val intendedFor: List<SearchResult> = emptyList(),
        val formerOwner: List<SearchResult> = emptyList(),
        val format: List<String> = emptyList(),
        val influencedBy: List<SearchResult> = emptyList(), // New 20251030
        val observedDimension: List<ObservedDimension> = emptyList(),
        val creditLine: String = String(),
        val acknowledgement: String = String(),
        val isPartOf: IsPartOf = IsPartOf(),
        val caption: String = String(),
        val captionFormatted: String = String(),
        val related: List<Related> = emptyList(),
        val relation: List<Relation> = emptyList(),
        val isReferencedBy: List<SearchResult> = emptyList(), // was IsReferencedBy
        val comprisesOf: List<SearchResult> = emptyList(), // New
        val hasRepresentation: List<SearchResult> = emptyList(), // Was List<HasRepresentation>
        val hasPart: List<SearchResult> = emptyList() // New 20251030
    ) : SearchResult

    @Serializable
    @SerialName("Organisation")
    data class Organisation(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Organisation,

        val verbatimBirthDate: String = String(),
        val birthDate: String = String(),
        val facetBirthDate: FacetBirthDate = FacetBirthDate(),
        val verbatimDeathDate: String = String(),
        val deathDate: String = String(),
        val facetDeathDate: FacetDeathDate = FacetDeathDate(),
        val briefName: String = String(),
        val displayName: String = String(),
        val related: List<Related> = emptyList(),
        val isReferencedBy: List<SearchResult> = emptyList(), // was IsReferencedBy
        val identifiers: List<Identifier> = emptyList(),
        val associatedParties: List<SearchResult> = emptyList()
    ) : SearchResult

    @Serializable
    @SerialName("Person")
    open class Person(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Person,

        val birthPlace: String = String(),
        val verbatimBirthDate: String = String(),
        val birthDate: String = String(),
        val facetBirthDate: FacetBirthDate = FacetBirthDate(),
        val deathPlace: String = String(),
        val verbatimDeathDate: String = String(),
        val deathDate: String = String(),
        val facetDeathDate: FacetDeathDate = FacetDeathDate(),
        val briefName: String = String(),
        val ethnicity: List<String> = emptyList(),
        val nationality: List<String> = emptyList(),
        val familyName: String = String(),
        val givenName: String = String(),
        val displayName: String = String(),
        val gender: String = String(),
        val related: List<Related> = emptyList(),
        val isReferencedBy: List<SearchResult> = emptyList(), // was IsReferencedBy
        val identifiers: List<Identifier> = emptyList(),
        val associatedParties: List<SearchResult> = emptyList()
    ) : SearchResult

    @Serializable
    @SerialName("Place")
    data class Place(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Place,

        val exactMatch: String = String(),
        val inScheme: String = String(),
        val nation: List<String> = emptyList(),
        val prefLabel: String = String(), // was Array
        val placeType: String = String(),
        val narrowerTerms: List<SearchResult> = emptyList(),
        val creditLine: String = String(),
        val geoLocation: GeoLocation = GeoLocation(),
        val broaderTerms: List<BroaderTerm> = emptyList(),
        val alternativeTerms: List<String> = emptyList(),
        val scopeNote: String = String()
    ) : SearchResult

    /* Difference is type = "Position" vs type = "Person" */
    @Serializable
    @SerialName("Position")
    data class Position(
        @Transient
        override val media: Media = MediaType.Person
    ) : Person()

    @Serializable
    @SerialName("Publication")
    data class Publication(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Publication,

        val collection: List<String> = emptyList(),
        val collectionLabel: List<String> = emptyList(),
        val purpose: List<String> = emptyList(),
        val narrativeType: List<String> = emptyList(),
        val publicationType: List<String> = emptyList(),
        val publicationDate: List<String> = emptyList(),
        val authors: List<Author> = emptyList(),
        val publisher: List<Publisher> = emptyList(),
        val narrativeSummary: String = String(),
        val narrative: String = String(),
        val isAbout: List<SearchResult> = emptyList(), // New
        val refersTo: List<SearchResult> = emptyList(),
        val hasRepresentation: List<SearchResult> = emptyList(),
        val isPartOf: IsPartOf = IsPartOf(),
        val relatedTopics: List<SearchResult> = emptyList(), // was List<RelatedTopic>
        val relatedObjects: List<SearchResult> = emptyList() // was List<RelatedObject>
    ) : SearchResult

    @Serializable
    @SerialName("Specimen")
    data class Specimen(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Specimen,

        val additionalType: List<String> = emptyList(),
        val collection: String = String(),
        val collectionLabel: String = String(),
        val identifier: String = String(),
        val description: String = String(), // HTML?
        val previousIdentifier: List<PreviousIdentifier> = emptyList(),
        val basisOfRecord: String = String(),
        val evidenceFor: EvidenceFor = EvidenceFor(),
        val identification: List<Identification> = emptyList(),
        val specimenType: String = String(),
        @SerialName("lifeStage") val _lifeStage: JsonElement? = null,
        @SerialName("sex") val _sex: JsonElement? = null, // TODO: needs testing
        val organismQuantity: String = String(), // was Int
        val observedDimension: List<ObservedDimension> = emptyList(),
        val creditLine: String = String(),
        val caption: String = String(),
        val captionFormatted: String = String(),
        val inCollection: String = String(),
        val institutionCode: String = String(),
        val relation: List<SearchResult> = emptyList(), // New
        val relationType: String = String(),
        val isReferencedBy: List<SearchResult> = emptyList(), // was IsReferencedBy
        val collectionCode: String = String(),
        val hasRepresentation: List<SearchResult> = emptyList(), // Was List<HasRepresentation>
        val hasPart: List<SearchResult> = emptyList() // New
    ) : SearchResult {
        val sex: List<String>? get() = when (_sex) {
            is JsonNull -> emptyList()
            is JsonObject -> Json.decodeFromJsonElement(_sex)
            else -> _sex?.let {
                Json.decodeFromJsonElement<List<String>>(it)
            }
        }
        val lifeStage: List<String>? get() = when (_lifeStage) {
            is JsonNull -> emptyList()
            is JsonObject -> Json.decodeFromJsonElement(_lifeStage)
            else -> _lifeStage?.let {
                Json.decodeFromJsonElement<List<String>>(it)
            }
        }
    }

    @Serializable
    @SerialName("Taxon")
    data class Taxon(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Taxon,

        val prefLabel: String = String(),
        val basisOfRecord: String = String(),
        val taxonRank: String = String(),
        val scientificName: String = String(),
        val scientificNameAuthorship: List<SearchResult> = emptyList(), // New
        val scientificNameAuthorshipSummary: String = String(),
        val higherClassification: String = String(),
        val kingdom: String = String(),
        val `class`: String = String(),
        val order: String = String(),
        val family: String = String(),
        val genus: String = String(),
        val subgenus: String = String(),
        val species: String = String(),
        val subspecies: String = String(),
        val narrowerRanks: List<SearchResult> = emptyList(),
        val commonName: List<String> = emptyList(),
        val nomenclaturalCode: String = String(),
        val phylum: String = String(),
        val vernacularName: List<VernacularName> = emptyList(),
        val vernacularNameGroups: List<VernacularNameGroup> = emptyList(),
        val broaderRank: BroaderRank = BroaderRank()
    ) : SearchResult

    /* Labels identical to ImageObject */
    @Serializable
    @SerialName("TextDigitalDocument")
    data class TextDigitalDocument(
        @Transient
        override val media: Media = MediaType.TextDigitalDocument
    ) : ImageObject()

    @Serializable
    @SerialName("Topic")
    data class Topic(
        override val id: Int = 0,
        override val title: String = String(),
        override val pid: String = String(),
        override val iri: String = String(),
        override val href: String = String(),
        override val rightsHolder: String = String(),
        override val accessRights: String = String(),
        override val _meta: MetaDataResult = MetaDataResult(),
        override val _api: ApiScore = ApiScore(),
        @Transient
        override val media: Media = MediaType.Topic,

        val collection: List<String> = emptyList(),
        val collectionLabel: List<String> = emptyList(),
        val purpose: List<String> = emptyList(),
        val narrativeType: List<String> = emptyList(),
        val publicationDate: List<String> = emptyList(),
        val authors: List<Author> = emptyList(),
        val narrativeSummary: String = String(),
        val narrative: String = String(),
        val isAbout: List<SearchResult> = emptyList(), // New
        val refersTo: List<SearchResult> = emptyList(),
        val associatedWith: List<SearchResult> = emptyList(),
        val hasRepresentation: List<SearchResult> = emptyList(), // New 20251030
        val hasPart: List<SearchResult> = emptyList(), // New 20251030
        val isPartOf: IsPartOf = IsPartOf(),
        val related: List<Related> = emptyList(),
        val relatedTopics: List<SearchResult> = emptyList(), // was List<RelatedTopic>
        val relatedObjects: List<SearchResult> = emptyList() // was List<RelatedObject>
    ) : SearchResult
}