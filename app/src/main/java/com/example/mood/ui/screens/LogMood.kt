package com.example.mood.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mood.model.MoodHistory
import com.example.mood.model.UserMood
import com.example.mood.model.enums.MoodTypeEnum
import com.example.mood.ui.NavBar
import com.example.mood.ui.TopBar
import com.example.mood.ui.theme.MOOdTheme
import com.example.mood.viewmodel.MoodViewModel
import java.time.LocalDate
import java.time.LocalDateTime
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
            LogMood(moodViewModel)
        }
    }
}




@Composable
fun LogMood(moodViewModel: MoodViewModel) {
    Column {
        MoodSelectionPage(moodViewModel)

    }
}



@Composable
fun MoodSelectionPage(moodViewModel: MoodViewModel) {
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
        MoodCalendar(moodViewModel)





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
fun MoodCalendar(moodViewModel: MoodViewModel) {
    val sampleMoodLogs = listOf(
        MoodHistory(1, 1, UserMood(entry = "very nice", typeId = MoodTypeEnum.HAPPY.id).id, LocalDateTime.of(2024, 12, 1, 0,0)),
        MoodHistory(2, 1, UserMood(entry = "very bad", typeId = MoodTypeEnum.SAD.id).id, LocalDateTime.of(2024, 12, 2,0,0)),
        MoodHistory(3, 1, UserMood(entry = "neutral", typeId = MoodTypeEnum.NEUTRAL.id).id, LocalDateTime.of(2024, 12, 3,0,0)),
        MoodHistory(4, 1, UserMood(entry = "very anxious", typeId = MoodTypeEnum.ANXIOUS.id).id, LocalDateTime.of(2024, 12, 4,0,0)),
        MoodHistory(5, 1, UserMood(entry = "very happy", typeId = MoodTypeEnum.HAPPY.id).id, LocalDateTime.of(2024, 12, 5,0,0)),
        MoodHistory(6, 1, UserMood(entry = "very angry", typeId = MoodTypeEnum.ANGRY.id).id, LocalDateTime.of(2024, 12, 6,0,0)),
    )

    val currentMonth = YearMonth.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val datesInMonth = (1..daysInMonth).map { currentMonth.atDay(it) }
    val moodType = remember { mutableStateOf<MoodTypeEnum?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(datesInMonth) { date ->
            moodType.value = sampleMoodLogs.find { it.dateLogged.toLocalDate() == date }?.userMoodId?.let { moodId ->
                MoodTypeEnum.entries.find { it.id == moodId }
            }
            MoodDayItem(date, moodType.value)
        }
    }
}


@SuppressLint("NewApi")
@Composable
fun MoodDayItem(date: LocalDate, mood: MoodTypeEnum?) {
    val moodColor = when (mood?.name) {
        MoodTypeEnum.HAPPY.mood -> Color.Green
        MoodTypeEnum.SAD.mood -> Color.Blue
        MoodTypeEnum.NEUTRAL.mood -> Color.Gray
        MoodTypeEnum.ANGRY.mood -> Color.Red
        MoodTypeEnum.ANXIOUS.mood -> Color.Yellow
        else -> MaterialTheme.colorScheme.secondary
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