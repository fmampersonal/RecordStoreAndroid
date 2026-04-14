package com.example.recordstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.recordstore.db.AlbumDatabase
import com.example.recordstore.db.AlbumRepository
import com.example.recordstore.screens.MainNavigation
import com.example.recordstore.ui.theme.RecordStoreAppTheme
import com.example.recordstore.viewmodel.AlbumViewModel
import com.example.recordstore.viewmodel.AlbumViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize the Room Database
        val database = AlbumDatabase.getInstance(this)

        // 2. Initialize the Repository
        val repository = AlbumRepository(database.albumDao)

        // 3. Initialize the ViewModel using the Factory
        val viewModelFactory = AlbumViewModelFactory(repository)
        val albumViewModel = ViewModelProvider(this, viewModelFactory)[AlbumViewModel::class.java]
        albumViewModel.contextForToast = this

        setContent {
            RecordStoreAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pass the ViewModel straight into our Navigation component
                    MainNavigation(viewModel = albumViewModel)
                }
            }
        }
    }
}