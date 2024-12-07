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
    var name: String,
    var email: String,
    var password: String,
    val createdAt: LocalDateTime,
    var editedAt: LocalDateTime,
    var profilePicture: String?,
    var colourTheme: String?
)

