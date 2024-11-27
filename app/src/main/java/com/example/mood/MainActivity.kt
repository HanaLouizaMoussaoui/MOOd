package com.example.mood

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.mood.data.DatabaseProvider
import com.example.mood.data.repositories.UserRepository
import java.time.LocalDate
import java.time.YearMonth
import com.example.mood.model.MoodType
import com.example.mood.model.MoodHistory
import com.example.mood.model.User
import com.example.mood.ui.UiState
import com.example.mood.viewmodel.MoodViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime.now



class MainActivity : ComponentActivity() {
    private val userRepository: UserRepository by lazy {
        val database = DatabaseProvider.AppDatabase.getDatabase(this)
        UserRepository(database.userDao())
    }
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sampleMoodLogs = listOf(
                MoodHistory(1, 1, MoodType.HAPPY, LocalDate.of(2024, 11, 1)),
                MoodHistory(2, 1, MoodType.SAD, LocalDate.of(2024, 11, 2)),
                MoodHistory(3, 1, MoodType.NEUTRAL, LocalDate.of(2024, 11, 3)),
                MoodHistory(4, 1, MoodType.ANXIOUS, LocalDate.of(2024, 11, 4)),
                MoodHistory(5, 1, MoodType.HAPPY, LocalDate.of(2024, 11, 5)),
                MoodHistory(6, 1, MoodType.ANGRY, LocalDate.of(2024, 11, 6)),
            )
            var user: User? = null
            Box(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = {
                        addUser()
                    }, modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text("Add User")
                }
                Column(){
                    MoodCalendar(monthLogs = sampleMoodLogs, user = user)
                    AIPromptBox()
                }
            }
        }
    }

        fun showPopup(context: Context, title: String, message: String) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        fun addUser() {
            lifecycleScope.launch {
                userRepository.addUser(
                    name = "gabriel", password = "even_worse_password", email = "email@email.com",
                    createdAt = now(), editedAt = now(), profilePicture = null, mood = null
                )
            }
        }

    private fun getUserById(id: Int) : User? {
        var user: User? = null
        lifecycleScope.launch {
           user = userRepository.getUserById(id)
        }
        return user
    }
}

@Composable
fun AIPromptBox1(viewModel: MoodViewModel = MoodViewModel()) {
    // State holders for the TextField and the ViewModel UI state
    var prompt by rememberSaveable { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    val scrollState = rememberScrollState()
    Column(modifier = Modifier.padding(16.dp)) {
        // Input row for the prompt
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            TextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Enter prompt") },
                modifier = Modifier
                    .weight(0.8f)
                    .padding(end = 8.dp)
                    .align(Alignment.CenterVertically)
            )

            Button(
                onClick = {
                    if (prompt.isNotBlank()) {
                        viewModel.sendPrompt(prompt)
                    }
                },
                enabled = prompt.isNotBlank(),
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(text = "Send")
            }
        }

        // Dynamically display the result or loading/error states
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                .padding(8.dp)
        ) {
            Log.d("AIPromptBox", "uiState observed: $uiState")
            when (uiState) {
                is UiState.Initial -> {
                    Text(
                        text = "Awaiting your input...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                is UiState.Loading -> {
                    Text(
                        text = "Loading, please wait...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is UiState.Success -> {
                    Text(
                        text = (uiState as UiState.Success).outputText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                is UiState.Error -> {
                    Text(
                        text = (uiState as UiState.Error).errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}


@Composable
fun AIPromptBox(moodViewModel: MoodViewModel = MoodViewModel()){
    val placeholderPrompt = stringResource(R.string.prompt_placeholder)
    val placeholderResult = stringResource(R.string.results_placeholder)
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val results by moodViewModel.results.collectAsState()
    val uiState by moodViewModel.uiState.collectAsState()

    Row(
        modifier = Modifier.padding(all = 16.dp)
    ) {
        TextField(
            value = prompt,
            label = { Text("Enter prompt") },
            onValueChange = { prompt = it },
            modifier = Modifier
                .weight(0.8f)
                .padding(end = 16.dp)
                .align(Alignment.CenterVertically)
        )

        Button(
            onClick = {
                moodViewModel.sendPrompt(prompt)
            },
            enabled = prompt.isNotEmpty(),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Text(text = "Send")
        }
    }

    LazyColumn {
        items(results) { resultItem ->
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                TextField(
                    value = resultItem.result ?: if (resultItem.isLoading) "Loading..." else "No response",
                    onValueChange = {},
                    enabled = false, // Make result fields read-only
                    label = { Text("Result for: ${resultItem.prompt}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    /*
    if (uiState is UiState.Loading) {
        CircularProgressIndicator()
    } else {
        var textColor = MaterialTheme.colorScheme.onSurface
        if (uiState is UiState.Error) {
            textColor = MaterialTheme.colorScheme.error
            result = (uiState as UiState.Error).errorMessage
        } else if (uiState is UiState.Success) {
            textColor = MaterialTheme.colorScheme.onSurface
            result = (uiState as UiState.Success).outputText
        }
        val scrollState = rememberScrollState()
        Text(
            text = result,
            textAlign = TextAlign.Start,
            color = textColor,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        )
    }*/
}

@SuppressLint("NewApi")
@Composable
fun MoodCalendar(monthLogs: List<MoodHistory>, user: User?) {
    val currentMonth = YearMonth.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val datesInMonth = (1..daysInMonth).map { currentMonth.atDay(it) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(datesInMonth) { date ->
            val moodForDay = monthLogs.find { it.dateLogged == date }?.mood
            MoodDayItem(date, moodForDay)
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun MoodDayItem(date: LocalDate, mood: MoodType?) {
    val moodColor = when (mood) {
        MoodType.HAPPY -> Color.Green
        MoodType.SAD -> Color.Blue
        MoodType.NEUTRAL -> Color.Gray
        MoodType.ANGRY -> Color.Red
        MoodType.ANXIOUS -> Color.Yellow
        null -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .background(moodColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }

}

@SuppressLint("NewApi")
@Composable
fun PreviewMoodCalendar() {
    val sampleMoodLogs = listOf(
        MoodHistory(1, 1, MoodType.HAPPY, LocalDate.of(2024, 11, 1)),
        MoodHistory(2, 1, MoodType.SAD, LocalDate.of(2024, 11, 2)),
        MoodHistory(3, 1,MoodType.NEUTRAL, LocalDate.of(2024, 11, 3)),
        MoodHistory(4,1,MoodType.ANXIOUS, LocalDate.of(2024, 11, 4)),
        MoodHistory(5,1, MoodType.HAPPY, LocalDate.of(2024, 11, 5)),
        MoodHistory(6,1, MoodType.ANGRY, LocalDate.of(2024, 11, 6)),
    )
    MoodCalendar(monthLogs = sampleMoodLogs, null)
}

@Preview(showBackground = true)
@Composable
fun MoodCalendarPreview() {
    PreviewMoodCalendar()
}
