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
        CisUtility.log("BJTEST", "Starting syncWithApi airplane enabled: $isInAirplaneMode")
        try {
            if (!isInAirplaneMode) {
                CisUtility.log("BJTEST", "syncing with api")
                repository.fetchAndSaveAlbumsFromApi()
                Toast.makeText(contextForToast, "Synced with API", Toast.LENGTH_LONG).show()
            } else {
                CisUtility.log("BJTEST", "can not sync in airplane mode")
                Toast.makeText(contextForToast, "Cannot sync while in airplane mode", Toast.LENGTH_LONG).show()
            }

            // TODO: Trigger Widget Update (Sprint 4)
            // AlbumWidget().updateAll(context)

        } catch (e: Exception) {
            CisUtility.log("BJTEST", "Exception sync with api")
            Toast.makeText(contextForToast, "Exception occurred during sync", Toast.LENGTH_LONG).show()
            e.printStackTrace()
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