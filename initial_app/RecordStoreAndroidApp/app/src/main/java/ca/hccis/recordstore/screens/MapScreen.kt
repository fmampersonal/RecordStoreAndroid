package ca.hccis.recordstore.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onNavigateBack: () -> Unit
) {
    // Exact coordinates for Holland College!
    val hollandCollege = LatLng(46.2366, -63.1218)

    // Controls where the camera starts looking and how far zoomed in it is
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(hollandCollege, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Find Our Store") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        // The actual Google Map Compose component
        GoogleMap(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            cameraPositionState = cameraPositionState
        ) {
            // Drops a red pin on the map
            Marker(
                state = MarkerState(position = hollandCollege),
                title = "Retro Records @ Holland College",
                snippet = "Vintage Vinyl & CDs"
            )
        }
    }
}