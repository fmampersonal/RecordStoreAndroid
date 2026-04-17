package ca.hccis.recordstore.screens.insights

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.hccis.recordstore.viewmodel.AlbumViewModel
import androidx.compose.runtime.getValue
import ca.hccis.recordstore.entity.Album

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(viewModel: AlbumViewModel, onNavigateBack: () -> Unit) {
    val albums by viewModel.allAlbums.collectAsState(initial = emptyList<Album>())
    // Trigger analysis whenever the album list changes
    LaunchedEffect(albums) {
        viewModel.runAnalysis(albums)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Store Insights") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            InsightCard(label = "Top Selling Artist", value = viewModel.topArtist.value)
            InsightCard(
                label = "Total Store Revenue",
                value = "$${String.format("%.2f", viewModel.totalRevenue.value)}"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Smart Recommendation", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(viewModel.recommendation.value, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun InsightCard(label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text(value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}