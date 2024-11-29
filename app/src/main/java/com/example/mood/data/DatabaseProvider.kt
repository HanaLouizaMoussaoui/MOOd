package com.example.mood.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mood.model.MoodType
import com.example.mood.model.User
import com.example.mood.objects.MoodTypeDao
import com.example.mood.objects.UserDao

object DatabaseProvider {
    @Database(entities = [User::class, MoodType::class], version = 2, exportSchema = false)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
        abstract fun moodTypeDao(): MoodTypeDao

        companion object {
            @Volatile
            private var INSTANCE: AppDatabase? = null

            fun getDatabase(context: Context): AppDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "mood_database"
                    ).build()
                    INSTANCE = instance
                    instance
                }

            }
        }
    }
}