package com.example.mood.objects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mood.model.MoodHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moodHistory: MoodHistory)

    @Update
    suspend fun update(moodHistory: MoodHistory)

    @Delete
    suspend fun delete(moodHistory: MoodHistory)

    @Query("SELECT * FROM mood_history WHERE id = :userId")
    fun getMoodHistoryById(userId: Int): Flow<MoodHistory>
}