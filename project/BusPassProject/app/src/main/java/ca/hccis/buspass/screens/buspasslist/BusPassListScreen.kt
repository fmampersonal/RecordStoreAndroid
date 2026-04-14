/**
 * BusPassList.kt
 *
 * Displays the main screen of the Bus Pass Manager application.
 *
 * This screen includes a top app bar, a floating action button (FAB) for navigating
 * to the "Add Bus Pass" screen, and a list of bus passes.
 *
 * @Author: Anthony Odu
 * @Date: 2025-01-11
 */

package ca.hccis.buspass.screens.buspasslist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.hccis.buspass.entity.BusPass
import kotlinx.coroutines.launch


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
fun BusPassListScreen(
    busPasses: List<BusPass>, // List of bus passes to display
    onAddPassClick: () -> Unit, // Callback for FAB to navigate to the Add Bus Pass screen
    onSyncClick: () -> Unit, // Callback for "Sync" FAB to fetch data from API
    onEdit: (BusPass) -> Unit, // Callback to handle the edit action
    onQrCode: (BusPass) -> Unit, // Callback to handle the qr code action
    onDelete: (BusPass) -> Unit, // Callback to handle the delete action
    onUndoDelete: (BusPass) -> Unit, // Callback to handle undo action
    onShare: (BusPass) -> Unit, // Callback to handle undo action
    onChatClick: () -> Unit, //Callback for chat page
    windowSize: WindowSizeClass

) {

    //******************************************************************************************
    //This is an object that manages and creates state of the Snackbar.
    //It keeps track of whether a Snackbar is currently visible and handles when its shown or dismissed.
    //******************************************************************************************
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope() // Coroutine scope for showing snackbar

    Scaffold(
        // Top App Bar
        topBar = {
            TopAppBar(
                title = { Text("Bus Pass List") } // Title of the app
            )
        },


        floatingActionButton = {

            // Floating Action Buttons


            when (windowSize.widthSizeClass) {
                WindowWidthSizeClass.Expanded -> {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(Modifier.weight(1f))
                        BusPassFabs(
                            onChatClick = onChatClick,
                            onSyncClick = onSyncClick,
                            onAddPassClick = onAddPassClick
                        )
                    }
                }
            }



            when (windowSize.widthSizeClass) {
                //https://dev.to/bestremoteteam/complete-guide-to-layouts-in-compose-android-jetpack-compose-223n
                WindowWidthSizeClass.Compact -> {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        BusPassFabs(
                            onChatClick = onChatClick,
                            onSyncClick = onSyncClick,
                            onAddPassClick = onAddPassClick
                        )
                    }
                }
            }
        },
        //This is the UI component that shows the Snackbar when the SnackbarHostState requests it.
        //It’s added to the Scaffold so that it appears at the bottom of the screen
        //TODO - Get this to show at the bottum of the screen.
        snackbarHost = { SnackbarHost(snackbarHostState) }

    ) { paddingValues ->
        // Display the list of bus passes with padding applied
        BusPassList(
            busPasses = busPasses,
            modifier = Modifier.padding(paddingValues),
            onEdit = onEdit, // Handle edit action,
            onQrCode = onQrCode, // Handle qr code action,
            onDelete = { busPass ->
                onDelete(busPass) // Perform the delete action

                // Shows the snackbar notification with an "Undo" action
                coroutineScope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "Bus pass deleted", // Snackbar message
                        actionLabel = "Undo", // Action label
                        duration = SnackbarDuration.Short // Snackbar duration
                    )

                    // If "Undo" action was clicked, restore the deleted bus pass
                    if (result == SnackbarResult.ActionPerformed) {
                        onUndoDelete(busPass) // Undo the delete action
                    }
                }
            },
            onShare = onShare
        )

    }
}
