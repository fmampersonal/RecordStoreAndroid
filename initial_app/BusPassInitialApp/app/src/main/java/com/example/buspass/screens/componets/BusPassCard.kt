/**
 * BusPassCard.kt
 *
 * Displays a card containing detailed information about a bus pass in a formatted way.
 * Provides options to edit or delete the bus pass.
 *
 * @Author: Anthony Odu
 * @Date: 2025-01-11
 */

package com.example.buspass.screens.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.buspass.entity.BusPass
import com.example.buspass.utility.CisUtility
import com.example.buspass.utility.Dimensions

/**
 * BusPassCard
 *
 * Displays a formatted summary of a bus pass's details in a card layout.
 * It allows for editing or deleting the bus pass with confirmation for deletion.
 *
 * @param busPass The BusPass object containing the data to be displayed.
 * @param onEdit The callback function to be invoked when the edit button is clicked.
 *               Passes the current bus pass for editing.
 * @param onDelete The callback function to be invoked when the delete button is clicked.
 *                 Passes the current bus pass for deletion.
 */
@Composable
fun BusPassCard(
    busPass: BusPass,
    onEdit: (BusPass) -> Unit, // Callback to handle the edit action
    onDelete: (BusPass) -> Unit // Callback to handle the delete action
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Card containing bus pass details
    Card(
        modifier = Modifier
            .fillMaxWidth() // Ensures the card spans the full width of the container
            .padding(vertical = Dimensions.space8), // Adds vertical spacing between cards
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.space4) // Slight elevation for a shadow effect
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.space8)
        ) {
            // Build the formatted string for the bus pass details
            val busPassDetails = "\nBusPass(${busPass.id})\n" +
                    "Name=${busPass.name}\n" +
                    "Address=${busPass.address}\n" +
                    "City=${busPass.city}\n" +
                    "Preferred Route=${busPass.preferredRoute}\nPass Type=${busPass.passType}\n" +
                    "Valid For Rural Route=${busPass.validForRuralRoute}\nLength Of Pass=${busPass.lengthOfPass}\n" +
                    "Start Date=${busPass.startDate}\nCost=${CisUtility.toCurrency(busPass.cost.toDouble())}"

            // Display the bus pass details inside the card
            Text(
                text = busPassDetails,
                style = MaterialTheme.typography.bodyMedium
            )

            // Action buttons for editing and deleting
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Edit button
                IconButton(onClick = { onEdit(busPass) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit BusPass")
                }

                // Delete button
                IconButton(onClick = { showDeleteConfirmation = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete BusPass")
                }
            }
        }

        // Show delete confirmation dialog
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete BusPass") },
                text = { Text("Are you sure you want to delete this BusPass?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDelete(busPass)
                            showDeleteConfirmation = false
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
