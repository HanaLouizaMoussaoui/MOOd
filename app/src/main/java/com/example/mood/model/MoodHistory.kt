package com.example.mood.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class MoodHistory(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val userId: Int,
    val mood: MoodType,
    val dateLogged: LocalDate
)
