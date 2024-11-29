package com.example.mood.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "user_mood",
    foreignKeys = [ForeignKey(
        entity = MoodType::class,
        parentColumns = ["id"],
        childColumns = ["typeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserMood(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val entry: String,
    val typeId: Int,
)
