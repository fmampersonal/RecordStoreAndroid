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
        GetInformationForm(
            paddingValues = paddingValues,
            album = album,
        ) { titleFromForm, artistFromForm, genreFromForm, isUsedFromForm, basePriceFromForm ->

            // 1. Create a new Album object using the NEW variable names from the entity
            val newAlbum = Album(
                customerName = titleFromForm,  // Updated
                artistName = artistFromForm,    // Updated
                formatType = genreFromForm,    // Updated
                giftWrapped = isUsedFromForm,   // Updated
                albumPrice = basePriceFromForm, // Updated
                totalCost = 0.0                // Updated
            )

            // 2. Calculate the final price using our Business Object
            newAlbum.totalCost = AlbumBO.calculateFinalPrice(newAlbum)

            // 3. Trigger the callback to notify the app
            onAlbumAdded(newAlbum)
        }
    }
}