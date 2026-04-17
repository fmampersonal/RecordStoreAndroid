package ca.hccis.recordstore.screens.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ca.hccis.recordstore.entity.Album
import ca.hccis.recordstore.utility.CisUtility
import ca.hccis.recordstore.utility.QRCodeGenerator

@Composable
fun AlbumCard(
    album: Album,
    onEdit: (Album) -> Unit,
    onDelete: (Album) -> Unit,
    onSetReminder: (Album) -> Unit
) {
    val context = LocalContext.current

    // Dialog States
    var showQrDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Card(
        // Matching the colored card style from BusPassCard
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit(album) }
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 1. The Main Headline Text
            Text(
                text = album.customerName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 2. The Formatted Details String (Like BusPassCard)
            val albumDetails = "Artist: ${album.artistName}\n" +
                    "Format: ${album.formatType}\n" +
                    "Type: ${if (album.giftWrapped) "Gift Wrapped" else "Standard"}\n" +
                    "Price: ${CisUtility.toCurrency(album.totalCost)}"

            Text(
                text = albumDetails,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. The Action Buttons arranged at the bottom right
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Share Button
                IconButton(onClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Hey! I just bought the ${album.formatType} format of ${album.customerName} by ${album.artistName}!")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }) {
                    Icon(Icons.Default.Share, contentDescription = "Share Album")
                }

                // Calendar Reminder Button
                IconButton(onClick = { onSetReminder(album) }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Set Reminder")
                }

                // QR Code Button
                IconButton(onClick = { showQrDialog = true }) {
                    Icon(Icons.Default.QrCode, contentDescription = "Show QR Code")
                }

                // Edit Button
                IconButton(onClick = { onEdit(album) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Album")
                }

                // Delete Button (Triggers Confirmation)
                IconButton(onClick = { showDeleteConfirmation = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Album")
                }
            }
        }

        // --- DIALOGS ---

        // QR Code Dialog
        if (showQrDialog) {
            val qrText = "Album: ${album.customerName}\nArtist: ${album.artistName}\nFormat: ${album.formatType}\nPrice: $${album.totalCost}"
            val qrBitmap = remember { QRCodeGenerator.generateQrCode(qrText) }

            AlertDialog(
                onDismissRequest = { showQrDialog = false },
                confirmButton = {
                    TextButton(onClick = { showQrDialog = false }) { Text("Close") }
                },
                title = { Text("Album Details") },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "QR Code Image",
                            modifier = Modifier.size(250.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = qrText, textAlign = TextAlign.Center)
                    }
                }
            )
        }

        // Delete Confirmation Dialog (Ported from BusPass)
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete Album") },
                text = { Text("Are you sure you want to delete this album?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDelete(album)
                            showDeleteConfirmation = false
                        }
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}