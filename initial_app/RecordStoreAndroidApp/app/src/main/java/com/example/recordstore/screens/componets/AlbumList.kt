package com.example.recordstore.screens.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recordstore.entity.Album

@Composable
fun AlbumList(
    albums: List<Album>,
    modifier: Modifier = Modifier,
    onEdit: (Album) -> Unit,
    onDelete: (Album) -> Unit,
    onSetReminder: (Album) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(albums) { album ->
            // This calls our individual Card design for each album
            AlbumCard(
                album = album,
                onEdit = onEdit,
                onDelete = onDelete,
                onSetReminder = onSetReminder
            )
        }
    }
}