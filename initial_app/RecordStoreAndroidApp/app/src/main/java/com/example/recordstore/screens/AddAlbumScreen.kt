package com.example.recordstore.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.example.recordstore.bo.AlbumBO
import com.example.recordstore.entity.Album
import com.example.recordstore.screens.componets.GetInformationForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlbumScreen(
    album: Album? = null,
    onAlbumAdded: (Album) -> Unit,
    back: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Album") },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                },
            )
        }
    ) { paddingValues ->
        // Call the form and wait for the user to submit Album details
        GetInformationForm(
            paddingValues = paddingValues,
            album = album,
        ) { title, artist, genre, isUsed, basePrice ->

            // 1. Create a new Album object based on form input
            val newAlbum = Album(
                title = title,
                artist = artist,
                genre = genre,
                isUsed = isUsed,
                basePrice = basePrice,
                finalPrice = 0.0.toBigDecimal()
            )

            // 2. Calculate the final price using our Business Object
            newAlbum.finalPrice = AlbumBO.calculateFinalPrice(newAlbum).toBigDecimal()

            // 3. Trigger the callback to notify the app
            onAlbumAdded(newAlbum)
        }
    }
}