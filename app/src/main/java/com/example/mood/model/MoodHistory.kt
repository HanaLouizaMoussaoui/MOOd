package com.example.mood.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.time.LocalDate

@Entity(
    tableName = "mood_history",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = UserMood::class,
        parentColumns = ["id"],
        childColumns = ["userMoodId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MoodHistory(
    @PrimaryKey(autoGenerate = true)val id: Int = 1,
    val userId: Int,
    val userMoodId: UserMood,
    val dateLogged: LocalDate
)
