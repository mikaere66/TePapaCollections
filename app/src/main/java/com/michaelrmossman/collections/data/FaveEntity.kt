package com.michaelrmossman.collections.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.michaelrmossman.collections.enum.MediaObject
import com.michaelrmossman.collections.enum.MediaType
import com.michaelrmossman.collections.enum.SearchType
import com.michaelrmossman.collections.database.COLUMN_NAME_FAVE_ADDED
import com.michaelrmossman.collections.database.COLUMN_NAME_FAVE_TITLE
import com.michaelrmossman.collections.database.COLUMN_NAME_MEDIA
import com.michaelrmossman.collections.database.TABLE_NAME_FAVOURITE
import java.util.concurrent.TimeUnit
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = TABLE_NAME_FAVOURITE) // ∞
data class FaveEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int,

    @ColumnInfo(name = COLUMN_NAME_FAVE_ADDED)
    val added: Long,

    @ColumnInfo
    val collection: String,

    @ColumnInfo
    val href: String,

    /* Only for use in HrefSingleViewModel */
    @ColumnInfo
    val isFave: Boolean,

    @ColumnInfo
    val itemId: Int,

    /* These three only for MediaType.Place */
    @ColumnInfo
    val latitude: Double,
    @ColumnInfo
    val locationTitle: String?,
    @ColumnInfo
    val longitude: Double,

    @ColumnInfo(name = COLUMN_NAME_MEDIA)
    val media: String,

    @ColumnInfo
    val searchType: String,

    @ColumnInfo
    val subtitle1: String,

    @ColumnInfo
    val subtitle2: String,

    @ColumnInfo(name = COLUMN_NAME_FAVE_TITLE)
    val title: String
) {

    companion object {
        fun getTestFavourites(): List<FaveEntity> {
            val testFaves = mutableListOf<FaveEntity>()
            val testCollections = listOf("Art",String())
            val testHrefs = listOf(
                "https://data.tepapa.govt.nz/collection/object/191570",
                "https://data.tepapa.govt.nz/collection/topic/2344"
            )
            val testItemIds = listOf(191570,2344)
            val testLatitudes = listOf(0.0,-42.0)
            val testLocationTitles = listOf(null,"Eketāhuna")
            val testLongitudes = listOf(0.0,174.0)
            val testMediaTypes = listOf(
                MediaObject.Art.toString(),
                MediaType.Category.toString()
            )
            val testMediaSearchTypes = listOf(
                SearchType.MediaObject.toString(),
                SearchType.MediaType.toString()
            )
            val testTitles = listOf("HQ Holden","Prime Ministers")
            val testSubtitles1 = listOf(
                "Art",
                "General Collection Narrative\u001ESearch Page Topic"
            )
            val testSubtitles2 = listOf(
                "sculpture\u001Eautomobiles",
                "Prime Ministers\u001ENew Zealand"
            )
            for (i in 0..1) {
                testFaves.add(
                    FaveEntity(
                        id = 0,
                        /* 14 hours ago, then 14 hours before that */
                        added = System.currentTimeMillis().minus(
                            TimeUnit.HOURS.toMillis(
                                14.times(i.plus(1)).toLong()
                            )
                        ),
                        collection = testCollections[i],
                        href = testHrefs[i],
                        isFave = true,
                        itemId = testItemIds[i],
                        latitude = testLatitudes[i],
                        locationTitle = testLocationTitles[i],
                        longitude = testLongitudes[i],
                        media = testMediaTypes[i],
                        searchType = testMediaSearchTypes[i],
                        subtitle1 = testSubtitles1[i],
                        subtitle2 = testSubtitles2[i],
                        title = testTitles[i]
                    )
                )
            }
            return testFaves
        }
    }
}