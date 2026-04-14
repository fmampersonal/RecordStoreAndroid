package com.example.recordstore.screens.componets

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
import com.example.recordstore.entity.Album

@Composable
fun GetInformationForm(
    paddingValues: PaddingValues,
    album: Album? = null,
    onSubmit: (String, String, String, Boolean, Double) -> Unit
) {
    var title by remember { mutableStateOf(album?.title ?: "") }
    var artist by remember { mutableStateOf(album?.artist ?: "") }
    var genre by remember { mutableStateOf(album?.genre ?: "") }
    var basePriceStr by remember { mutableStateOf(album?.basePrice?.toString() ?: "") }
    var isUsed by remember { mutableStateOf(album?.isUsed ?: false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Album Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = artist,
            onValueChange = { artist = it },
            label = { Text("Artist") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = basePriceStr,
            onValueChange = { basePriceStr = it },
            label = { Text("Base Price ($)") },
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
            Text("Is this a used record? (20% Discount)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val price = basePriceStr.toDoubleOrNull() ?: 0.0
                onSubmit(title, artist, genre, isUsed, price)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Album")
        }
    }
}