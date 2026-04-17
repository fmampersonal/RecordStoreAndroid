package ca.hccis.recordstore.screens.recordsave

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import ca.hccis.recordstore.bo.AlbumBO
import ca.hccis.recordstore.entity.Album
import ca.hccis.recordstore.screens.components.GetInformationForm
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                // Dynamic Title depending on if we are adding or editing
                title = { Text(if (album == null) "Add Sale" else "Edit Sale") },
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

            // 1. Auto-generate today's date automatically
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            // 2. Create a new Sale object using the variables from the entity
            val newAlbum = Album(
                customerName = titleFromForm,
                artistName = artistFromForm,
                formatType = genreFromForm,
                giftWrapped = isUsedFromForm,
                albumPrice = basePriceFromForm,
                dateOfSale = currentDate,      // Automatically stamped!
                totalCost = 0.0
            )

            // 3. Calculate the final price using our Business Object
            newAlbum.totalCost = AlbumBO.calculateFinalPrice(newAlbum)

            // 4. Trigger the callback to notify the app
            onAlbumAdded(newAlbum)
        }
    }
}