package com.example.mood

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mood.data.DatabaseProvider
import com.example.mood.data.MoodRepository
import com.example.mood.ui.screens.HomeScreen
import com.example.mood.ui.screens.LogMoodScreen
import com.example.mood.ui.screens.Login
import com.example.mood.ui.screens.LoginScreen
import com.example.mood.ui.screens.UserAccountScreen
import com.example.mood.ui.theme.MOOdTheme
import com.example.mood.viewmodel.MoodViewModel


class MainActivity : ComponentActivity() {
    private val moodRepository: MoodRepository by lazy {
        val database = DatabaseProvider.AppDatabase.getDatabase(this)
        MoodRepository(database.userDao())
    }
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MOOdTheme {
                val moodViewModel: MoodViewModel by viewModels()

                Router(moodViewModel)

            }
        }
    }


    @Composable
    fun Router(moodViewModel: MoodViewModel) {
        // Setting the nav controller
        val navController = rememberNavController()
        // Defining the routes and their corresponding screens
        NavHost(navController = navController, startDestination = "HomeScreen") {
            composable("LoginScreenRoute") { LoginScreen(PaddingValues(8.dp), moodViewModel, navController) }
            composable("HomeScreen") { HomeScreen(PaddingValues(8.dp), moodViewModel, navController) }
            composable("UserAccount") { UserAccountScreen(PaddingValues(8.dp), moodViewModel, navController) }
            composable("LogMood") { LogMoodScreen(PaddingValues(8.dp), moodViewModel, navController) }

        }
    }



}
