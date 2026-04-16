package com.example.recordstore.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recordstore.MusicService
import com.example.recordstore.db.AlbumRepository
import com.example.recordstore.entity.Album
import com.example.recordstore.utility.CisUtility
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.example.recordstore.contentprovider.CalendarProviderHelper
import java.util.Calendar


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

        // 👇 1. Check if the phone is in Airplane Mode FIRST
        if (isInAirplaneMode) {
            Toast.makeText(context, "Please turn off Airplane Mode to sync", Toast.LENGTH_LONG).show()
            return@launch // This instantly stops the function so it doesn't even try to connect!
        }

        // 👇 2. If it is NOT in airplane mode, proceed with the normal sync
        Toast.makeText(context, "Syncing...", Toast.LENGTH_SHORT).show()
        try {
            repository.fetchAndSaveAlbumsFromApi()
            Toast.makeText(context, "Synced with API", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Sync Failed: Server Offline", Toast.LENGTH_LONG).show()
        }
    }

    fun playMusic(start: Boolean) {
        val intent = Intent(contextForToast, MusicService::class.java)
        if (start) {
            intent.action = "START"
        } else {
            intent.action = "STOP"
        }
        contextForToast.startService(intent)
    }

    fun setReminderCalendarEvent(album: Album) {
        // Create a timestamp for tomorrow at this exact time
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 5)
        val startTime = calendar.timeInMillis

        // Make the event 1 hour long
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        val endTime = calendar.timeInMillis
        CisUtility.log("CalendarTest", "Scheduling event for: ${calendar.time}")

        try {
            CalendarProviderHelper.addEventToCalendar(
                context = contextForToast,
                title = "Listening Session: ${album.artistName}",
                description = "Time to spin the ${album.formatType} format of ${album.customerName}!",
                startDateMillis = startTime,
                endDateMillis = endTime
            )
            Toast.makeText(contextForToast, "Calendar Event Created!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(contextForToast, "Failed to create event", Toast.LENGTH_SHORT).show()
        }
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