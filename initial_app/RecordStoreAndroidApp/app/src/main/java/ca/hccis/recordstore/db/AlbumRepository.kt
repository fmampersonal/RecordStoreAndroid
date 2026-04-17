package ca.hccis.recordstore.db

import ca.hccis.recordstore.dao.AlbumDao
import ca.hccis.recordstore.entity.Album
import ca.hccis.recordstore.utility.ApiUtility
import ca.hccis.recordstore.utility.CisUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AlbumRepository(private val albumDao: AlbumDao) {
    val allAlbums: Flow<List<Album>> = albumDao.getAllAlbums()
    var count: Int = 0

    suspend fun insert(album: Album) {
        // 1. Save locally FIRST so the UI updates instantly!
        albumDao.insert(album)

        // 2. Send it to the Java server in the background
        try {
            ApiUtility.insert(album)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Notice we REMOVED fetchAndSaveAlbumsFromApi() from here.
        // It will no longer auto-sync.
    }

    suspend fun update(album: Album) {
        // 1. Update locally FIRST
        albumDao.update(album)

        // 2. Send the update to Java
        try {
            ApiUtility.insert(album) // Assuming your API uses POST for both insert and update
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun delete(album: Album) {
        albumDao.delete(album) // Local first
        try {
            ApiUtility.delete(album)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // This ONLY runs when you press the Refresh button in the Top Bar!
    suspend fun fetchAndSaveAlbumsFromApi() {
        withContext(Dispatchers.IO) {
            try {
                val albumsFromApi = ApiUtility.fetchAlbums()

                if (albumsFromApi.isNotEmpty()) {
                    albumDao.deleteAll() // Clears the local list
                    albumDao.insertAll(albumsFromApi) // Pulls in the fresh Java data with calculations!
                    CisUtility.log("API", "Synced successfully with server.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e // Re-throw so the ViewModel can show the "Failed" Toast
            }
        }
    }

    suspend fun countAlbums(): Int {
        withContext(Dispatchers.IO) {
            try {
                val albumsFromApi = ApiUtility.fetchAlbums()
                count = albumsFromApi.size
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
        CisUtility.log("BJTEST", "In AlbumRepository.countAlbums:$count")
        return count
    }
}