package com.example.mood.model

data class UserMood(
    val id: Int,
    val entry: String,
    val type: Mood,
)
