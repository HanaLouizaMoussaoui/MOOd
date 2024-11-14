package com.example.mood.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserMood(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val entry: String,
    val type: MoodType,
)
