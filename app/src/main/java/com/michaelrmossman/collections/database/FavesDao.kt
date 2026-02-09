package com.michaelrmossman.collections.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.michaelrmossman.collections.data.FaveEntity
import com.michaelrmossman.collections.data.SettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavesDao {

    @Query("DELETE FROM $TABLE_NAME_FAVOURITE")
    suspend fun deleteAllFavourites(): Int

    @Delete
    suspend fun deleteFave(fave: FaveEntity): Int

    @Query("""
        DELETE FROM $TABLE_NAME_FAVOURITE
        WHERE itemId = :itemId
        AND media = :itemType
    """)
    suspend fun deleteFaveByIdAndType(
        itemId: Int, itemType: String
    ) : Int

    @Query("""
        SELECT * FROM $TABLE_NAME_FAVOURITE
        WHERE id = :id
    """)
    suspend fun getFaveById(id: String): FaveEntity

    @Query("""
        SELECT COUNT(*)
        FROM $TABLE_NAME_FAVOURITE
    """)
    fun getFaveCount(): Flow<Int>

//    @RawQuery(
//        observedEntities = [
//            FaveEntity::class,
//            SettingEntity::class
//        ]
//    )
//    fun getFavesFlow(
//        query: SimpleSQLiteQuery
//    ) : Flow<List<FaveEntity>>

    @Query("SELECT * FROM $TABLE_NAME_FAVOURITE")
    fun getFavesFlow(): Flow<List<FaveEntity>>

//    @Query("""
//        SELECT * FROM $TABLE_NAME_FAVOURITE
//        ORDER BY :sortFavesBy
//    """)
//    fun getFavesFlow(
//        sortFavesBy: String
//    ) : Flow<List<FaveEntity>>

    @Insert
    suspend fun insertFave(fave: FaveEntity): Long

    @Insert
    suspend fun insertFaves(faves: List<FaveEntity>)

    @Query("""
        SELECT EXISTS
        (
            SELECT itemId,media
            FROM $TABLE_NAME_FAVOURITE
            WHERE itemId = :itemId
            AND media = :itemType
            LIMIT 1
        )
    """)
    suspend fun isFavourite(
        itemId: Int, itemType: String
    ) : Boolean

    @Update
    suspend fun updateFave(fave: FaveEntity)
}