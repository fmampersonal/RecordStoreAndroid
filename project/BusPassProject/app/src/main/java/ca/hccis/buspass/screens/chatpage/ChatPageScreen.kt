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

package ca.hccis.buspass.screens.chatpage

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import np.com.bimalkafle.easybot.viewModel.ChatViewModel

/**
 * QR Code Screen
 *
 * Displays a qr code from the bus pass passed and a qr code wtih a link to the web
 * application which could take the user to the web to see the bus pass.
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPageScreen(
    back: () -> Unit,
    viewModel: ChatViewModel
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat Page") },
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
        ChatPage(
            paddingValues = paddingValues,
            viewModel
        )
    }
}
