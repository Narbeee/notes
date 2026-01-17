package com.example.notes

data class NoteResponse(
    val message: String,
    val id: Int?,      // Pastikan ada ini
    val title: String?,
    val content: String?
)