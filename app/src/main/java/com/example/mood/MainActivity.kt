package com.example.mood

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mood.data.DatabaseProvider
import com.example.mood.data.repositories.MoodTypeRepository
import com.example.mood.data.repositories.UserMoodHistoryRepository
import com.example.mood.data.repositories.UserMoodRepository
import com.example.mood.data.repositories.UserRepository
import com.example.mood.ui.screens.HomeScreen
import com.example.mood.ui.screens.LogMoodScreen
import com.example.mood.ui.screens.SelectionBar
import com.example.mood.ui.screens.RegisterScreen
import com.example.mood.ui.screens.UserAccountScreen
import com.example.mood.ui.theme.MOOdTheme
import com.example.mood.viewmodel.MoodViewModel
import com.example.mood.viewmodel.MoodViewModelFactory


class MainActivity : ComponentActivity() {
    private val userRepository: UserRepository by lazy {
        val database = DatabaseProvider.AppDatabase.getDatabase(this)
        UserRepository(database.userDao())
    }
    private val moodTypeRepository: MoodTypeRepository by lazy {
        val database = DatabaseProvider.AppDatabase.getDatabase(this)
        MoodTypeRepository(database.moodTypeDao())
    }
    private val userMoodRepository: UserMoodRepository by lazy {
        val database = DatabaseProvider.AppDatabase.getDatabase(this)
        UserMoodRepository(database.userMoodDao())
    }
    private val userMoodHistoryRepository: UserMoodHistoryRepository by lazy {
        val database = DatabaseProvider.AppDatabase.getDatabase(this)
        UserMoodHistoryRepository(database.userMoodHistoryDao())
    }

    private val moodViewModel: MoodViewModel by viewModels {
        MoodViewModelFactory(userRepository, userMoodRepository, userMoodHistoryRepository, moodTypeRepository)
    }
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selectedTheme by remember { mutableStateOf("Default") }

            MOOdTheme (themeMode = selectedTheme){

                Router(moodViewModel, onThemeSelect = { selectedTheme = it } )

            }
        }
    }
    @Composable
    fun Router(moodViewModel: MoodViewModel, onThemeSelect: (String) -> Unit) {
        // Setting the nav controller
        val navController = rememberNavController()
        // Defining the routes and their corresponding screens
        NavHost(navController = navController, startDestination = "Register") {
            composable("LoginScreenRoute") { LoginScreen(PaddingValues(8.dp), moodViewModel, navController) }
            composable("HomeScreen") { HomeScreen(PaddingValues(8.dp), moodViewModel, navController) }
            composable("UserAccount") { UserAccountScreen(PaddingValues(8.dp), moodViewModel, navController, onThemeSelect) }
            composable("LogMood") { LogMoodScreen(PaddingValues(8.dp), moodViewModel, navController) }
            composable("Register") { RegisterScreen(PaddingValues(8.dp), moodViewModel, navController) }
        }
    }
}
