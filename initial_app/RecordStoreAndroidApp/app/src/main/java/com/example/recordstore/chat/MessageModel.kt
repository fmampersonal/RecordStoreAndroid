package com.example.recordstore.chat

data class MessageModel(
    val message: String,
    val role: String // "user" or "model"
)