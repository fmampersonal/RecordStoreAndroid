package com.example.recordstore.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recordstore.entity.Album
import com.example.recordstore.viewmodel.AlbumViewModel

enum class Screen(val route: String) {
    MainScreen("main_screen"),
    AddAlbum("add_album")
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MainNavigation(viewModel: AlbumViewModel) { // Accept the ViewModel here!
    val navController = rememberNavController()
    val context = LocalContext.current

    // MAGIC HAPPENS HERE: Observe the Room database in real-time!
    val albums by viewModel.allAlbums.collectAsState(initial = emptyList())
    var albumToEdit: Album? by remember { mutableStateOf(null) }

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {

        composable(Screen.MainScreen.route) {
            HomeScreen(
                albums = albums,
                onAddAlbumClick = {
                    albumToEdit = null
                    navController.navigate(Screen.AddAlbum.route)
                },
                onEdit = { selectedAlbum ->
                    albumToEdit = selectedAlbum
                    navController.navigate(Screen.AddAlbum.route)
                },
                onDelete = { albumToRemove ->
                    viewModel.delete(albumToRemove) // Uses the Database!
                },
                onSyncClick = {
                    viewModel.syncWithApi(context) // Triggers your ApiUtility!
                },

                onPlayClick = {                            // <-- Add this block
                    viewModel.playMusic(true)
                },
                onStopClick = {                            // <-- Add this block
                    viewModel.playMusic(false)
                },
                onSetReminder = { selectedAlbum ->                  // <-- Add this block
                    viewModel.setReminderCalendarEvent(selectedAlbum)
                }

            )
        }

        composable(Screen.AddAlbum.route) {
            AddAlbumScreen(
                album = albumToEdit,
                onAlbumAdded = { newAlbum ->
                    if (albumToEdit == null) {
                        viewModel.insert(newAlbum) // Save new to Database
                    } else {
                        newAlbum.id = albumToEdit!!.id // Keep the old ID so it updates, not duplicates
                        viewModel.update(newAlbum) // Update in Database
                    }
                    albumToEdit = null
                    navController.navigateUp()
                },
                back = {
                    albumToEdit = null
                    navController.navigateUp()
                }
            )
        }
    }
}