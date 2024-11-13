package com.example.mood

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mood.ui.theme.MOOdTheme
import java.time.LocalDate
import java.time.YearMonth
import com.example.mood.model.Mood
import com.example.mood.model.MoodHistory


class MainActivity : ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sampleMoodLogs = listOf(
                MoodHistory(1, 1, Mood.HAPPY, LocalDate.of(2024, 11, 1)),
                MoodHistory(2, 1, Mood.SAD, LocalDate.of(2024, 11, 2)),
                MoodHistory(3, 1,Mood.NEUTRAL, LocalDate.of(2024, 11, 3)),
                MoodHistory(4,1,Mood.ANXIOUS, LocalDate.of(2024, 11, 4)),
                MoodHistory(5,1, Mood.HAPPY, LocalDate.of(2024, 11, 5)),
                MoodHistory(6,1, Mood.ANGRY, LocalDate.of(2024, 11, 6)),
            )

            MoodCalendar(monthLogs = sampleMoodLogs)
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun MoodCalendar(monthLogs: List<MoodHistory>) {
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
fun MoodDayItem(date: LocalDate, mood: Mood?) {
    val moodColor = when (mood) {
        Mood.HAPPY -> Color.Green
        Mood.SAD -> Color.Blue
        Mood.NEUTRAL -> Color.Gray
        Mood.ANGRY -> Color.Red
        Mood.ANXIOUS -> Color.Yellow
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
        MoodHistory(1, 1, Mood.HAPPY, LocalDate.of(2024, 11, 1)),
        MoodHistory(2, 1, Mood.SAD, LocalDate.of(2024, 11, 2)),
        MoodHistory(3, 1,Mood.NEUTRAL, LocalDate.of(2024, 11, 3)),
        MoodHistory(4,1,Mood.ANXIOUS, LocalDate.of(2024, 11, 4)),
        MoodHistory(5,1, Mood.HAPPY, LocalDate.of(2024, 11, 5)),
        MoodHistory(6,1, Mood.ANGRY, LocalDate.of(2024, 11, 6)),
    )
    MoodCalendar(monthLogs = sampleMoodLogs)
}

@Preview(showBackground = true)
@Composable
fun MoodCalendarPreview() {
    PreviewMoodCalendar()
}
