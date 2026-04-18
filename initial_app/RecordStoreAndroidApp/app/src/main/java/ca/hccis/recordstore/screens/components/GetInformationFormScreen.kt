package ca.hccis.recordstore.screens.components

import android.content.res.Configuration // <-- NEW IMPORT
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration // <-- NEW IMPORT
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ca.hccis.recordstore.entity.Album
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun GetInformationForm(
    paddingValues: PaddingValues,
    album: Album? = null,
    onSubmit: (String, String, String, Boolean, Double) -> Unit
) {
    var title by remember { mutableStateOf(album?.customerName ?: "") }
    var artist by remember { mutableStateOf(album?.artistName ?: "") }
    var genre by remember { mutableStateOf(album?.formatType ?: "") }
    var basePriceStr by remember { mutableStateOf(album?.albumPrice?.toString() ?: "") }
    var isUsed by remember { mutableStateOf(album?.giftWrapped ?: false) }

    val qrLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            try {
                val data = result.contents.split(",")
                if(data.size >= 3) {
                    artist = data[1].trim()
                    genre = data[2].trim()
                    if(data.size >= 4) {
                        basePriceStr = data[3].trim()
                    }
                }
            } catch (e: Exception) { }
        }
    }

    // 👇 1. Check the device orientation 👇
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        // ==========================================
        // LANDSCAPE LAYOUT (Split into Two Columns)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // LEFT COLUMN: Text Inputs
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(end = 16.dp), // Add spacing between columns
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Customer Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = artist, onValueChange = { artist = it }, label = { Text("Artist Name") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = genre, onValueChange = { genre = it }, label = { Text("Format (Vinyl, etc.)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = basePriceStr, onValueChange = { basePriceStr = it }, label = { Text("Album Price ($)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            }

            // RIGHT COLUMN: Actions (QR, Checkbox, Save)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Center the actions vertically
            ) {
                Button(
                    onClick = {
                        val options = ScanOptions()
                        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                        options.setPrompt("Scan Album QR Code")
                        options.setCameraId(0)
                        options.setBeepEnabled(true)
                        options.setBarcodeImageEnabled(true)
                        qrLauncher.launch(options)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("📷 Scan QR Code")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isUsed, onCheckedChange = { isUsed = it })
                    Text("Gift Wrapped (+$5)")
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
    } else {
        // ==========================================
        // PORTRAIT LAYOUT (Original Single Column)
        // ==========================================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    val options = ScanOptions()
                    options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                    options.setPrompt("Scan Album QR Code")
                    options.setCameraId(0)
                    options.setBeepEnabled(true)
                    options.setBarcodeImageEnabled(true)
                    qrLauncher.launch(options)
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("📷 Scan Album QR Code")
            }

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Customer Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = artist, onValueChange = { artist = it }, label = { Text("Artist Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = genre, onValueChange = { genre = it }, label = { Text("Format (Vinyl, CD, etc.)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = basePriceStr, onValueChange = { basePriceStr = it }, label = { Text("Album Price ($)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isUsed, onCheckedChange = { isUsed = it })
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
}