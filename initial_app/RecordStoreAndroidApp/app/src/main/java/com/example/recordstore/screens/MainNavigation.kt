package com.example.recordstore.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recordstore.entity.Album

enum class Screen(val route: String) {
    MainScreen("main_screen"),
    AddAlbum("add_album")
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    var albums by remember { mutableStateOf(mutableListOf<Album>()) }
    var album: Album? = null
    var id by remember { mutableIntStateOf(1) }
    var editId by remember { mutableIntStateOf(0) }

    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        // Main screen composable
        composable(Screen.MainScreen.route) {
            HomeScreen(
                albums = albums,
                onAddAlbumClick = { navController.navigate(Screen.AddAlbum.route) },
                onEdit = { selectedAlbum ->
                    album = selectedAlbum
                    editId = selectedAlbum.id
                    navController.navigate(Screen.AddAlbum.route)
                },
                onDelete = { albumToRemove ->
                    albums = albums.toMutableList().apply { remove(albumToRemove) }
                }
            )
        }

        // Add album screen composable
        composable(Screen.AddAlbum.route) {
            AddAlbumScreen(
                album = album,
                onAlbumAdded = { newAlbum ->
                    albums = if (album == null) {
                        // Add new Album
                        albums.toMutableList().apply {
                            newAlbum.id = id
                            add(newAlbum)
                            id++
                        }
                    } else {
                        // Update existing Album
                        albums.toMutableList().apply {
                            val index = indexOfFirst { it.id == album!!.id }
                            if (index != -1) {
                                newAlbum.id = editId
                                set(index, newAlbum)
                            }
                        }
                    }

                    // Reset states
                    album = null
                    editId = 0
                    // Navigate back to the main screen
                    navController.navigateUp()
                },
                back = {
                    navController.navigateUp()
                }
            )
        }
    }
}