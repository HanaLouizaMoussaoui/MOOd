package com.example.mood.model

import java.time.LocalDateTime

data class MoodHistory(
    val id: Int,
    val userId: Int,
    val mood: Mood,
    val dateLogged: LocalDateTime
)
