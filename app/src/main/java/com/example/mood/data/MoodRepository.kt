package com.example.mood.data

import com.example.mood.model.MoodType
import com.example.mood.model.User
import com.example.mood.objects.UserDao
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

class MoodRepository(private val userDao: UserDao) {
    suspend fun addUser(name: String,
                        email: String,
                        password: String,
                        createdAt: LocalDateTime,
                        editedAt: LocalDateTime,
                        profilePicture: String?,
                        mood: MoodType?) {
        userDao.insert(
            User(name = name, email = email, password = password, createdAt = createdAt,
            editedAt = editedAt, profilePicture = profilePicture, mood = mood)
        )
    }

    suspend fun getUserById(id: Int) : User {
        return userDao.getUserById(id).first()
    }
}