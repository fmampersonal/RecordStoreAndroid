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
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.DateRange

@Composable
fun AlbumCard(
    album: Album,
    onEdit: (Album) -> Unit,
    onDelete: (Album) -> Unit,
    onSetReminder: (Album) -> Unit // <-- Add this parameter

) {
    val context = LocalContext.current // Get the context for the Intent
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = album.customerName, // Changed from title
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "by ${album.artistName}", // Changed from artist
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Format: ${album.formatType} | ${if (album.giftWrapped) "Gift Wrapped" else "Standard"}", // Updated labels
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Price: ${CisUtility.toCurrency(album.totalCost)}", // Changed from finalPrice
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row {
                // 1. Add the Share Button
                IconButton(onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        // This is the message that will be sent to Twitter/WhatsApp/SMS
                        putExtra(Intent.EXTRA_TEXT, "Hey! I just bought the ${album.formatType} format of ${album.customerName} by ${album.artistName}!")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }) {
                    Icon(Icons.Default.Share, contentDescription = "Share Album")
                }

            Row {
                IconButton(onClick = { onEdit(album) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Album")
                }
                IconButton(onClick = { onDelete(album) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Album")
                }
                IconButton(onClick = { onSetReminder(album) }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Set Reminder")
                }
            }
        }
    }
}}