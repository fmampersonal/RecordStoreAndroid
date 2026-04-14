package com.example.recordstore.screens.componets

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.recordstore.entity.Album
import com.example.recordstore.utility.CisUtility

@Composable
fun AlbumCard(
    album: Album,
    onEdit: (Album) -> Unit,
    onDelete: (Album) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Album Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "by ${album.artist}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Genre: ${album.genre} | ${if (album.isUsed) "Used" else "New"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Price: ${CisUtility.toCurrency(album.finalPrice.toDouble())}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Right side: Action Buttons (Edit and Delete)
            Row {
                IconButton(onClick = { onEdit(album) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Album")
                }
                IconButton(onClick = { onDelete(album) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Album")
                }
            }
        }
    }
}