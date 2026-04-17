package ca.hccis.recordstore.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ca.hccis.recordstore.entity.Album

@Composable
fun GetInformationForm(
    paddingValues: PaddingValues,
    album: Album? = null,
    onSubmit: (String, String, String, Boolean, Double) -> Unit
) {
    // Update these to use the new names from your Album entity
    var title by remember { mutableStateOf(album?.customerName ?: "") }       // Changed .title to .customerName
    var artist by remember { mutableStateOf(album?.artistName ?: "") }       // Changed .artist to .artistName
    var genre by remember { mutableStateOf(album?.formatType ?: "") }         // Changed .genre to .formatType
    var basePriceStr by remember { mutableStateOf(album?.albumPrice?.toString() ?: "") } // Changed .basePrice to .albumPrice
    var isUsed by remember { mutableStateOf(album?.giftWrapped ?: false) }     // Changed .isUsed to .giftWrapped

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... rest of your OutlinedTextFields remain exactly the same ...
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Customer Name") }, // Updated label for clarity
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = artist,
            onValueChange = { artist = it },
            label = { Text("Artist Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text("Format (Vinyl, CD, etc.)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = basePriceStr,
            onValueChange = { basePriceStr = it },
            label = { Text("Album Price ($)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isUsed,
                onCheckedChange = { isUsed = it }
            )
            Text("Gift Wrapped(+$5)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val price = basePriceStr.toDoubleOrNull() ?: 0.0
                onSubmit(title, artist, genre, isUsed, price)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Record Sale")
        }
    }
}