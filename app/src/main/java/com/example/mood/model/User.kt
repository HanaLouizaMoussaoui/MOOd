package com.example.mood.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mood.converters.LocalDateTimeConverter
import java.time.LocalDateTime

@Entity(tableName = "user")
@TypeConverters(LocalDateTimeConverter::class)
data class User(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val createdAt: LocalDateTime,
    val editedAt: LocalDateTime,
    val profilePicture: String?,
)

