package com.example.recordstore.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recordstore.entity.Album
import com.example.recordstore.viewmodel.AlbumViewModel
import com.example.recordstore.chat.ChatPage // <-- ADDED IMPORT

enum class Screen(val route: String) {
    MainScreen("main_screen"),
    AddAlbum("add_album"),
    Chat("chat") // <-- ADDED CHAT ROUTE
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MainNavigation(viewModel: AlbumViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

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
                    viewModel.delete(albumToRemove)
                },
                onSyncClick = {
                    viewModel.syncWithApi(context)
                },
                onPlayClick = {
                    viewModel.playMusic(true)
                },
                onStopClick = {
                    viewModel.playMusic(false)
                },
                onSetReminder = { selectedAlbum ->
                    viewModel.setReminderCalendarEvent(selectedAlbum)
                },
                onChatClick = { // <-- ADDED CHAT NAVIGATION HANDLER
                    navController.navigate(Screen.Chat.route)
                }
            )
        }

        composable(Screen.AddAlbum.route) {
            AddAlbumScreen(
                album = albumToEdit,
                onAlbumAdded = { newAlbum ->
                    if (albumToEdit == null) {
                        viewModel.insert(newAlbum)
                    } else {
                        newAlbum.id = albumToEdit!!.id
                        viewModel.update(newAlbum)
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

        // 👇 ADDED THE CHAT SCREEN COMPOSABLE HERE 👇
        composable(Screen.Chat.route) {
            ChatPage(
                albumViewModel = viewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}