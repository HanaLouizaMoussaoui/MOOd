package com.example.mood.data.repositories

import com.example.mood.objects.UserMoodHistoryDao
import com.example.mood.model.MoodHistory

class UserMoodHistoryRepository(private val userMoodHistoryDao: UserMoodHistoryDao) : UserMoodHistoryDao{
    override suspend fun insert(userMoodHistory: MoodHistory) {
        return userMoodHistoryDao.insert(userMoodHistory)
    }

    override suspend fun update(userMoodHistory: MoodHistory) {
        return userMoodHistoryDao.update(userMoodHistory)
    }

    override suspend fun delete(userMoodHistory: MoodHistory) {
        return userMoodHistoryDao.delete(userMoodHistory)
    }

    override suspend fun getUserMoodHistoryByUserId(userId: Int): MoodHistory {
        return userMoodHistoryDao.getUserMoodHistoryByUserId(userId)
    }
}