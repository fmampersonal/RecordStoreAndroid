package com.example.recordstore.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recordstore.viewmodel.AlbumViewModel

@Composable
fun ChatPage(
    albumViewModel: AlbumViewModel,
    chatViewModel: ChatViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    var messageInput by remember { mutableStateOf("") }

    // Grab the database list and turn it into text for the AI
    val allAlbums by albumViewModel.allAlbums.collectAsState(initial = emptyList())
    val inventoryText = allAlbums.joinToString("\n") {
        "- ${it.artistName} (${it.formatType}) for $${it.totalCost}"
    }

    // Initialize the AI with the database string!
    LaunchedEffect(inventoryText) {
        if (inventoryText.isNotEmpty()) {
            chatViewModel.initializeAI(inventoryText)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Simple Header
        Text("Record Store AI Assistant", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Chat History List
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(chatViewModel.messageList) { model ->
                ChatBubble(model)
            }
        }

        // Input Box
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageInput,
                onValueChange = { messageInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask about your records...") }
            )
            IconButton(
                onClick = {
                    if (messageInput.isNotBlank()) {
                        chatViewModel.sendMessage(messageInput)
                        messageInput = ""
                    }
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun ChatBubble(model: MessageModel) {
    val isModel = model.role == "model"
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isModel) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary)
                .padding(12.dp)
        ) {
            Text(
                text = model.message,
                color = if (isModel) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}