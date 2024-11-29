package com.example.mood.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_type")

data class MoodType(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val name: String
)


