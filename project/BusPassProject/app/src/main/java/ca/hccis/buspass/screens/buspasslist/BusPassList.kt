/**
 * BusPassList.kt
 *
 * Displays a list of bus passes or a message if the list is empty.
 *
 * @Author: Anthony Odu
 * @Date: 2025-01-11
 */

package ca.hccis.buspass.screens.buspasslist

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.utility.Dimensions

/**
 * BusPassList
 *
 * A composable to display a list of bus passes in a scrollable view or
 * show a message when the list is empty.
 *
 * @param busPasses A list of BusPass objects to display.
 * @param modifier Modifier for customizing the layout of the list.
 */
/**
 * BusPassList.kt
 *
 * Displays a list of bus passes or a message if the list is empty.
 *
 * @Author: Anthony Odu
 * @Date: 2025-01-11
 */

@Composable
fun BusPassList(
    busPasses: List<BusPass>,
    modifier: Modifier = Modifier,
    onEdit: (BusPass) -> Unit, // Callback to handle the edit action
    onQrCode: (BusPass) -> Unit, // Callback to handle the qr code action
    onDelete: (BusPass) -> Unit, // Callback to handle the delete action
    onShare: (BusPass) -> Unit // Callback to handle the share action
) {
    if (busPasses.isEmpty()) {
        // Display a centered message when there are no bus passes
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize() // Occupy the full available size
        ) {
            Text("No bus passes added. Click the FAB to add one.") // Message for empty state
        }
    } else {
        // Display the list of bus passes in a scrollable LazyColumn
        LazyColumn(
            modifier = modifier.padding(Dimensions.space16) // Add padding around the list
        ) {
            // Iterate over the list of bus passes and display each in a BusPassCard
            items(busPasses) { busPass ->
                BusPassCard(
                    busPass = busPass,
                    onEdit = onEdit, // Handle edit action
                    onQrCode = onQrCode,
                    onDelete = onDelete, // Handle delete action
                    onShare = onShare
                )
            }
        }
    }
}
