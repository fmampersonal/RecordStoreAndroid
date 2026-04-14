/**
 * BusPassCard.kt
 *
 * Description: Enhanced the GetInformationForm with additional fields for address, city, preferred route,
 * rural route validity, length of pass, and start date, with error handling and validation for each.
 *
 * @Author: Anthony Odu
 * @Date: 2025-01-11
 */
package ca.hccis.buspass.screens.buspassqr

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import ca.hccis.buspass.utility.Dimensions
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import ca.hccis.buspass.entity.BusPass
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


/**
 * Composable function to display qr codes
 *
 * @param paddingValues Optional padding values to be applied to the form.
 */
@Composable
fun QRCodeScreen(
    paddingValues: PaddingValues,
    busPass: BusPass? = null
) {

    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var qrBitmapUrl by remember { mutableStateOf<Bitmap?>(null) }

    Column( modifier = Modifier
        .padding(paddingValues)
        .fillMaxWidth()
        .padding(Dimensions.space16)
    ) {
        qrBitmap = busPass?.let { generateQRCode(it) }

        Spacer(modifier = Modifier.height(16.dp))

        qrBitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = "Bus Pass QR Code")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (busPass != null) {
            qrBitmap = busPass.id.let { generateQRCode(it) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        qrBitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = "Bus Pass URL QR Code")
        }

    }
}

fun generateQRCode(busPass: BusPass): Bitmap? {
    return try {
        val gson = Gson()
        val busPassJson = gson.toJson(busPass)
        val barcodeEncoder = BarcodeEncoder()
        barcodeEncoder.encodeBitmap(busPassJson, BarcodeFormat.QR_CODE, 400, 400)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun generateQRCode(busPassId: Int): Bitmap? {
    return try {
        val barcodeEncoder = BarcodeEncoder()
        barcodeEncoder.encodeBitmap(
            "localhost:8081/buspass/edit/" + busPassId,
            BarcodeFormat.QR_CODE,
            400,
            400
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


