package com.example.recordstore.db

import com.example.recordstore.dao.AlbumDao
import com.example.recordstore.entity.Album
import com.example.recordstore.utility.ApiUtility
import com.example.recordstore.utility.CisUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AlbumRepository(private val albumDao: AlbumDao) {
    val allAlbums: Flow<List<Album>> = albumDao.getAllAlbums()
    var count: Int = 0

    suspend fun insert(album: Album) {
        // 1. Save locally FIRST so the UI updates instantly!
        albumDao.insert(album)

        // 2. Try to send it to the server in the background
        ApiUtility.insert(album)
    }

    suspend fun update(album: Album) {
        albumDao.update(album) // Local first
        ApiUtility.insert(album)
    }

    suspend fun delete(album: Album) {
        albumDao.delete(album) // Local first
        ApiUtility.delete(album)
    }

    suspend fun fetchAndSaveAlbumsFromApi() {
        withContext(Dispatchers.IO) {
            try {
                val albumsFromApi = ApiUtility.fetchAlbums()

                if (albumsFromApi.isNotEmpty()) {
                    albumDao.deleteAll()
                    albumDao.insertAll(albumsFromApi)
                    CisUtility.log("API", "Synced successfully with server.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e // IMPORTANT: Re-throw the error so the ViewModel can see the failure!
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