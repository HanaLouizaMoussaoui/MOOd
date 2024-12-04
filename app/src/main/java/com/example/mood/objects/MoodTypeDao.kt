package com.example.mood.objects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mood.model.MoodType

@Dao
interface MoodTypeDao {
    @Insert
    suspend fun insert(moodType: MoodType)

    @Update
    suspend fun update(moodType: MoodType)

    @Delete
    suspend fun delete(moodType: MoodType)

    @Query("SELECT * FROM mood_type")
    suspend fun getAllMoodTypes(): List<MoodType>

    @Query("SELECT * FROM mood_type WHERE name = :moodTypeName")
    suspend fun getMoodTypeByName(moodTypeName: String): MoodType?
}