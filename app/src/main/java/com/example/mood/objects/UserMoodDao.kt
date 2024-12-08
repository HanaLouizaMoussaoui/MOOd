package com.example.mood.objects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mood.model.UserMood

@Dao
interface UserMoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userMood: UserMood)

    @Update
    suspend fun update(userMood: UserMood)

    @Delete
    suspend fun delete(userMood: UserMood)

    @Query("SELECT * FROM user_mood WHERE id = :userMoodId")
    suspend fun getUserMoodById(userMoodId: Int): UserMood

    @Query("SELECT * FROM user_mood WHERE userId = :userId")
    suspend fun getUserMoodByUserId(userId: Int): UserMood

    @Query("SELECT * FROM user_mood WHERE userId = :userId ORDER BY dateLogged DESC LIMIT 1")
    suspend fun getMostRecentUserMood(userId: Int): UserMood
}