package ca.hccis.recordstore.screens.recordlist

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.hccis.recordstore.entity.Album
import ca.hccis.recordstore.screens.components.AlbumList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    albums: List<Album>,
    snackbarHostState: SnackbarHostState,
    onAddAlbumClick: () -> Unit,
    onEdit: (Album) -> Unit,
    onDelete: (Album) -> Unit,
    onSyncClick: () -> Unit,
    onPlayClick: () -> Unit,
    onStopClick: () -> Unit,
    onSetReminder: (Album) -> Unit,
    onChatClick: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Record Store Sales") },
                actions = {
                    IconButton(onClick = onChatClick) {
                        Icon(Icons.Default.Face, contentDescription = "Store Insights")
                    }
                    IconButton(onClick = onSyncClick) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sync with API")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // 👇 Upgraded Play Button (Music Note + Primary Color)
                    IconButton(onClick = onPlayClick) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Play Music",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // 👇 Upgraded Stop Button (Stop Circle + Error Color)
                    IconButton(onClick = onStopClick) {
                        Icon(
                            imageVector = Icons.Default.StopCircle,
                            contentDescription = "Stop Music",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAlbumClick,
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Record Sale")
                }
            )
        }
    ) { paddingValues ->
        AlbumList(
            albums = albums,
            modifier = Modifier.padding(paddingValues),
            onEdit = onEdit,
            onDelete = onDelete, // MainNavigation handles the Snackbar now
            onSetReminder = onSetReminder
        )
    }
}