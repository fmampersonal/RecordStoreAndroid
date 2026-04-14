package com.example.recordstore.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
    onDelete: (Album) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Record Store") }
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
        // Display the list of albums 
        // (We will update this list component in the next step!)
        AlbumList(
            albums = albums,
            modifier = Modifier.padding(paddingValues),
            onEdit = onEdit,
            onDelete = onDelete
        )
    }
}