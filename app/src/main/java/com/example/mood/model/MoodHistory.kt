package com.example.mood.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import com.example.mood.converters.LocalDateTimeConverter
import java.time.LocalDateTime


@Entity(
    tableName = "user_mood_history",
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
@TypeConverters(LocalDateTimeConverter::class)
data class MoodHistory(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val userId: Int,
    var userMoodId: Int,
    val dateLogged: LocalDateTime
)
