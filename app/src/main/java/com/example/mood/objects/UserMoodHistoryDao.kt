package com.example.mood.objects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mood.model.MoodHistory

@Dao
interface UserMoodHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userMoodHistory: MoodHistory)

    @Update
    suspend fun update(userMoodHistory: MoodHistory)

    @Delete
    suspend fun delete(userMoodHistory: MoodHistory)

    @Query("SELECT * FROM user_mood_history WHERE userId = :userId")
    suspend fun getUserMoodHistoryByUserId(userId: Int): MoodHistory
}