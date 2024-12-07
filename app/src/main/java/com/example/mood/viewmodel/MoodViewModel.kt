package com.example.mood.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mood.data.repositories.MoodTypeRepository
import com.example.mood.data.repositories.UserMoodHistoryRepository
import com.example.mood.data.repositories.UserMoodRepository
import com.example.mood.data.repositories.UserRepository
import com.example.mood.model.MoodHistory
import com.example.mood.model.User
import com.example.mood.model.UserMood
import com.example.mood.model.enums.MoodTypeEnum
import com.example.mood.ui.UiState
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
    private val defaultThoughts = listOf("Today was a great day, a few things happened that made me smile. How can I keep this mood up?",
        "I'm feeling a bit down today, I think it's because of the weather. How can I cheer myself up? What resources would you recommend",
        "I find myself feeling angry today for seemingly no reason. How can I relax a little and change perspective?",
        "I'm feeling a bit anxious today, I think it's because of the upcoming deadline. How can I calm myself down? What resources woudl you recommend?",
        "I'm feeling very excited today, I think it's because of the good news I received. How can I keep this excitement going?",
        "I feel very mellow and relaxed today. How can I keep this feeling going?",
        "I feel a little confused, like I have a brain fog today. How can I clear my mind?",
        "I feel pretty neutral today, like I don't feel much of anything. How can I change that?",)

    //variables for user:
    private val _currentUser = MutableStateFlow<User?>(null)  // Track logged-in user
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _moodHistory = MutableStateFlow<List<MoodHistory>>(emptyList())
    val moodHistory: StateFlow<List<MoodHistory>> = _moodHistory.asStateFlow()

    // variables for AI:
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result.asStateFlow()

    private val _currentPrompt = MutableStateFlow("Your messages appear here.")
    val currentPrompt: StateFlow<String> = _currentPrompt.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyBWs3W5AfZTHoo57RcG0kJXNHqOFrFuxdM"
    )

    fun sendPrompt(prompt: String) {
        _result.value = ResultItem(prompt = prompt, isLoading = true).toString()
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text("The user said: " + prompt + " You are MOOd, a personal wellness assistant. Please answer in a kindly and friendly" +
                                "manner, as if you are friends. Please answer in plain text, with no markdown.")
                    }
                )
                _currentPrompt.value = prompt
                val outputContent = response.text ?: "The AI response was empty."
                _result.value = ResultItem(prompt = prompt, result = outputContent, isLoading = false).toString()
                _uiState.value = UiState.Success(outputContent)

            } catch (e: Exception) {
                val errorMessage = "Error: ${e.localizedMessage ?: "Unknown error"}"
                _result.value = ResultItem(prompt = prompt, result = errorMessage, isLoading = false ).toString()
                _uiState.value = UiState.Error(errorMessage)
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
        val user = User(name = username, email = email, password = password, createdAt = currentTime, editedAt = currentTime,
            profilePicture = "", colourTheme = "Default")
        userRepository.insert(user)
    }

    // returns the user object if it exists, otherwise null
    suspend fun loginUser(email: String, password: String): User?{
        val user = userRepository.getUserByEmail(email)
        // making sure the password matches
        return if (user != null && password == user.password){
            _currentUser.value = user
            _moodHistory.value = getMoodHistoryFromUserId(user.id)
            user
        } else {
            null
        }
    }

    suspend fun updateUserInfo(user: User, name: String, email: String, password: String, theme: String){
        try {
            val currentTime = LocalDateTime.now()
            val updatedUser = User(
                id = user.id,
                name = name,
                email = email,
                password = password,
                createdAt = user.createdAt,
                editedAt = currentTime,
                profilePicture = user.profilePicture,
                colourTheme = theme
                )
            userRepository.update(updatedUser)
        }
        catch (_: Exception){}

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

    suspend fun logMood(selectedMood: MoodTypeEnum, thoughts: String?) {
        val currentUser = _currentUser.value
        if (currentUser != null) {
            val currentTime = LocalDateTime.now()
            val defaultThoughtMap = mapOf(
                MoodTypeEnum.HAPPY.id to defaultThoughts[0],
                MoodTypeEnum.SAD.id to defaultThoughts[1],
                MoodTypeEnum.ANGRY.id to defaultThoughts[2],
                MoodTypeEnum.ANXIOUS.id to defaultThoughts[3],
                MoodTypeEnum.EXCITED.id to defaultThoughts[4],
                MoodTypeEnum.CALM.id to defaultThoughts[5],
                MoodTypeEnum.CONFUSED.id to defaultThoughts[6],
                MoodTypeEnum.NEUTRAL.id to defaultThoughts[7]
            )
            val userMood = UserMood(
                typeId = selectedMood.id,
                entry = thoughts,
                userId = currentUser.id,
                dateLogged = currentTime
            )
            userMoodRepository.insert(userMood)

            val insertedMood = userMoodRepository.getMostRecentUserMood(currentUser.id)
            val userMoodHistory = MoodHistory(
                userId = currentUser.id,
                userMoodId = insertedMood.id,
                dateLogged = currentTime
            )
            addToUserHistory(userMoodHistory)

            val finalThoughts = thoughts ?: defaultThoughtMap[selectedMood.id]
            val prompt = "I'm feeling ${selectedMood.mood}. $finalThoughts"
            _currentPrompt.value = prompt
            sendPrompt(prompt)
        }
    }

    private suspend fun addToUserHistory(userMoodHistory: MoodHistory) {
        userMoodHistoryRepository.insert(userMoodHistory)
    }



    suspend fun getMoodDateAndTypeFromUserId(): List<MoodHistory> {
        val userId = currentUser.value?.id ?: return emptyList()
        val moodHistory = getMoodHistoryFromUserId(userId).toMutableList()
        for (history in moodHistory) {
            val userMood = getUserMoodFromMoodId(history.userMoodId)
            history.userMoodId = userMood.typeId
        }
        return moodHistory.toList()
    }

     suspend fun getMoodHistoryFromUserId(userId: Int): List<MoodHistory> {
        return userMoodHistoryRepository.getUserMoodHistoryByUserId(userId)

    }

    suspend fun getUserMoodFromMoodId(moodId: Int): UserMood{
        return userMoodRepository.getUserMoodById(moodId)

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