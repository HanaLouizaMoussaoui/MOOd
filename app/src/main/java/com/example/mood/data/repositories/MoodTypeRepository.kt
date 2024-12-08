package com.example.mood.data.repositories

import com.example.mood.model.MoodType
import com.example.mood.objects.MoodTypeDao

class MoodTypeRepository(private val moodTypeDao: MoodTypeDao) :MoodTypeDao {
    override suspend fun insert(moodType: MoodType) {
        return moodTypeDao.insert(moodType)
    }

    override suspend fun update(moodType: MoodType) {
        return moodTypeDao.update(moodType)
    }

    override suspend fun delete(moodType: MoodType) {
        return moodTypeDao.delete(moodType)
    }

    override suspend fun getAllMoodTypes(): List<MoodType> {
        return moodTypeDao.getAllMoodTypes()
    }

    override suspend fun getMoodTypeByName(moodTypeName: String): MoodType? {
        return moodTypeDao.getMoodTypeByName(moodTypeName)
    }

}