package ca.hccis.recordstore.screens.recordlist

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Place // <-- NEW IMPORT
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ca.hccis.recordstore.R
import ca.hccis.recordstore.entity.Album
import ca.hccis.recordstore.screens.components.AlbumList
import kotlin.math.roundToInt

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
    onInsightsClick: () -> Unit,
    onGeminiClick: () -> Unit,
    onWebClick: () -> Unit,
    onMapClick: () -> Unit // <-- ADDED MAP PARAMETER
) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("RecordStorePrefs", Context.MODE_PRIVATE) }
    var managerName by remember { mutableStateOf(sharedPrefs.getString("manager_name", "Admin") ?: "Admin") }

    var showSettingsDialog by remember { mutableStateOf(false) }
    var tempNameInput by remember { mutableStateOf(managerName) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    var showCarousel by remember { mutableStateOf(true) }

    val pagerState = rememberPagerState(pageCount = { 3 })
    val featuredImages = listOf(
        R.drawable.floyd,
        R.drawable.beatles,
        R.drawable.mac
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Welcome, $managerName") },
                actions = {
                    IconButton(onClick = {
                        tempNameInput = managerName
                        showSettingsDialog = true
                    }) { Icon(Icons.Default.Settings, contentDescription = "Settings") }
                    IconButton(onClick = onInsightsClick) { Icon(Icons.Default.Face, "Insights") }
                    IconButton(onClick = onSyncClick) { Icon(Icons.Default.Refresh, "Sync") }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onPlayClick) { Icon(Icons.Default.MusicNote, "Play", tint = MaterialTheme.colorScheme.primary) }
                    IconButton(onClick = onStopClick) { Icon(Icons.Default.StopCircle, "Stop", tint = MaterialTheme.colorScheme.error) }
                    IconButton(onClick = onWebClick) { Icon(Icons.Default.Public, "Open Web") }
                    IconButton(onClick = onMapClick) { Icon(Icons.Default.Place, "Store Location") } // <-- MAP PIN BUTTON ADDED
                }
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            Column(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Featured Vintage Arrivals",
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(onClick = { showCarousel = !showCarousel }) {
                        Icon(
                            imageVector = if (showCarousel) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Carousel Visibility"
                        )
                    }
                }

                AnimatedVisibility(
                    visible = showCarousel,
                    enter = expandVertically(animationSpec = tween(500)),
                    exit = shrinkVertically(animationSpec = tween(500))
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.height(180.dp).fillMaxWidth()
                    ) { page ->
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Image(
                                painter = painterResource(id = featuredImages[page]),
                                contentDescription = "Featured Album $page",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                AlbumList(
                    albums = albums,
                    modifier = Modifier.weight(1f),
                    onEdit = onEdit,
                    onDelete = onDelete,
                    onSetReminder = onSetReminder
                )
            }

            // Draggable Floating Buttons
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
            ) {
                SmallFloatingActionButton(
                    onClick = onGeminiClick,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) { Icon(Icons.Default.AutoAwesome, "Ask AI Chatbot") }
                Spacer(modifier = Modifier.height(8.dp))
                FloatingActionButton(onClick = onAddAlbumClick) { Icon(Icons.Default.Add, "Record Sale") }
            }
        }

        // Settings Dialog
        if (showSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showSettingsDialog = false },
                title = { Text("Store Settings") },
                text = {
                    OutlinedTextField(
                        value = tempNameInput,
                        onValueChange = { tempNameInput = it },
                        label = { Text("Store Manager Name") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        sharedPrefs.edit().putString("manager_name", tempNameInput).apply()
                        managerName = tempNameInput
                        showSettingsDialog = false
                    }) { Text("Save") }
                },
                dismissButton = {
                    TextButton(onClick = { showSettingsDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}