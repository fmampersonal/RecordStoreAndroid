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

package ca.hccis.buspass.screens.buspassadd

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ca.hccis.buspass.bo.BusPassBO
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.screens.components.GetInformationForm

/**
 * AddBusPassScreen
 *
 * Displays a form for adding a new bus pass. Collects user input for various
 * fields like name, address, city, and calculates the cost based on the input.
 *
 * @param onBusPassAdded A callback triggered after the new bus pass is created and calculated.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusPassScreen(
    bussPass: BusPass? = null,
    onBusPassAdded: (BusPass) -> Unit,
    back: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Bus Pass") },
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
            bussPass = bussPass,
        ) { name, address, city, preferredRoute, passType, validForRuralRoute, lengthOfPass, startDate ->
            val newBusPass = bussPass?.copy(
                name = name,
                address = address,
                city = city,
                preferredRoute = preferredRoute,
                passType = passType,
                validForRuralRoute = validForRuralRoute,
                lengthOfPass = lengthOfPass,
                startDate = startDate
            ) ?: BusPass(
                name = name,
                address = address,
                city = city,
                preferredRoute = preferredRoute,
                passType = passType,
                validForRuralRoute = validForRuralRoute,
                lengthOfPass = lengthOfPass,
                startDate = startDate,
                cost = 0.0.toBigDecimal() //d
            )

//             Calculate the cost using the business logic and update the BusPass
            newBusPass.cost = BusPassBO.calculateBusPassCost(newBusPass).toBigDecimal()
            onBusPassAdded(newBusPass)


        }
    }
}
