/**
 * AddBusPassScreen.kt
 *
 * Provides a screen for adding a new bus pass in the Bus Pass Manager application.
 *
 * This screen includes a form to collect user input for creating a new bus pass.
 * Upon submission, the calculated cost is added, and the new bus pass is passed
 * back to the parent component via a callback.
 *
 * @Author: Anthony Odu
 * @Date: 2025-01-11
 */

package ca.hccis.buspass.screens.buspassqr

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ca.hccis.buspass.bo.BusPassBO
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.screens.components.GetInformationForm
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.math.BigDecimal
import java.time.LocalDate

/**
 * QR Code Screen
 *
 * Displays a qr code from the bus pass passed and a qr code wtih a link to the web
 * application which could take the user to the web to see the bus pass.
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeBusPassScreen(
    busPass: BusPass? = null,
    back: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QR Codes") },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        QRCodeScreen(
            paddingValues = paddingValues,
            busPass = busPass,
        )
    }
}
