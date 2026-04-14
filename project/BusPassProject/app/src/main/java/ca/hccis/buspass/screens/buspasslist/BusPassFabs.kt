/**
 * BusPassCard.kt
 *
 * Displays a card containing detailed information about a bus pass in a formatted way.
 * Provides options to edit or delete the bus pass.
 *
 * @Author: Anthony Odu
 * @Date: 2025-01-11
 */

package ca.hccis.buspass.screens.buspasslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.utility.CisUtility
import ca.hccis.buspass.utility.Dimensions

/**
 * BusPassFabs
 * Displays the fabs
 */

@Composable
fun BusPassFabs(onChatClick: () -> Unit,
                onSyncClick: () -> Unit,
                onAddPassClick: () -> Unit
) {
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                            onClick = onChatClick, //Chat
                            content = {
                                Icon(
                                    Icons.Default.QuestionMark,
                                    contentDescription = "Chat Page"
                                ) // Chat FAB
                            },
                            modifier = Modifier
                                .padding(all = 6.dp) // Add spacing between FABs
                        )
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                            onClick = onSyncClick, // Sync bus passes with API
                            content = {
                                Icon(
                                    Icons.Default.Sync,
                                    contentDescription = "Sync Bus Passes"
                                ) // Sync FAB
                            },
                            modifier = Modifier
                                .padding(all = 6.dp) // Add spacing between FABs
                        )
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                            onClick = onAddPassClick, // Navigate to Add Bus Pass screen
                            content = {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add Bus Pass"
                                ) // Add FAB
                            },
                            modifier = Modifier
                                .padding(all = 6.dp) // Add spacing between FABs
                        )

}
