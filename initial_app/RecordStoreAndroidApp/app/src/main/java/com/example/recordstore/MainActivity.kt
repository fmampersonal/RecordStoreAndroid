package com.example.recordstore

import android.content.Intent
import android.content.IntentFilter
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
import com.example.recordstore.broadcast.AirplaneModeReceiver
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.provider.Settings
class MainActivity : ComponentActivity() {

    // Declare the receiver
    private lateinit var airplaneModeReceiver: AirplaneModeReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // 1. Initialize the Room Database
        val database = AlbumDatabase.getInstance(this)

        // 2. Initialize the Repository
        val repository = AlbumRepository(database.albumDao)

        // 3. Initialize the ViewModel using the Factory
        val viewModelFactory = AlbumViewModelFactory(repository)
        val albumViewModel = ViewModelProvider(this, viewModelFactory)[AlbumViewModel::class.java]
        albumViewModel.contextForToast = this

        val isCurrentlyInAirplaneMode = Settings.Global.getInt(
            contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
        albumViewModel.setIsInAirplaneMode(isCurrentlyInAirplaneMode)

        // Initialize and register the receiver
        airplaneModeReceiver = AirplaneModeReceiver(albumViewModel)
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(airplaneModeReceiver, it)
        }



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
    override fun onDestroy() {
        super.onDestroy()
        // Prevent memory leaks
        unregisterReceiver(airplaneModeReceiver)
    }
}