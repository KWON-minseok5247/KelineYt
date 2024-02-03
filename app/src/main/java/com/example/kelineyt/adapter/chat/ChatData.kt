package com.example.kelineyt.adapter.chat

data class ChatData(
    val id: String?,
    val writer: UserData,
    val contact: UserData,
    val messages: List<MessageData>
)
data class UserData(
    val id: Int?,
    val nickname: String
)
data class MessageData(
    val content: String,
    val createdAt: Long,
    val from: Int
)