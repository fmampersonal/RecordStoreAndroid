/**
 * HomeScreen.kt
 *
 * Displays the main screen of the Bus Pass Manager application.
 *
 * This screen includes a top app bar, a floating action button (FAB) for navigating
 * to the "Add Bus Pass" screen, and a list of bus passes.
 *
 * @Author: Anthony Odu
 * @Date: 2025-01-11
 */

package com.example.buspass.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.buspass.entity.BusPass
import com.example.buspass.screens.componets.BusPassList

/**
 * HomeScreen
 *
 * The main screen of the application that displays the list of bus passes
 * and includes a floating action button (FAB) to navigate to the "Add Bus Pass" screen.
 *
 * @param busPasses The list of bus passes to display in the list.
 * @param onAddPassClick A callback triggered when the FAB is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    busPasses: List<BusPass>, // List of bus passes to display
    onAddPassClick: () -> Unit, // Callback for FAB to navigate to the Add Bus Pass screen
    onEdit: (BusPass) -> Unit, // Callback to handle the edit action
    onDelete: (BusPass) -> Unit // Callback to handle the delete action
) {
    Scaffold(
        // Top App Bar
        topBar = {
            TopAppBar(
                title = { Text("Bus Pass") } // Title of the app
            )
        },
        // Floating Action Button
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPassClick, // Navigate to Add Bus Pass screen
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Add Bus Pass") // Icon for the FAB
                }
            )
        }
    ) { paddingValues ->
        // Display the list of bus passes with padding applied
        BusPassList(
            busPasses = busPasses,
            modifier = Modifier.padding(paddingValues),
            onEdit = onEdit, // Handle edit action,
            onDelete = onDelete // Handle delete action
        )
    }
}
