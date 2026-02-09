package com.michaelrmossman.collections.database

import androidx.sqlite.db.SimpleSQLiteQuery
import com.michaelrmossman.collections.enum.SortFavesBy

object DbHelpers {

    fun getFavesQuery(
        sortFavesBy: SortFavesBy
    ) : SimpleSQLiteQuery {
        // android.util.Log.d("HEY",sortFavesBy.toString())

        val columnSort = when (sortFavesBy) {
            SortFavesBy.Date -> COLUMN_NAME_FAVE_ADDED
            SortFavesBy.Name -> COLUMN_NAME_FAVE_TITLE
            SortFavesBy.Type -> COLUMN_NAME_MEDIA
        }

        val args: ArrayList<Any> = arrayListOf(columnSort)
        val sb = StringBuilder()

        sb.append("SELECT")
        sb.append(" ")
        sb.append("*")
        sb.append(" ")
        sb.append("FROM $TABLE_NAME_FAVOURITE")
        sb.append(" ")

        sb.append("ORDER BY ?")
        sb.append(" ")
        /* Allow for Māori special chars when sorting */
        sb.append("COLLATE UNICODE")

        // android.util.Log.d("HEY",sb.toString())
        return SimpleSQLiteQuery(sb.toString(), args.toTypedArray())
    }
}