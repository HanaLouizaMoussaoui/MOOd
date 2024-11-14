package com.example.mood.model

import androidx.room.Entity

@Entity
enum class MoodType {
    HAPPY, SAD, NEUTRAL, ANGRY, ANXIOUS
}