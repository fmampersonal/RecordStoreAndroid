package com.example.recordstore.chat

import android.R.attr.content
import android.R.attr.text
import android.R.id.content
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messageList by lazy { mutableStateListOf<MessageModel>() }
    private var generativeModel: GenerativeModel? = null

    // This is the magic! We inject your Room DB data directly into the AI's brain.
    fun initializeAI(inventoryContext: String) {
        if (generativeModel == null) {
            generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = Constants.apiKey,
                systemInstruction = content {
                    text("You are a helpful AI assistant for a Record Store app. " +
                            "You help the owner manage their inventory. " +
                            "Here is their current database of records:\n$inventoryContext\n" +
                            "Please answer their questions based ONLY on this data.")
                }
            )
        }
    }

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                // 1. Show user message and "Typing..." indicator
                messageList.add(MessageModel(question, "user"))
                messageList.add(MessageModel("Typing...", "model"))

                // 2. Load previous chat history
                val history = messageList.dropLast(1).map {
                    content(it.role) { text(it.message) }
                }
                val chat = generativeModel?.startChat(history)

                // 3. Send message to Google
                val response = chat?.sendMessage(question)

                // 4. Remove "Typing..." and show actual AI answer
                messageList.removeAt(messageList.lastIndex)
                messageList.add(MessageModel(response?.text ?: "No response", "model"))

            } catch (e: Exception) {
                messageList.removeAt(messageList.lastIndex)
                messageList.add(MessageModel("AI Error: Check API Key or connection.", "model"))
            }
        }
    }
}