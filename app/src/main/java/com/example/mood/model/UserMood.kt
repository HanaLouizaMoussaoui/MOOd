package com.example.mood.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import com.example.mood.converters.LocalDateTimeConverter
import java.time.LocalDateTime

@Entity(tableName = "user_mood",
    foreignKeys = [ForeignKey(
        entity = MoodType::class,
        parentColumns = ["id"],
        childColumns = ["typeId"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)

@TypeConverters(LocalDateTimeConverter::class)
data class UserMood(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val entry: String?,
    val typeId: Int,
    val userId: Int,
    val dateLogged: LocalDateTime
)
