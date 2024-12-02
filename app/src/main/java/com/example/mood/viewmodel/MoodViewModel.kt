package com.example.mood.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mood.ui.UiState
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoodViewModel() : ViewModel() {
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
}