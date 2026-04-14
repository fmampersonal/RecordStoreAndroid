package com.example.recordstore.db

import com.example.recordstore.dao.AlbumDao
import com.example.recordstore.entity.Album
import com.example.recordstore.utility.ApiUtility // This will be red until the next step!
import com.example.recordstore.utility.CisUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AlbumRepository(private val albumDao: AlbumDao) {
    val allAlbums: Flow<List<Album>> = albumDao.getAllAlbums()
    var count: Int = 0

    suspend fun insert(album: Album) {
        ApiUtility.insert(album) // Send to server
        albumDao.insert(album)   // Save locally
    }

    suspend fun update(album: Album) {
        ApiUtility.insert(album) // The template uses insert for update on the API side
        albumDao.update(album)
    }

    suspend fun delete(album: Album) {
        ApiUtility.delete(album)
        albumDao.delete(album)
    }

    suspend fun fetchAndSaveAlbumsFromApi() {
        withContext(Dispatchers.IO) {
            try {
                // Fetch data from the API
                val albumsFromApi = ApiUtility.fetchAlbums()

                // Delete all local room db albums and replace with those from the api
                albumDao.deleteAll()
                albumDao.insertAll(albumsFromApi)
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
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