package com.example.mood.data.repositories

import com.example.mood.model.MoodType
import com.example.mood.objects.MoodTypeDao

class MoodTypeRepository(private val moodTypeDao: MoodTypeDao) :MoodTypeDao {
    override suspend fun insert(moodType: MoodType) {
        moodTypeDao.insert(moodType)
    }

    override suspend fun update(moodType: MoodType) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(moodType: MoodType) {
        moodTypeDao.delete(moodType)
    }

    override suspend fun getAllMoodTypes(): List<MoodType> {
        return moodTypeDao.getAllMoodTypes()
    }

    override suspend fun getMoodTypeById(moodTypeId: Int): MoodType {
        return moodTypeDao.getMoodTypeById(moodTypeId)
    }

}