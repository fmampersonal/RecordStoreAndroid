package ca.hccis.recordstore.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import ca.hccis.recordstore.db.AlbumDatabase

class AlbumContentProvider : ContentProvider() {

    // This block sets up the secure "address" other apps use to find your data
    companion object {
        const val AUTHORITY = "ca.hccis.recordstore.provider"
        const val ALBUM_TABLE = "albums"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$ALBUM_TABLE")

        const val ALBUMS = 1
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, ALBUM_TABLE, ALBUMS)
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    // 👇 This is the core function. When another app asks for data, we give them the Cursor! 👇
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val context = context ?: return null

        // Grab an instance of your Room database
        val database = AlbumDatabase.getInstance(context)
        val dao = database.albumDao

        return when (uriMatcher.match(uri)) {
            ALBUMS -> dao.getAlbumsCursor() // Calls the new DAO method we just made
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            ALBUMS -> "vnd.android.cursor.dir/$AUTHORITY.$ALBUM_TABLE"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    // We block other apps from Editing, Deleting, or Adding data for security.
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Insert not allowed via Content Provider")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Delete not allowed via Content Provider")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Update not allowed via Content Provider")
    }
}