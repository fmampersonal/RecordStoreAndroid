package com.example.recordstore.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close // <-- Added Icon
import androidx.compose.material.icons.filled.PlayArrow // <-- Added Icon
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
    onSyncClick: () -> Unit,
    onPlayClick: () -> Unit, // <-- Added Parameter
    onStopClick: () -> Unit  // <-- Added Parameter
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Store") },
                actions = {
                    // Added Play Button
                    IconButton(onClick = onPlayClick) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Play Music")
                    }
                    // Added Stop Button
                    IconButton(onClick = onStopClick) {
                        Icon(Icons.Default.Close, contentDescription = "Stop Music")
                    }
                    // Existing Sync Button
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