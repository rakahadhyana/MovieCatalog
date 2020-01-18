package com.example.moviecatalog.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class FavoriteMovieProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.example.moviecatalog"
//        const val SCHEME = "content"
//        val CONTENT_URI =
//            Uri.Builder().scheme(SCHEME).authority(AUTHORITY)
//                .appendPath("favorite_table")
//                .build()


        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, "favorite_table", 1)
        }
    }
    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val db = context?.let {
            AppDatabase.getDatabase(it).openHelper.readableDatabase
        }
        return db?.query("SELECT * FROM favorite_table WHERE type='movie'")
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}
