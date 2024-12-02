package com.example.mood.data.repositories

import com.example.mood.model.UserMood
import com.example.mood.objects.UserMoodDao

class UserMoodRepository(private val userMoodDao: UserMoodDao) : UserMoodDao {
    override suspend fun insert(userMood: UserMood) {
        return userMoodDao.insert(userMood)
    }

    override suspend fun update(userMood: UserMood) {
        return userMoodDao.update(userMood)
    }

    override suspend fun delete(userMood: UserMood) {
        return userMoodDao.delete(userMood)
    }

    override suspend fun getUserMoodById(userMoodId: Int): UserMood {
        return userMoodDao.getUserMoodById(userMoodId)
    }

}