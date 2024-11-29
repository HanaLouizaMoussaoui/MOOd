package com.example.mood.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.Instant
import java.time.ZoneOffset

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

class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
        }
    }

    @TypeConverter
    fun toTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }
}