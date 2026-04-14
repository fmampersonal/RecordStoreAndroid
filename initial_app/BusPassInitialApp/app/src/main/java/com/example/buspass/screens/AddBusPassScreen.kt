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

package com.example.buspass.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.example.buspass.bo.BusPassBO
import com.example.buspass.entity.BusPass
import com.example.buspass.screens.componets.GetInformationForm

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
        // Top App Bar
        topBar = {
            TopAppBar(
                title = { Text("Add Bus Pass") }, // Title for the Add Bus Pass screen,
                        navigationIcon = {
                    IconButton(onClick = back) {

                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Edit BusPass")
                    }
                },
            )
        }
    ) { paddingValues ->
        // Form to collect bus pass details
        GetInformationForm(
            paddingValues = paddingValues,
            bussPass = bussPass,
        ) {
            name,
            address,
            city,
            preferredRoute,
            passType,
            validForRuralRoute,
            lengthOfPass,
            startDate ->
            // Create a new BusPass object based on form input
            val newBusPass = BusPass(
                name = name,
                address = address,
                city = city,
                preferredRoute = preferredRoute,
                passType = passType,
                validForRuralRoute = validForRuralRoute,
                lengthOfPass = lengthOfPass,
                startDate = startDate,
                cost = 0.0.toBigDecimal() // Initial cost, calculated later
            )

            // Calculate the cost using the business logic and update the BusPass
            newBusPass.cost = BusPassBO.calculateBusPassCost(newBusPass).toBigDecimal()

            // Trigger the callback to notify the parent about the new bus pass
            onBusPassAdded(newBusPass)
        }
    }
}
