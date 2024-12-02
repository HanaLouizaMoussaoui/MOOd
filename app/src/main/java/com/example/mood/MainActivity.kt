package com.example.mood

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.example.mood.data.DatabaseProvider
import com.example.mood.data.MoodRepository
import com.example.mood.ui.screens.HomeScreen


class MainActivity : ComponentActivity() {
    private val moodRepository: MoodRepository by lazy {
        val database = DatabaseProvider.AppDatabase.getDatabase(this)
        MoodRepository(database.userDao())
    }
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(PaddingValues(8.dp))
        }
    }
}
