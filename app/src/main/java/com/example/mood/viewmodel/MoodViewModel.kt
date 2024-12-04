package com.example.mood.viewmodel

import androidx.lifecycle.ViewModel
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mood.data.repositories.MoodTypeRepository
import com.example.mood.data.repositories.UserMoodHistoryRepository
import com.example.mood.data.repositories.UserMoodRepository
import com.example.mood.data.repositories.UserRepository
import com.example.mood.model.MoodType
import com.example.mood.model.User
import com.example.mood.model.enums.MoodTypeEnum
import com.example.mood.ui.UiState
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MoodViewModel(
    private val userRepository: UserRepository,
    private val userMoodRepository: UserMoodRepository,
    private val userMoodHistoryRepository: UserMoodHistoryRepository,
    private val moodTypeRepository: MoodTypeRepository
) : ViewModel() {
    //variables for user:
    private val _currentUser = MutableStateFlow<User?>(null)  // Track logged-in user
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // variables for AI:
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    private val _results = MutableStateFlow<List<ResultItem>>(emptyList())
    val results: StateFlow<List<ResultItem>> = _results.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyBWs3W5AfZTHoo57RcG0kJXNHqOFrFuxdM"
    )

    fun sendPrompt(prompt: String) {
        _results.value += ResultItem(prompt = prompt, isLoading = true)
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.value = UiState.Success(outputContent)

                    val updatedResults = _results.value.map {
                        if (it.prompt == prompt) it.copy(isLoading = false, result = outputContent)
                        else it
                    }
                    _results.value = updatedResults
                } ?: run {
                    val updatedResults = _results.value.map {
                        if (it.prompt == prompt) it.copy(isLoading = false, result = "The AI response was empty.")
                        else it
                    }
                    _results.value = updatedResults
                }

            } catch (e: Exception) {
                val updatedResults = _results.value.map {
                    if (it.prompt == prompt) it.copy(isLoading = false, result = "Error: ${e.localizedMessage ?: "Unknown error"}")
                    else it
                }
                _results.value = updatedResults
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }
    data class ResultItem(
        val prompt: String,
        val result: String? = null,
        val isLoading: Boolean = false
    )

    suspend fun checkUserExistsByEmail(email: String): Boolean{
         return userRepository.getUserByEmail(email) != null
    }

    suspend fun createUser(username:String, email: String, password:String) {
        val currentTime = LocalDateTime.now()
        val user = User(name = username, email = email, password = password, createdAt = currentTime, editedAt = currentTime, profilePicture = "")
        userRepository.insert(user)
    }

    // returns the user object if it exists, otherwise null
    suspend fun loginUser(email: String, password: String): User?{
        val user = userRepository.getUserByEmail(email)
        // making sure the password matches
        return if (user != null && password == user.password){
            _currentUser.value = user
            user
        } else {
            null
        }
    }

    suspend fun getAllMoodTypes(): List<MoodTypeEnum> {
        val moodTypes = moodTypeRepository.getAllMoodTypes()
        return moodTypes.mapNotNull { moodType ->
            MoodTypeEnum.entries.find { it.mood == moodType.name }
        }
    }

    suspend fun checkMoodTypeExists(moodTypeName: String): Boolean {
        return moodTypeRepository.getMoodTypeByName(moodTypeName) != null
    }
}

class MoodViewModelFactory(
    private val userRepository: UserRepository,
    private val userMoodRepository: UserMoodRepository,
    private val userMoodHistoryRepository: UserMoodHistoryRepository,
    private val moodTypeRepository: MoodTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoodViewModel(userRepository, userMoodRepository, userMoodHistoryRepository, moodTypeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}