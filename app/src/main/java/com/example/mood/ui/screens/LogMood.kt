package com.example.mood.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mood.model.MoodHistory
import com.example.mood.model.MoodType
import com.example.mood.model.User
import com.example.mood.ui.NavBar
import com.example.mood.ui.TopBar
import com.example.mood.ui.theme.MOOdTheme
import com.example.mood.viewmodel.MoodViewModel
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun LogMoodScreen(contentPadding: PaddingValues, moodViewModel: MoodViewModel,  navController: NavHostController) {
    MOOdTheme {
        Column(
            modifier = Modifier
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Align items at the top
        ) {
            TopBar { navController.navigate("UserAccount") }
            NavBar(
                onHomeClick = { navController.navigate("HomeScreen") },
                onLogClick = { navController.navigate("LogMood") }
            )
            LogMood()
        }
    }
}




@Composable
fun LogMood() {
    Column(
    ) {
        MoodSelectionPage()

    }
}



@Composable
fun MoodSelectionPage() {
    val moods = listOf("Angry", "Sad", "Happy", "Ecstatic")
    var selectedMood by remember { mutableStateOf("") }
    var thoughts by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "How are you feeling today?",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(24.dp)
        )

        // Mood Buttons Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            moods.forEach { mood ->
                MoodButton(
                    mood = mood,
                    isSelected = selectedMood == mood,
                    onClick = { selectedMood = mood }
                )
            }
        }

        // DEBUGGING: displays selected mood
        if (selectedMood.isNotEmpty()) {
            Text(
                text = "You selected: $selectedMood",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        OutlinedTextField(
            value = thoughts,
            onValueChange = { thoughts = it },
            label = { Text("Any thoughts?") },
            placeholder = { Text("Type your thoughts here...") },
            modifier = Modifier.fillMaxWidth()
                .padding(24.dp)
        )

        // Submit Button
        Button(
            onClick = {
                println("Mood: $selectedMood, Thoughts: $thoughts")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor =  MaterialTheme.colorScheme.primary,
                contentColor = Color.Black
            ),
          //  enabled = selectedMood.isNotEmpty() && thoughts.isNotBlank(), // Only enabled if mood and thoughts are filled
            modifier = Modifier.
            width(150.dp)
        ) {
            Text("Log Mood")
        }

        Text(
            text = "Your mood history:",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )
        MoodCalendar()





    }
}

@Composable
fun MoodButton(mood: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Gray else MaterialTheme.colorScheme.tertiary,
            contentColor = Color.Black
        ),
        shape = CircleShape,
        modifier = Modifier.padding(1.dp)
    ) {
        Text(text = mood, style = MaterialTheme.typography.bodySmall)
    }
}


@SuppressLint("NewApi")
@Composable
fun MoodCalendar() {
    val sampleMoodLogs = listOf(
        MoodHistory(1, 1, MoodType.HAPPY, LocalDate.of(2024, 12, 1)),
        MoodHistory(2, 1, MoodType.SAD, LocalDate.of(2024, 12, 2)),
        MoodHistory(3, 1, MoodType.NEUTRAL, LocalDate.of(2024, 12, 3)),
        MoodHistory(4, 1, MoodType.ANXIOUS, LocalDate.of(2024, 12, 4)),
        MoodHistory(5, 1, MoodType.HAPPY, LocalDate.of(2024, 12, 5)),
        MoodHistory(6, 1, MoodType.ANGRY, LocalDate.of(2024, 12, 10)),
    )
    var user: User? = null

    val currentMonth = YearMonth.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val datesInMonth = (1..daysInMonth).map { currentMonth.atDay(it) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(datesInMonth) { date ->
            val moodForDay = sampleMoodLogs.find { it.dateLogged == date }?.mood
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
        null -> MaterialTheme.colorScheme.secondary
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