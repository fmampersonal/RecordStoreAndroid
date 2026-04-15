package com.example.recordstore.dao

import androidx.room.*
import com.example.recordstore.entity.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    // Insert a single Album entry
    @Insert
    suspend fun insert(album: Album)

    // Insert multiple Album entries
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(albums: List<Album>)

    // Update an existing Album entry
    @Update
    suspend fun update(album: Album)

    // Delete a single Album entry
    @Delete
    suspend fun delete(album: Album)

    // Get all albums as a Flow so the UI updates automatically when data changes
    @Query("SELECT * FROM albums ORDER BY id")
    fun getAllAlbums(): Flow<List<Album>>

    // Get all albums as a static List
// Get all albums as a static List
    @Query("SELECT * FROM albums ORDER BY id")
    suspend fun getAllAlbumsList(): List<Album>

    // Added the suspend keyword here!
    @Query("DELETE FROM albums")
    suspend fun deleteAll()
}