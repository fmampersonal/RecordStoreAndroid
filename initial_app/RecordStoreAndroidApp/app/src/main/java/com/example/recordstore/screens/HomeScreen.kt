package com.example.recordstore.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.recordstore.entity.Album
import com.example.recordstore.screens.componets.AlbumList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    albums: List<Album>,
    onAddAlbumClick: () -> Unit,
    onEdit: (Album) -> Unit,
    onDelete: (Album) -> Unit,
    onSyncClick: () -> Unit // Added the sync parameter
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Store") },
                actions = {
                    // Added a button to the Top Bar to trigger the API!
                    IconButton(onClick = onSyncClick) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sync with API")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAlbumClick,
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Add Album")
                }
            )
        }
    ) { paddingValues ->
        AlbumList(
            albums = albums,
            modifier = Modifier.padding(paddingValues),
            onEdit = onEdit,
            onDelete = onDelete
        )
    }
}