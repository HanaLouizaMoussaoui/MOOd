package com.example.mood

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mood.data.DatabaseProvider
import com.example.mood.data.repositories.MoodTypeRepository
import com.example.mood.data.repositories.UserMoodHistoryRepository
import com.example.mood.data.repositories.UserMoodRepository
import com.example.mood.data.repositories.UserRepository
import com.example.mood.model.MoodType
import com.example.mood.model.enums.MoodTypeEnum
import com.example.mood.ui.screens.HomeScreen
import com.example.mood.ui.screens.LogMoodScreen
import com.example.mood.ui.screens.LoginScreen
import com.example.mood.ui.screens.RegisterScreen
import com.example.mood.ui.screens.UserAccountScreen
import com.example.mood.ui.theme.MOOdTheme
import com.example.mood.viewmodel.MoodViewModel
import com.example.mood.viewmodel.MoodViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val localNavController = compositionLocalOf<NavController> { error("No NavController found!") }



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
        seedDatabase(moodTypeRepository)
        //remove()
        setContent {

            val currentUser by moodViewModel.currentUser.collectAsState()


            var selectedTheme = currentUser?.colourTheme ?: "Default"


            MOOdTheme (themeMode = selectedTheme){

                Router(moodViewModel, onThemeSelect = { selectedTheme = currentUser?.colourTheme ?: "Default" } )

            }




        }
    }

    @Composable
    fun Router(moodViewModel: MoodViewModel, onThemeSelect: (String) -> Unit) {
        // Setting the nav controller
        val navController = rememberNavController()
        CompositionLocalProvider(localNavController provides navController) {
            NavHost(navController = navController, startDestination = "Register") {
                // Defining the routes and their corresponding screens
                composable("LoginScreenRoute") { LoginScreen(PaddingValues(8.dp), moodViewModel) }
                composable("HomeScreen") { HomeScreen(PaddingValues(8.dp), moodViewModel) }
                composable("UserAccount") { UserAccountScreen(PaddingValues(8.dp), moodViewModel, onThemeSelect) }
                composable("LogMood") { LogMoodScreen(PaddingValues(8.dp), moodViewModel) }
                composable("Register") { RegisterScreen(PaddingValues(8.dp), moodViewModel) }
            }
        }
    }

    private fun remove(){
        CoroutineScope(Dispatchers.IO).launch {
            val moods = moodTypeRepository.getAllMoodTypes()
            moods.forEach {
                moodTypeRepository.delete(it)
            }
        }
    }

    private fun seedDatabase(moodTypeRepository: MoodTypeRepository) {
        CoroutineScope(Dispatchers.IO).launch {
            val moods = moodTypeRepository.getAllMoodTypes()
            if (moods.isEmpty()) {
                val moodTypes = MoodTypeEnum.entries.map { MoodType(name = it.mood) }
                moodTypes.forEach {
                    moodTypeRepository.insert(it)
                }
            }
            else {
                val moodTypes = MoodTypeEnum.entries.map { MoodType(name = it.mood) }
                moodTypes.forEach {
                    if (!moodViewModel.checkMoodTypeExists(it.name)) {
                        moodTypeRepository.insert(it)
                    }
                }
            }
        }
    }
}
