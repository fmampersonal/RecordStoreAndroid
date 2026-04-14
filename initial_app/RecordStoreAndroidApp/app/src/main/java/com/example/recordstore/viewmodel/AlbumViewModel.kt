package com.example.recordstore.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recordstore.db.AlbumRepository
import com.example.recordstore.entity.Album
import com.example.recordstore.utility.CisUtility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class AlbumViewModel(private val repository: AlbumRepository) : ViewModel() {

    var isInAirplaneMode: Boolean = false
    lateinit var contextForToast: Context

    fun setIsInAirplaneMode(isEnabled: Boolean) {
        CisUtility.log("BJTEST", "Setting the airplane mode to $isEnabled")
        isInAirplaneMode = isEnabled
    }

    // Flow to observe all albums from the Room database
    val allAlbums: Flow<List<Album>> = repository.allAlbums

    fun insert(album: Album) = viewModelScope.launch {
        repository.insert(album)
    }

    fun update(album: Album) = viewModelScope.launch {
        repository.update(album)
    }

    fun delete(album: Album) = viewModelScope.launch {
        repository.delete(album)
    }

    fun syncWithApi(context: Context) = viewModelScope.launch {
        Toast.makeText(context, "Syncing...", Toast.LENGTH_SHORT).show()
        try {
            // If this line fails/throws an error, it skips the next line!
            repository.fetchAndSaveAlbumsFromApi()

            // This line ONLY runs if the line above worked perfectly
            Toast.makeText(context, "Synced with API", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            // This runs if the server is offline or the code is wrong
            Toast.makeText(context, "Sync Failed: Server Offline", Toast.LENGTH_LONG).show()
        }
    }

    fun playMusic(start: Boolean) {
        // TODO: Implement BackgroundMusicService (Sprint 4)
        Toast.makeText(contextForToast, "Music service coming in Sprint 4!", Toast.LENGTH_SHORT).show()
    }

    fun setReminderCalendarEvent(album: Album) {
        // TODO: Implement Calendar Provider (Sprint 4)
        Toast.makeText(contextForToast, "Calendar feature coming in Sprint 4!", Toast.LENGTH_SHORT).show()
    }
}

class AlbumViewModelFactory(private val repository: AlbumRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}